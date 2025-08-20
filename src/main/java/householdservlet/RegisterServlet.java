package householdservlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import dao.HHD;
import dao.RegisterDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.SetGetCal;

/**
 * Servlet implementation class RegisterServlet
 */
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8"); // 文字化け対策
		
        // セッションからユーザーIDを取得
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userID");

		//変数定義

		//Databaseの項目取得のため(insert文とdelete文のため)
		int Id = 0;
		String Contents = null;
		int Price = 0;
		String Spending = null;
		String Income = null;
		String Category = null; 
		String Remarks = null;

		//編集した後の項目取得のため(update文のため)
		int eId = 0;
		String eContents = null;
		int ePrice = 0;
		String eSpending = null;
		String eIncome = null;
		String eCategory = null;
		String eRemarks = null;
        
        //insert文のための変数
		Id = Optional.ofNullable(request.getParameter("Id")).map(Integer::parseInt).orElse(0);
		Contents = request.getParameter("Contents");
		Price = Optional.ofNullable(request.getParameter("Price")).map(Integer::parseInt).orElse(0);
		Spending = request.getParameter("Spending");
		Income = request.getParameter("Income");
		Category = request.getParameter("Category"); // カテゴリの取得
		Remarks = request.getParameter("Remarks");

		//update文のための変数(登録されている内容を編集したときにデータを上書きするために必要)
		eId = Optional.ofNullable(request.getParameter("eId")).map(Integer::parseInt).orElse(0);
		eContents = request.getParameter("eContents");
		ePrice = Optional.ofNullable(request.getParameter("ePrice")).map(Integer::parseInt).orElse(0);
		eSpending = request.getParameter("eSpending");
		eIncome = request.getParameter("eIncome");
		eCategory = request.getParameter("eCategory"); // カテゴリの取得
		eRemarks = request.getParameter("eRemarks");
        
		//Calendar.jspおよびRegister.jspからdoPostで送られてきた年月日のSetterおよびGetter
		SetGetCal sgc = new SetGetCal();
		//カレンダーからの日付取得のため
		int year = 0;
		int month = 0;
		int date = 0;
		year = Optional.ofNullable(request.getParameter("Year")).map(Integer::parseInt).orElse(0);
		month = Optional.ofNullable(request.getParameter("Month")).map(Integer::parseInt).orElse(0);
		date = Optional.ofNullable(request.getParameter("Date")).map(Integer::parseInt).orElse(0);
		
		//翌日・前日のための変数
        String lastDay = request.getParameter("lastDay");
        String nextDay = request.getParameter("nextDay");
        
		//ここまでが変数定義で下記から様々な処理が行われている。
		
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, date);  // Calendar.MONTH は0から始まるので1を引く(0←1月になるので、0になるようにしている)

		// 翌日の処理
		if ("翌日".equals(nextDay)) {
		    cal.add(Calendar.DAY_OF_MONTH, 1); // 日付を1日進める
		} 
		// 前日の処理
		else if ("前日".equals(lastDay)) {
		    cal.add(Calendar.DAY_OF_MONTH, -1); // 日付を1日戻す
		}

		// 更新後の年、月、日をSetGetCalオブジェクトに設定
		sgc.setYear(cal.get(Calendar.YEAR));
		sgc.setMonth(cal.get(Calendar.MONTH) + 1);  // Calendar.MONTH は0が1月なので1を足す(0になっているとき、1と表示したいため＋1している)
		sgc.setDate(cal.get(Calendar.DAY_OF_MONTH));

		//日付をget(後のDabase検索のために取得している)
		int getYear = sgc.getYear();
		int getMonth = sgc.getMonth();
		int getDate = sgc.getDate();

		//上記の日付を用いてデータベースを検索している
		RegisterDAO registerDAO = new RegisterDAO();
		List<HHD> allContents = registerDAO.findByYearMonthDate(getYear, getMonth, getDate , userId); // 特定の日付のデータを取得している


		try {
			//上記の日付を文字列に変換（データベースはdate型でないといけないが、data型にするためには文字列型でないとダメなため)
			String strYear = Integer.toString(getYear);
			String strMonth = Integer.toString(getMonth);
			String strDate = Integer.toString(getDate);

			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
			String sYMD = strYear + "/" + strMonth + "/" + strDate;
			Date parsedDate = sdFormat.parse(sYMD); // ユーザーから入力された日付をdate型に変換(parse :java.util.Date オブジェクトに変換する場合に使用される)
			java.sql.Date csqlDate = new java.sql.Date(parsedDate.getTime());//java.util.Date型からjava.sql.Date型に変換している(getTime()がある理由は、java.sql.Date型の引数として必要であるため、結局時間はリセットされている)
			
			//下記の変数はinsertメソッドとdeleteの引数とするため(insertのi、hhdはHHDから),deleteはおまけで使えると分かったから、使っただけ
			HHD ihhd = new HHD(Id, csqlDate, Contents, Price, Spending, Income,  Category, Remarks, userId);
			
			//下記の変数はupdateの引数とするため(editのe、hhdはHHDから)
			HHD ehhd = new HHD(eId, csqlDate, eContents, ePrice, eSpending, eIncome, eCategory, eRemarks, userId);
			
			//actionによってinsertメソッドかupdateメソッドを判別している（これを書かないと編集したときに登録されることなったため)
			String action = request.getParameter("action");
			if ("insert".equals(action)) {
				
				registerDAO.insertByContent(ihhd);
				//リダイレクトしないと登録内容がいつまでも残って、更新ボタンを押したときに何回も登録されたことになったから
				response.sendRedirect("Register.jsp");
				return; // リダイレクト後は処理を中断
			} else if("delete".equals(action)) {
				registerDAO.deleteByContent(ihhd);
			} else {
				registerDAO.updateByContent(ehhd);
			}
			
			request.getSession().setAttribute("allContents", allContents);
			request.getSession().setAttribute("SetGetCal", sgc);
			request.getRequestDispatcher("Register.jsp").forward(request, response);
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
