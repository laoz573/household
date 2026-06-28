// package householdservlet;

// import java.io.BufferedReader;
// import java.io.IOException;

// import com.google.gson.Gson;
// import com.google.gson.JsonObject;

// import dao.CategoryDAO;
// import jakarta.servlet.annotation.WebServlet;
// import jakarta.servlet.http.HttpServlet;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import jakarta.servlet.http.HttpSession;

// @WebServlet("/api/category/add")
// public class CategoryAddApiServlet extends HttpServlet {

//     private static final long serialVersionUID = 1L;
//     private final Gson gson = new Gson();

//     @Override
//     protected void doPost(HttpServletRequest request, HttpServletResponse response)
//             throws IOException {
//         request.setCharacterEncoding("UTF-8");
//         response.setContentType("application/json; charset=UTF-8");

//         HttpSession session = request.getSession(false);
//         if (session == null || session.getAttribute("userID") == null) {
//             response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//             response.getWriter().write("{\"success\":false, \"message\":\"not logged in\"}");
//             return;
//         }

//         int userId = (int) session.getAttribute("userID");

//         // JSON を読み取る
//         BufferedReader reader = request.getReader();
//         JsonObject json = gson.fromJson(reader, JsonObject.class);

//         String name = json.get("name").getAsString();

//         if (name == null || name.trim().isEmpty()) {
//             response.getWriter().write("{\"success\":false, \"message\":\"カテゴリ名が空です\"}");
//             return;
//         }

//         CategoryDAO dao = new CategoryDAO();
//         boolean success = dao.insert(userId, name);

//         if (!success) {
//             response.getWriter().write("{\"success\":false, \"message\":\"追加に失敗しました\"}");
//             return;
//         }

//         response.getWriter().write("{\"success\":true}");
//     }
// }
