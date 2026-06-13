package householdservlet;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginFilter extends HttpFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        // ★ API はフィルターを通さない
        if (path.startsWith("/HouseHold/api/")) {
            chain.doFilter(request, response);
            return;
        }

        // ★ ログインページとログイン処理は除外
        if (path.endsWith("Login.jsp") || path.endsWith("LoginServlet")) {
            chain.doFilter(request, response);
            return;
        }

        // ★ セッションチェック
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("userID") != null) {
            chain.doFilter(request, response);
            return;
        }

        // ★ 未ログイン → ログインページへ
        res.sendRedirect("Login.jsp");
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
