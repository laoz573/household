package householdservlet;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import dao.HHD;
import dao.RegisterDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Day;
import model.SetGetCal;

public class CalendarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // 文字化け
    	
        //ユーザーのIDを取得している
    	HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userID");
    	
    	// 変数定義
        //jspから値を受け取る
        String postYear = request.getParameter("Year");
        String postMonth = request.getParameter("Month");
        String lastMonth = request.getParameter("lastmonth");
        String nextMonth = request.getParameter("nextmonth");
        
        //月の総収入と総支出を合計にするための変数
        int monthlyTotalIncome = 0;
        int monthlyTotalSpending = 0;
        
        //jspから年月を受け取った値をint型にしている(Optionalはnull回避のため)
        int cYear = Optional.ofNullable(postYear).map(Integer::parseInt).orElse(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR));
        int cMonth = Optional.ofNullable(postMonth).map(Integer::parseInt).orElse(java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1);

        //ここまでが変数定義　下記から処理が行われている
        
        SetGetCal sgc = new SetGetCal();
        sgc.setYear(cYear);
        sgc.setMonth(cMonth);
        
        //次月と前月の処理を行っている
        if ("次月".equals(nextMonth)) {
            if (cMonth >= 12) {
                sgc.setYear(cYear + 1);
                sgc.setMonth(1);
            } else {
                sgc.setMonth(cMonth + 1);
            }
        } else if ("前月".equals(lastMonth)) {
            if (cMonth <= 1) {
                sgc.setYear(cYear - 1);
                sgc.setMonth(12);
            } else {
                sgc.setMonth(cMonth - 1);
            }
        }
        
        //月の総収入と総支出を計算する
        RegisterDAO registerDAO = new RegisterDAO();
        List<HHD> monthlyRecords = registerDAO.findByYearMonth(sgc.getYear(), sgc.getMonth() , userId);

        //収入と支出でPriceがどちらにいくか決めている
        for (HHD record : monthlyRecords) {
            if ("収入".equals(record.getIncome())) {
                monthlyTotalIncome += record.getPrice();
            } else if ("支出".equals(record.getSpending())) {
                monthlyTotalSpending += record.getPrice();
            }
        }

        request.setAttribute("Days", new Day());
        request.setAttribute("SetGetCal", sgc);
        request.setAttribute("monthlyTotalIncome", monthlyTotalIncome);
        request.setAttribute("monthlyTotalSpending", monthlyTotalSpending);
        request.getRequestDispatcher("Calendar.jsp").forward(request, response);
    }
}