package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

	    // SQL文の作成
	    String sql = "INSERT INTO userID (username, password) VALUES (?, ?)";

	    try {
	        // JDBCドライバのロード
	        Class.forName("com.mysql.cj.jdbc.Driver");
			// 環境変数 DB_HOST を取得（なければ localhost）
            String dbHost = System.getenv("DB_HOST");
            if (dbHost == null || dbHost.trim().isEmpty()) {
                dbHost = "localhost";
            }

            // JDBC接続URLを構築
            String jdbcUrl = "jdbc:mysql://" + dbHost + ":3306/household_db?useUnicode=true&characterEncoding=UTF-8";
			// データベース接続
	        con = DriverManager.getConnection(jdbcUrl, "root", "root12345");	        // SQL実行準備
	        stmt = con.prepareStatement(sql);

	        // パラメータの設定
	        stmt.setString(1, username);
	        stmt.setString(2, password);

	        // インサートのSQL実行
	        rowsInserted = stmt.executeUpdate();

	        if (rowsInserted > 0) {
	            System.out.println(rowsInserted + " 行が挿入されました。");
	        } else {
	            System.out.println("挿入された行はありません。");
	        }
	    } catch (ClassNotFoundException e) {
	        System.out.println("JDBCドライバのロードでエラーが発生しました");

	    } catch (SQLException e) {
	        System.out.println("データベースへのアクセスでエラーが発生しました。");

	    } finally {
	        // リソースのクローズ
	        try {
	            if (stmt != null) {
	                stmt.close();
	            }
	            if (con != null) {
	                con.close();
	            }
	        } catch (SQLException e) {
	            System.out.println("データベースクローズでエラーが発生しました。");
	        }
	    }
	// 最後に結果を返す
    return rowsInserted > 0;
	}
}
