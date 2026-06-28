package householdservlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dao.CategoryDAO;
import dao.Category;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/api/category")
public class CategoryApiServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final Gson gson = new Gson();

    // -------------------------
    // GET（カテゴリ一覧）
    // -------------------------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json; charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"not logged in\"}");
            return;
        }

        int userId = (int) session.getAttribute("userID");

        CategoryDAO dao = new CategoryDAO();
        List<Category> list = dao.findAll(userId);

        response.getWriter().write(gson.toJson(list));
    }

    // -------------------------
    // POST（カテゴリ追加）
    // -------------------------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\":false, \"message\":\"not logged in\"}");
            return;
        }

        int userId = (int) session.getAttribute("userID");

        BufferedReader reader = request.getReader();
        JsonObject json = gson.fromJson(reader, JsonObject.class);

        String name = json.get("name").getAsString();

        if (name == null || name.trim().isEmpty()) {
            response.getWriter().write("{\"success\":false, \"message\":\"カテゴリ名が空です\"}");
            return;
        }

        CategoryDAO dao = new CategoryDAO();
        boolean success = dao.insert(userId, name);

        if (!success) {
            response.getWriter().write("{\"success\":false, \"message\":\"追加に失敗しました\"}");
            return;
        }

        response.getWriter().write("{\"success\":true}");
    }

    // -------------------------
    // DELETE（カテゴリ削除）
    // -------------------------
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\":false, \"message\":\"not logged in\"}");
            return;
        }

        int userId = (int) session.getAttribute("userID");

        // URL パラメータから id を取得
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.getWriter().write("{\"success\":false, \"message\":\"id がありません\"}");
            return;
        }

        int categoryId = Integer.parseInt(idParam);

        CategoryDAO dao = new CategoryDAO();
        boolean success = dao.delete(userId, categoryId);

        if (!success) {
            response.getWriter().write("{\"success\":false, \"message\":\"削除に失敗しました\"}");
            return;
        }

        response.getWriter().write("{\"success\":true}");
    }
}
