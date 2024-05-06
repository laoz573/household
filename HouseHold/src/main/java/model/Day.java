package model;

import java.util.Calendar;
import java.util.List;

import dao.HHD;
import dao.RegisterDAO;

public class Day {
    public StringBuilder Days(int year, int month , int userId) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        int daysCount = cal.getActualMaximum(Calendar.DAY_OF_MONTH);//その日が何日まであるか
        int blank = cal.get(Calendar.DAY_OF_WEEK) - 1;

        RegisterDAO registerDAO = new RegisterDAO();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 42; i++) {
            if (i >= daysCount + blank) {
                sb.append("<td></td>");
                if (i % 7 == 6) {
                    sb.append("</tr><tr>");
                }
            } else if (i >= blank) {
                int date = i + 1 - blank;
                List<HHD> dailyRecords = registerDAO.findByYearMonthDate(year, month, date , userId);

                // 日の収入と支出の合計を計算
                int dailyTotalIncome = 0;
                int dailyTotalSpending = 0;
                for (HHD record : dailyRecords) {
                    if ("収入".equals(record.getIncome())) {
                        dailyTotalIncome += record.getPrice();
                    } else if ("支出".equals(record.getSpending())) {
                        dailyTotalSpending += record.getPrice();
                    }
                }

                sb.append("<td onclick=\"dateRecord(").append(date).append(")\">");
                sb.append("<p class=\"day\">").append(date).append("</p>");
                sb.append("<p class=\"Income\">収入:").append(dailyTotalIncome).append("円").append("</p>");
                sb.append("<p class=\"Spending\">支出:").append(dailyTotalSpending).append("円").append("</p>");
                sb.append("</td>");

                if (i % 7 == 6) {
                    sb.append("</tr><tr>");
                }
            } else {
                sb.append("<td></td>");
                if (i % 7 == 6) {
                    sb.append("</tr><tr>");
                }
            }
        }

        return sb;
    }
}