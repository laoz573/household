package householdservlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;

import dao.HHD;
import dao.RegisterDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/api/calendar")
public class CalendarApiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        // ★ セッションチェック（ログインしていない場合は 401 を返す）
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"not logged in\"}");
            return;
        }

        int userId = (int) session.getAttribute("userID");

        // ★ パラメータ取得（year, month）
        int year = Optional.ofNullable(request.getParameter("year"))
                .map(Integer::parseInt)
                .orElse(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR));

        int month = Optional.ofNullable(request.getParameter("month"))
                .map(Integer::parseInt)
                .orElse(java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1);

        // ★ DAO 呼び出し
        RegisterDAO registerDAO = new RegisterDAO();
        List<HHD> monthlyRecords = registerDAO.findByYearMonth(year, month, userId);

        // ★ 月の総収入・総支出を計算
        int totalIncome = 0;
        int totalSpending = 0;

        for (HHD record : monthlyRecords) {
            if ("収入".equals(record.getIncome())) {
                totalIncome += record.getPrice();
            } else if ("支出".equals(record.getSpending())) {
                totalSpending += record.getPrice();
            }
        }

        // ★ JSON レスポンス用の Map
        Map<String, Object> result = new HashMap<>();
        result.put("year", year);
        result.put("month", month);
        result.put("totalIncome", totalIncome);
        result.put("totalSpending", totalSpending);
        result.put("records", monthlyRecords);

        // ★ JSON を返す
        Gson gson = new Gson();
        String json = gson.toJson(result);

        response.getWriter().write(json);
    }
}
