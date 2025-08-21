<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.SetGetCal"%>
<%@ page import="java.util.*"%>

<%
int yearlyTotalIncome = (int) request.getAttribute("yearlyTotalIncome");
int yearlyTotalSpending = (int) request.getAttribute("yearlyTotalSpending");
SetGetCal sgc = (SetGetCal) request.getAttribute("SetGetCal");
int year = sgc.getYear();
int month = sgc.getMonth();

// 月別収支データ
int[] monthlyTotalIncomes = (int[]) request.getAttribute("monthlyTotalIncomes");
int[] monthlyTotalSpendings = (int[]) request.getAttribute("monthlyTotalSpendings");

// カテゴリ別支出データ
int targetMonth = month; // 例：8月
System.out.println("Target Month: " + targetMonth);
Map<String, Integer> categorySpending = ((Map<Integer, Map<String, Integer>>)request.getAttribute("monthlyCategorySpending")).get(targetMonth);

StringBuilder labels = new StringBuilder("[");
StringBuilder data = new StringBuilder("[");

if (categorySpending == null || categorySpending.isEmpty()) {
    labels.append("\"データなし\"");
    data.append("0"); // ダミー値
} else {
    for (Map.Entry<String, Integer> entry : categorySpending.entrySet()) {
        labels.append("\"").append(entry.getKey()).append("\",");
        data.append(entry.getValue()).append(",");
    }
    labels.setLength(labels.length() - 1); // 最後のカンマ削除
    data.setLength(data.length() - 1);
}

labels.append("]");
data.append("]");
%>

<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>家計簿</title>
  <link rel="stylesheet" href="./css/Yearly.css">
</head>
<body>
  <form action="LoginFilter" method="POST">
    <input type="hidden" name="action" value="logout">
    <button type="submit">ログイン画面</button>
  </form>

  <h1>家計簿</h1>
  <h2>年間料金</h2>

  <form action="CalendarServlet" method="POST">
    <table class="selectYearMonth">
      <tr>
        <th>年</th>
        <th>年間収入</th>
        <th>年間支出</th>
        <th>月選択</th>
        <th>操作</th>
      </tr>
      <tr>
        <td>
          <select id="yearInput" name="Year">
            <script>
              var currentYear = <%=year%>;
              for (var y = 2023; y <= currentYear + 10; y++) {
                var selected = y === currentYear ? ' selected' : '';
                document.write('<option value="' + y + '"' + selected + '>' + y + '年</option>');
              }
            </script>
          </select>
        </td>
        <td><%=yearlyTotalIncome%></td>
        <td><%=yearlyTotalSpending%></td>
        <td>
          <select id="monthInput" name="Month" required>
            <script>
              var currentMonth = <%=month%>;
              var monthNames = ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"];
              for (var m = 1; m <= 12; m++) {
                var selected = m === currentMonth ? ' selected' : '';
                document.write('<option value="' + m + '"' + selected + '>' + monthNames[m - 1] + '</option>');
              }
            </script>
          </select>
        </td>
        <td><button type="submit">表示</button></td>
      </tr>
    </table>
  </form>

  <form action="YearlyServlet" method="POST" onsubmit="submitYearMonth()" class="update">
    <input type="hidden" id="yearHidden" name="Year" value="">
    <input type="hidden" id="monthHidden" name="Month" value="">
    <button class="update" type="submit">更新</button>
  </form>

	<script>
      function submitYearMonth() {
      document.getElementById('yearHidden').value = document.getElementById('yearInput').value;
      document.getElementById('monthHidden').value = document.getElementById('monthInput').value;
    }
	</script>

  <!-- グラフ表示領域 -->
  <div class="graph-container">
    <canvas id="monthlyFinanceChart"></canvas>
  </div>

  <div class="graph-container">
    <canvas id="categoryPieChart"></canvas>
  </div>

  <!-- Chart.js CDN -->
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2"></script>

  <script>

    document.addEventListener('DOMContentLoaded', function () {
      // 棒グラフ
      const monthlyIncomes = <%= Arrays.toString(monthlyTotalIncomes) %>;
      const monthlySpendings = <%= Arrays.toString(monthlyTotalSpendings) %>;
      const ctxBar = document.getElementById('monthlyFinanceChart').getContext('2d');
      new Chart(ctxBar, {
        type: 'bar',
        data: {
          labels: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
          datasets: [
            {
              label: '収入',
              data: monthlyIncomes,
              backgroundColor: 'rgba(54, 162, 235, 0.2)',
              borderColor: 'rgba(54, 162, 235, 1)',
              borderWidth: 1
            },
            {
              label: '支出',
              data: monthlySpendings,
              backgroundColor: 'rgba(255, 99, 132, 0.2)',
              borderColor: 'rgba(255, 99, 132, 1)',
              borderWidth: 1
            }
          ]
        },
        options: {
          responsive: true,
          scales: {
            x: { beginAtZero: true, barPercentage: 0.4 },
            y: { beginAtZero: true }
          }
        }
      });

      // 円グラフ（カテゴリ別支出）
const categoryLabels = <%= labels.toString() %>;
const categoryData = <%= data.toString() %>;
const ctxPie = document.getElementById('categoryPieChart').getContext('2d');
new Chart(ctxPie, {
  type: 'doughnut',
  data: {
    labels: categoryLabels,
    datasets: [{
      label: 'カテゴリ別支出',
      data: categoryData,
      backgroundColor: [
        'rgba(255, 99, 132, 0.6)',
        'rgba(54, 162, 235, 0.6)',
        'rgba(255, 206, 86, 0.6)',
        'rgba(75, 192, 192, 0.6)',
        'rgba(153, 102, 255, 0.6)'
      ],
      borderColor: 'rgba(255, 255, 255, 1)',
      borderWidth: 1
    }]
  },
  options: {
    responsive: true,
    plugins: {
      legend: { position: 'bottom' },
      title: { display: true, text: 'カテゴリ別支出' },
      datalabels: {
        color: '#000',
        formatter: function(value, context) {
          const label = context.chart.data.labels[context.dataIndex];
          return label + '\n¥' + value.toLocaleString();
        },
        font: {
          weight: 'bold',
          size: 12
        }
      }
    }
  },
  plugins: [ChartDataLabels]
});
    });
  </script>

  <form action="YearlyServlet" method="POST">
    <label for="username">ユーザー名:</label>
    <input type="text" name="username"><br>
    <label for="password">パスワード:</label>
    <input type="password" name="password"><br>
    <button type="submit" name="action" value="register">新規登録</button>
  </form>
</body>
</html>