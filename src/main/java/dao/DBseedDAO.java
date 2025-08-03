package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBseedDAO {
    public static void initialize() {

        Connection con = null;
	    Statement stmt = null;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            // household_dbがまだ無い場合も考慮してデータベース名なしで接続
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/?useUnicode=true&characterEncoding=UTF-8", "root", "root12345");

            stmt = con.createStatement();

            // household_dbがなければ作成
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS household_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");

            // householdテーブル作成
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS household_db.household (" +
                "id int not null AUTO_INCREMENT," +
                "registerday date not null," +
                "contents text not null," +
                "price int not null," +
                "spending text," +
                "income text," +
                "remarks text not null," +
                "userID int not null," +
                "PRIMARY KEY (id)" +
                ") DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci"
            );

            // userIDテーブル作成
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS household_db.userID (" +
                "userID int not null auto_increment," +
                "username text not null," +
                "password text not null," +
                "PRIMARY KEY(userID)" +
                ") DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci"
            );

            // 初期ユーザー挿入（重複しない場合のみ）
            stmt.executeUpdate(
                "INSERT IGNORE INTO household_db.userID (userID, username, password) VALUES (1, 'masaya', '3939')"
            );

            System.out.println("DB初期化完了");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}