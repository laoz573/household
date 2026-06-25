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

        // ★ セッションチェック
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

        // ★ パラメータ取得（date）→ これがある場合は日別モード
        String dateParam = request.getParameter("date");

        RegisterDAO registerDAO = new RegisterDAO();
        Gson gson = new Gson();

        // ★ 日別モード（昔の Day.java と同じ動作）
        if (dateParam != null) {
            int date = Integer.parseInt(dateParam);

            // DAO は触らず、既存の findByYearMonthDate を使う
            List<HHD> dailyRecords = registerDAO.findByYearMonthDate(year, month, date, userId);

            response.getWriter().write(gson.toJson(dailyRecords));
            return;
        }

        // ★ 月別モード（今まで通り）
        List<HHD> monthlyRecords = registerDAO.findByYearMonth(year, month, userId);

        int totalIncome = 0;
        int totalSpending = 0;

        for (HHD record : monthlyRecords) {
            if ("収入".equals(record.getIncome())) {
                totalIncome += record.getPrice();
            } else if ("支出".equals(record.getSpending())) {
                totalSpending += record.getPrice();
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("year", year);
        result.put("month", month);
        result.put("totalIncome", totalIncome);
        result.put("totalSpending", totalSpending);
        result.put("records", monthlyRecords);

        response.getWriter().write(gson.toJson(result));
    }
}
