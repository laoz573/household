package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBseedDAO {
    public static void initialize() {
        Connection con = null;
        Statement stmt = null;

        try {
            // JDBCドライバのロード
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 環境変数 DB_HOST を取得（なければ localhost）
            String dbHost = System.getenv("DB_HOST");
            if (dbHost == null || dbHost.trim().isEmpty()) {
                dbHost = "localhost";
            }

            // JDBC接続URLを構築
            String jdbcUrl = "jdbc:mysql://" + dbHost + ":3306/?useUnicode=true&characterEncoding=UTF-8";

            // 接続
            con = DriverManager.getConnection(jdbcUrl, "root", "root12345");
            stmt = con.createStatement();

            // household_db がなければ作成
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS household_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");

            // household テーブル作成
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS household_db.household (" +
                "id INT NOT NULL AUTO_INCREMENT," +
                "registerday DATE NOT NULL," +
                "contents TEXT NOT NULL," +
                "price INT NOT NULL," +
                "spending TEXT," +
                "income TEXT," +
                "category VARCHAR(255)," +
                "remarks TEXT NOT NULL," +
                "userID INT NOT NULL," +
                "PRIMARY KEY (id)" +
                ") DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci"
            );

            // userID テーブル作成
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS household_db.userID (" +
                "userID INT NOT NULL AUTO_INCREMENT," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "PRIMARY KEY (userID)" +
                ") DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci"
            );

            // 初期ユーザー挿入（重複しない場合のみ）
            stmt.executeUpdate(
                "INSERT IGNORE INTO household_db.userID (userID, username, password) VALUES (1, 'masaya', '3939')"
            );

            System.out.println("✅ DB初期化完了");

        } catch (ClassNotFoundException e) {
            System.err.println("❌ JDBCドライバが見つかりません");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ SQLエラーが発生しました");
            e.printStackTrace();
        } finally {
            // リソースのクローズ（省略可能だが推奨）
            try {
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}