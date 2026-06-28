// package householdservlet;

// import java.io.IOException;
// import java.util.List;

// import com.google.gson.Gson;

// import dao.CategoryDAO;
// import jakarta.servlet.annotation.WebServlet;
// import jakarta.servlet.http.HttpServlet;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import jakarta.servlet.http.HttpSession;
// import dao.Category;

// @WebServlet("/api/category")
// public class CategoryListApiServlet extends HttpServlet {

//     private static final long serialVersionUID = 1L;
//     private final Gson gson = new Gson();

//     @Override
//     protected void doGet(HttpServletRequest request, HttpServletResponse response)
//             throws IOException {

//         response.setContentType("application/json; charset=UTF-8");

//         HttpSession session = request.getSession(false);
//         if (session == null || session.getAttribute("userID") == null) {
//             response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//             response.getWriter().write("{\"error\":\"not logged in\"}");
//             return;
//         }

//         int userId = (int) session.getAttribute("userID");

//         CategoryDAO dao = new CategoryDAO();
//         List<Category> list = dao.findAll(userId);

//         response.getWriter().write(gson.toJson(list));
//     }
// }
