package householdservlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import model.HHD;
import dao.RegisterDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/api/yearly/category")
public class YearlyCategoryApiServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json; charset=UTF-8");

        // ログインチェック
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"not logged in\"}");
            return;
        }

        int userId = (int) session.getAttribute("userID");

        // パラメータ取得
        int year = Integer.parseInt(request.getParameter("year"));
        int month = Integer.parseInt(request.getParameter("month"));

        RegisterDAO dao = new RegisterDAO();
        List<HHD> monthlyRecords = dao.findByYearMonth(year, month, userId);

        // カテゴリ別集計
        Map<String, Integer> categoryTotals = new HashMap<>();

        for (HHD record : monthlyRecords) {
            if ("支出".equals(record.getSpending())) {
                String category = record.getCategory();
                int price = record.getPrice();

                categoryTotals.put(category, categoryTotals.getOrDefault(category, 0) + price);
            }
        }

        // JSON 返却
        Map<String, Object> result = new HashMap<>();
        result.put("year", year);
        result.put("month", month);
        result.put("categories", categoryTotals);

        response.getWriter().write(gson.toJson(result));
    }
}
