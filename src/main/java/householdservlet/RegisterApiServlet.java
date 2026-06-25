package householdservlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

import dao.HHD;
import dao.RegisterDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/api/record")
public class RegisterApiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final Gson gson = new Gson();

    // ★ フロントから受け取る JSON 用 DTO
    private static class RecordRequest {
        int id;         // 更新・削除時に使用
        int year;
        int month;
        int date;
        String contents;
        int price;
        String spending;
        String income;
        String category;
        String remarks;
    }

    // -------------------------
    // GET（指定日のデータ取得）
    // -------------------------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");  // ★これが必須！
        response.setContentType("application/json; charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"not logged in\"}");
            return;
        }

        int userId = (int) session.getAttribute("userID");

        int year = Integer.parseInt(request.getParameter("year"));
        int month = Integer.parseInt(request.getParameter("month"));
        int date = Integer.parseInt(request.getParameter("date"));

        request.setCharacterEncoding("UTF-8");
        RegisterDAO dao = new RegisterDAO();
        List<HHD> list = dao.findByYearMonthDate(year, month, date, userId);

        response.getWriter().write(gson.toJson(list));
    }

    // -------------------------
    // POST（新規登録：insertByContent）
    // -------------------------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");  // ★これが必須！
        response.setContentType("application/json; charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"not logged in\"}");
            return;
        }

        int userId = (int) session.getAttribute("userID");

        BufferedReader reader = request.getReader();
        RecordRequest req = gson.fromJson(reader, RecordRequest.class);

        try {
            // RegisterServlet と同じ日付生成ロジック
            String sYMD = req.year + "/" + req.month + "/" + req.date;
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date parsedDate = sdFormat.parse(sYMD);
            java.sql.Date csqlDate = new java.sql.Date(parsedDate.getTime());

            // Id は 0（新規）、他は RegisterServlet と同じ並び
            HHD ihhd = new HHD(
                    0,
                    csqlDate,
                    req.contents,
                    req.price,
                    req.spending,
                    req.income,
                    req.category,
                    req.remarks,
                    userId
            );

            RegisterDAO dao = new RegisterDAO();
            dao.insertByContent(ihhd);

            response.getWriter().write("{\"status\":\"ok\"}");

        } catch (ParseException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"invalid date\"}");
        }
    }

    // -------------------------
    // PUT（更新：updateByContent）
    // -------------------------
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");  // ★これが必須！
        response.setContentType("application/json; charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"not logged in\"}");
            return;
        }

        int userId = (int) session.getAttribute("userID");

        BufferedReader reader = request.getReader();
        RecordRequest req = gson.fromJson(reader, RecordRequest.class);

        try {
            String sYMD = req.year + "/" + req.month + "/" + req.date;
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date parsedDate = sdFormat.parse(sYMD);
            java.sql.Date csqlDate = new java.sql.Date(parsedDate.getTime());

            // RegisterServlet の ehhd と同じイメージ
            HHD ehhd = new HHD(
                    req.id,
                    csqlDate,
                    req.contents,
                    req.price,
                    req.spending,
                    req.income,
                    req.category,
                    req.remarks,
                    userId
            );

            RegisterDAO dao = new RegisterDAO();
            dao.updateByContent(ehhd);

            response.getWriter().write("{\"status\":\"updated\"}");

        } catch (ParseException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"invalid date\"}");
        }
    }

    // -------------------------
    // DELETE（削除：deleteByContent）
    // -------------------------
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"not logged in\"}");
            return;
        }

        int userId = (int) session.getAttribute("userID");

        // クエリパラメータから id / year / month / date を受け取る想定
        int id = Integer.parseInt(request.getParameter("id"));
        int year = Integer.parseInt(request.getParameter("year"));
        int month = Integer.parseInt(request.getParameter("month"));
        int date = Integer.parseInt(request.getParameter("date"));

        try {
            String sYMD = year + "/" + month + "/" + date;
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date parsedDate = sdFormat.parse(sYMD);
            java.sql.Date csqlDate = new java.sql.Date(parsedDate.getTime());

            // 他の項目はダミーでも OK（DAO 側が id / userId / date などで消す前提）
            HHD dhhd = new HHD(
                    id,
                    csqlDate,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    userId
            );

            RegisterDAO dao = new RegisterDAO();
            dao.deleteByContent(dhhd);

            response.getWriter().write("{\"status\":\"deleted\"}");

        } catch (ParseException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"invalid date\"}");
        }
    }
}
