package dao;

import model.userID;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class userIDDAO {
	public userID findbyUserID(String username, String password) {
		userID userID = null; // userIDをnullで初期化します

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// SQL文の作成
		String sql = "SELECT * FROM userID WHERE username = ? AND password = ?";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			// 環境変数 DB_HOST を取得（なければ localhost）
            String dbHost = System.getenv("DB_HOST");
            if (dbHost == null || dbHost.trim().isEmpty()) {
                dbHost = "localhost";
            }

            // JDBC接続URLを構築
            String jdbcUrl = "jdbc:mysql://" + dbHost + ":3306/household_db?useUnicode=true&characterEncoding=UTF-8";
			// データベース接続
	        con = DriverManager.getConnection(jdbcUrl, "root", "root12345");
			stmt = con.prepareStatement(sql);
			// パラメータの設定
			stmt.setString(1, username);
			stmt.setString(2, password);

			rs = stmt.executeQuery();

			while (rs.next()) {

				int getUserID = rs.getInt("userID");
				String getUsername = rs.getString("username");
				String getPassword = rs.getString("password");

				userID = new userID(getUserID, getUsername, getPassword);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("JDBCドライバのロードでエラーが発生しました");
		} catch (SQLException e) {
			System.out.println("データベースへのアクセスでエラーが発生しました。");
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println("データベースへのアクセスでエラーが発生しました。");
			}
		}
		return userID;
	}
	
	public boolean insertUserID(String username, String password) {
		Connection con = null;
		PreparedStatement stmt = null;
		int rowsInserted = 0;

		String sql = "INSERT INTO userID (username, password) VALUES (?, ?)";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String dbHost = System.getenv("DB_HOST");
			if (dbHost == null || dbHost.trim().isEmpty()) {
				dbHost = "localhost";
			}

			String jdbcUrl = "jdbc:mysql://" + dbHost + ":3306/household_db?useUnicode=true&characterEncoding=UTF-8";

			con = DriverManager.getConnection(jdbcUrl, "root", "root12345");

			// ★ 自動採番された userID を取得するために RETURN_GENERATED_KEYS を使う
			stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, username);
			stmt.setString(2, password);

			rowsInserted = stmt.executeUpdate();

			if (rowsInserted == 0) return false;

			// ★ 新規 userID を取得
			ResultSet rs = stmt.getGeneratedKeys();
			int newUserId = 0;
			if (rs.next()) {
				newUserId = rs.getInt(1);
			}

			// ★ 初期カテゴリを追加
			insertDefaultCategories(con, newUserId);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;

		} finally {
			try {
				if (stmt != null) stmt.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// ★ 初期カテゴリを登録するメソッド
	private void insertDefaultCategories(Connection con, int userId) {
		String[] defaults = {"食費", "外食費", "日用品", "光熱費", "通信費", "サブスク", "その他"};

		try {
			for (String name : defaults) {
				try (PreparedStatement ps = con.prepareStatement(
					"INSERT INTO category (userID, name) VALUES (?, ?)")) {
					ps.setInt(1, userId);
					ps.setString(2, name);
					ps.executeUpdate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
