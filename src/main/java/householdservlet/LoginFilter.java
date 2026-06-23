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

@WebFilter("/*")
public class LoginFilter extends HttpFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        // ★ API はログインチェックを通さない
        if (path.startsWith("/api/")) {
            chain.doFilter(request, response);
            return;
        }

        // ★ 静的ファイル（HTML / CSS / JS）は通す
        if (path.endsWith(".html") || path.endsWith(".css") || path.endsWith(".js")) {
            chain.doFilter(request, response);
            return;
        }

        // ★ ログインページとログイン処理は通す
        if (path.endsWith("Login.html") || path.endsWith("login")) {
            chain.doFilter(request, response);
            return;
        }

        // ★ セッションチェック
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("userID") != null) {
            chain.doFilter(request, response);
            return;
        }

        // ★ 未ログイン → ログイン画面へ
        res.sendRedirect("Login.html");
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
