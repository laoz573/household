package householdservlet;

import java.io.IOException;

import dao.userID;
import dao.userIDDAO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class LoginFilter
 */
public class LoginFilter extends HttpFilter {
    
    public LoginFilter() {
        super();
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String action = req.getParameter("action");

        if ("logout".equals(action)) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            res.sendRedirect("Login.jsp");
            return;
        }
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        userIDDAO dao = new userIDDAO();
        userID user = dao.findbyUserID(username, password);

        if (user != null) {
            HttpSession session = req.getSession();
            int userID = user.getUserID();
            session.setAttribute("userID", userID);
            res.sendRedirect("YearlyServlet");
        } else {
            res.sendRedirect("Login.jsp"); 
        }
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }
}