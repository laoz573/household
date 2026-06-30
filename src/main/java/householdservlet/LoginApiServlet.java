package householdservlet;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.annotation.WebServlet;
import dao.userIDDAO;
import model.userID;

@WebServlet("/login")
public class LoginApiServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        userIDDAO dao = new userIDDAO();
        userID user = dao.findbyUserID(username, password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("userID", user.getUserID());
            response.sendRedirect("Yearly.html");
        } else {
            response.sendRedirect("Login.html?error=1");
        }
    }
}
