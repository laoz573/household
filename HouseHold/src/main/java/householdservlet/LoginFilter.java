package householdservlet;

import java.io.IOException;

import dao.userID;
import dao.userIDDAO;
import jakarta.servlet.Filter;
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
public class LoginFilter extends HttpFilter implements Filter {
    
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
            // セッションを無効にする
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            // ログインページにリダイレクト
            res.sendRedirect("Login.jsp");
            return;
        }
    	
    	
    	// リクエストのパラメータからユーザーネームとパスワードを取得
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // userIDDAOを使用して認証を行う
        userIDDAO dao = new userIDDAO();
        userID user = dao.findbyUserID(username, password);

        if (user != null) {
            // ユーザーが見つかった場合、セッションにユーザー情報を設定
            HttpSession session = req.getSession();
            int userID = user.getUserID();
            session.setAttribute("userID", userID);

            // YearlyServletにリダイレクト
            res.sendRedirect("YearlyServlet");
        } else {
            // ユーザーが見つからない場合、ログインページにリダイレクト
        	res.sendRedirect("Login.jsp"); // ここには適切なログインページのURLを指定してください
        }
    }
    @Override
	public void init(FilterConfig fConfig) throws ServletException {
    }
}
