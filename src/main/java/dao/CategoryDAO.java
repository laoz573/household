package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Category;

public class CategoryDAO {

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String dbHost = System.getenv("DB_HOST");
        if (dbHost == null || dbHost.trim().isEmpty()) {
            dbHost = "localhost";
        }

        String jdbcUrl = "jdbc:mysql://" + dbHost + ":3306/household_db?useUnicode=true&characterEncoding=UTF-8";
        return DriverManager.getConnection(jdbcUrl, "root", "root12345");
    }

    // ★ カテゴリ一覧取得（ユーザーごと）
    public List<Category> findAll(int userID) {
        List<Category> list = new ArrayList<>();

        String sql = "SELECT id, userID, name FROM category WHERE userID = ? ORDER BY id";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int uid = rs.getInt("userID");
                String name = rs.getString("name");

                list.add(new Category(id, uid, name));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ★ カテゴリ追加
    public boolean insert(int userID, String name) {
        String sql = "INSERT INTO category (userID, name) VALUES (?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userID);
            ps.setString(2, name);

            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ★ カテゴリ削除（id 指定）
    public boolean delete(int userID, int categoryId) {
        String sql = "DELETE FROM category WHERE userID = ? AND id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userID);
            ps.setInt(2, categoryId);

            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
