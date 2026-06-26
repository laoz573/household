package householdservlet;

import java.io.IOException;
import com.google.gson.Gson;
import dao.userIDDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/user/register")
public class UserRegisterApiServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 入力チェック
        if (username == null || username.isEmpty() ||
            password == null || password.isEmpty()) {

            response.getWriter().write("{\"success\":false, \"message\":\"ユーザー名またはパスワードが空です\"}");
            return;
        }

        userIDDAO dao = new userIDDAO();
        boolean success = dao.insertUserID(username, password);

        if (!success) {
            response.getWriter().write("{\"success\":false, \"message\":\"登録に失敗しました（重複の可能性）\"}");
            return;
        }

        response.getWriter().write("{\"success\":true}");
    }
}
