package householdservlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import dao.HHD;
import dao.RegisterDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/api/yearly")
public class YearlyApiServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json; charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"not logged in\"}");
            return;
        }

        int userId = (int) session.getAttribute("userID");

        int year = Integer.parseInt(request.getParameter("year"));

        RegisterDAO dao = new RegisterDAO();

        // 年間データ
        List<HHD> yearlyRecords = dao.findByYear(year, userId);

        int yearlyTotalIncome = 0;
        int yearlyTotalSpending = 0;

        for (HHD r : yearlyRecords) {
            if ("収入".equals(r.getIncome())) yearlyTotalIncome += r.getPrice();
            if ("支出".equals(r.getSpending())) yearlyTotalSpending += r.getPrice();
        }

        // 月別データ
        int[] monthlyIncome = new int[12];
        int[] monthlySpending = new int[12];

        for (int m = 1; m <= 12; m++) {
            List<HHD> monthly = dao.findByYearMonth(year, m, userId);

            for (HHD r : monthly) {
                if ("収入".equals(r.getIncome())) monthlyIncome[m - 1] += r.getPrice();
                if ("支出".equals(r.getSpending())) monthlySpending[m - 1] += r.getPrice();
            }
        }

        // JSON 返却
        Map<String, Object> result = new HashMap<>();
        result.put("year", year);
        result.put("yearlyTotalIncome", yearlyTotalIncome);
        result.put("yearlyTotalSpending", yearlyTotalSpending);
        result.put("monthlyTotalIncomes", monthlyIncome);
        result.put("monthlyTotalSpendings", monthlySpending);

        response.getWriter().write(gson.toJson(result));
    }
}