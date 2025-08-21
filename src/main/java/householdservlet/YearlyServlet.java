package householdservlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import dao.HHD;
import dao.RegisterDAO;
import dao.userIDDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.SetGetCal;

/**
 * Servlet implementation class YearlyServlet
 */
public class YearlyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public YearlyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {	

        // セッションからユーザーIDを取得
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userID");

        // 変数を定義
        int cYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        int cMonth = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
        
        int yearlyTotalIncome = 0;
        int yearlyTotalSpending = 0;
        
        int[] monthlyTotalIncomes = new int[12];
        int[] monthlyTotalSpendings = new int[12];
        
        SetGetCal sgc = new SetGetCal();
        sgc.setYear(cYear);
        sgc.setMonth(cMonth+1);
        
        

        RegisterDAO registerDAO = new RegisterDAO();
        List<HHD> yearlyRecords = registerDAO.findByYear(sgc.getYear() , userId);

        
        for (HHD record : yearlyRecords) {
            if ("収入".equals(record.getIncome())) {
                yearlyTotalIncome += record.getPrice();
            } else if ("支出".equals(record.getSpending())) {
                yearlyTotalSpending += record.getPrice();
            }
        }


        for (int month = 1; month <= 12; month++) {
            List<HHD> monthlyRecords = registerDAO.findByYearMonth(sgc.getYear(), month , userId);
            int monthlyTotalIncome = 0;
            int monthlyTotalSpending = 0;

            for (HHD record : monthlyRecords) {
                if ("収入".equals(record.getIncome())) {
                    monthlyTotalIncome += record.getPrice();
                } else if ("支出".equals(record.getSpending())) {
                    monthlyTotalSpending += record.getPrice();
                }
            }

            monthlyTotalIncomes[month - 1] = monthlyTotalIncome;
            monthlyTotalSpendings[month - 1] = monthlyTotalSpending;
        }

	// 月ごとのカテゴリ別支出を計算
	Map<Integer, Map<String, Integer>> monthlyCategorySpending = new HashMap<>();
	for (int month = 1; month <= 12; month++) {
		List<HHD> monthlyRecords = registerDAO.findByYearMonth(sgc.getYear(), month, userId);

		Map<String, Integer> categoryTotals = new HashMap<>();

		for (HHD record : monthlyRecords) {
			if ("支出".equals(record.getSpending())) {
				String category = record.getCategory();
				int price = record.getPrice();

				categoryTotals.put(category, categoryTotals.getOrDefault(category, 0) + price);
			}
		}
		monthlyCategorySpending.put(month, categoryTotals);
	}

        request.setAttribute("monthlyTotalIncomes", monthlyTotalIncomes);
        request.setAttribute("monthlyTotalSpendings", monthlyTotalSpendings);
        request.setAttribute("yearlyTotalIncome", yearlyTotalIncome);
        request.setAttribute("yearlyTotalSpending", yearlyTotalSpending);
		request.setAttribute("monthlyCategorySpending", monthlyCategorySpending);
        request.setAttribute("SetGetCal", sgc);
		//フォワード先の指定
		RequestDispatcher dispatcher =  request.getRequestDispatcher("Yearly.jsp");
		//フォワードの実行
		dispatcher.forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

			request.setCharacterEncoding("UTF-8"); // 文字化け対策
			
	      	String postYear = request.getParameter("Year");
	      	String postmonth = request.getParameter("Month");
	      	
	        // デフォルト値を設定
	        int cYear = Optional.ofNullable(postYear).map(Integer::parseInt).orElse(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR));
	        int cMonth = Optional.ofNullable(postmonth).map(Integer::parseInt).orElse(java.util.Calendar.getInstance().get(java.util.Calendar.MONTH));
	        
	        SetGetCal sgc = new SetGetCal();
	        sgc.setYear(cYear);
	        sgc.setMonth(cMonth);
	        
	        // セッションからユーザーIDを取得
	        HttpSession session = request.getSession();
	        int userId = (int) session.getAttribute("userID");

	        RegisterDAO registerDAO = new RegisterDAO();
	        List<HHD> yearlyRecords = registerDAO.findByYear(sgc.getYear() , userId);
	        int yearlyTotalIncome = 0;
	        int yearlyTotalSpending = 0;

	        for (HHD record : yearlyRecords) {
	            if ("収入".equals(record.getIncome())) {
	                yearlyTotalIncome += record.getPrice();
	            } else if ("支出".equals(record.getSpending())) {
	                yearlyTotalSpending += record.getPrice();
	            }
	        }
	        
	        int[] monthlyTotalIncomes = new int[12];
	        int[] monthlyTotalSpendings = new int[12];

	        for (int month = 1; month <= 12; month++) {
	            List<HHD> monthlyRecords = registerDAO.findByYearMonth(cYear, month , userId);
	            int monthlyTotalIncome = 0;
	            int monthlyTotalSpending = 0;

	            for (HHD record : monthlyRecords) {
	                if ("収入".equals(record.getIncome())) {
	                    monthlyTotalIncome += record.getPrice();
	                } else if ("支出".equals(record.getSpending())) {
	                    monthlyTotalSpending += record.getPrice();
	                }
	            }

	            monthlyTotalIncomes[month - 1] = monthlyTotalIncome;
	            monthlyTotalSpendings[month - 1] = monthlyTotalSpending;
	        }

	// 月ごとのカテゴリ別支出を計算
	Map<Integer, Map<String, Integer>> monthlyCategorySpending = new HashMap<>();
	for (int month = 1; month <= 12; month++) {
		List<HHD> monthlyRecords = registerDAO.findByYearMonth(sgc.getYear(), month, userId);

		Map<String, Integer> categoryTotals = new HashMap<>();

		for (HHD record : monthlyRecords) {
			if ("支出".equals(record.getSpending())) {
				String category = record.getCategory();
				int price = record.getPrice();

				categoryTotals.put(category, categoryTotals.getOrDefault(category, 0) + price);
			}
		}
		monthlyCategorySpending.put(month, categoryTotals);
	}

	String action = request.getParameter("action");

		if ("register".equals(action)) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			if (username == null || username.trim().isEmpty() ||
				password == null || password.trim().isEmpty()) {
				request.setAttribute("error", "ユーザー名またはパスワードが空です");
				RequestDispatcher dispatcher = request.getRequestDispatcher("Register.jsp");
				dispatcher.forward(request, response);
				return;
			}

			userIDDAO dao = new userIDDAO();
			boolean success = dao.insertUserID(username, password);

			if (!success) {
				request.setAttribute("error", "登録に失敗しました。ユーザー名が既に存在している可能性があります。");
				RequestDispatcher dispatcher = request.getRequestDispatcher("Register.jsp");
				dispatcher.forward(request, response);
				return;
			}

			// 登録成功
			response.sendRedirect("YearlyServlet");
			return;
		}

	        request.setAttribute("monthlyTotalIncomes", monthlyTotalIncomes);
	        request.setAttribute("monthlyTotalSpendings", monthlyTotalSpendings);
	        request.setAttribute("yearlyTotalIncome", yearlyTotalIncome);
	        request.setAttribute("yearlyTotalSpending", yearlyTotalSpending);
			request.setAttribute("monthlyCategorySpending", monthlyCategorySpending);
	        request.setAttribute("SetGetCal", sgc);
	        RequestDispatcher dispatcher = request.getRequestDispatcher("Yearly.jsp");
	        dispatcher.forward(request, response);
	}
}
	