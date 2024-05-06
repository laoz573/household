<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.SetGetCal"%>
<%@ page import="java.util.Arrays"%>

<%
int yearlyTotalIncome = (int) request.getAttribute("yearlyTotalIncome");
int yearlyTotalSpending = (int) request.getAttribute("yearlyTotalSpending");
SetGetCal sgc = (SetGetCal) request.getAttribute("SetGetCal");
int year = sgc.getYear();
int month = sgc.getMonth();
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
	</form>
	<h1>家計簿</h1>
	<h2>年間料金</h2>

			<form action="CalendarServlet" method="POST" >
					<table class ="selectYearMonth">
						<tr>
							<th>年</th>
							<th>年間収入</th>
							<th>年間支出</th>
							<th>月選択</th>
							<th>操作</th>
						</tr>
						<tr>
							<td><select id="yearInput" name="Year">
									<script>
										var currentYear = <%=year%>; // 現在の年を取得
										var startYear = 2023; // 開始年
										var endYear = currentYear + 10; // 終了年（現在の年から10年後）
	
										// 開始年から終了年までの選択肢を生成
										for (var year = startYear; year <= endYear; year++) {
											var selected = year === currentYear ? ' selected': ''; // 現在の年を選択済みにする
											document.write('<option value="' + year + '"' + selected + '>'+ year + '年</option>');
										}
									</script>
							</select></td>
							<td><%=yearlyTotalIncome%></td>
							<td><%=yearlyTotalSpending%></td>
							<td><select id="monthInput" name="Month" required>
									<script>
	       								 var currentMonth = <%=month%>; //現在の月を取得
	
										// 1月から12月までの選択肢を生成
										for (var month = 1; month <= 12; month++) {
											var monthName = [ "1月", "2月", "3月","4月", "5月", "6月", "7月", "8月","9月", "10月", "11月", "12月" ][month - 1];
											var selected = month === currentMonth ? ' selected': ''; // 現在の月を選択済みにする
											document.write('<option value="' + month + '"' + selected + '>'+ monthName+ '</option>');
										}
									</script>
							</select></td>
							<td>
								<button type="submit">表示</button>
							</td>
						</tr>
					</table>
			</form>

			<!-- 年と月を選択して更新するためのフォーム -->
			<form action="YearlyServlet" method="POST" onsubmit="submitYearMonth()" class="update">
				<input type="hidden" id="yearHidden" name="Year" value=""> 
				<input type="hidden" id="monthHidden" name="Month" value="">
				<button class ="update" type="submit">更新</button>
			</form>
			
    		<!-- グラフのサイズを制御するための親要素 -->
    		<!-- グラフを表示するためのキャンバス -->
			<div class="graph-container">
				<canvas id="monthlyFinanceChart"></canvas>
			</div>


			 <!-- Chart.jsのCDNを追加 -->
  			 <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
			
			<form action="YearlyServlet" method="POST">
				<label for="username">ユーザー名:</label>
				<input type="text" name="username"><br>
				<label for="password">パスワード:</label>
				<input type="password" name="password"><br>
				<!-- 新規登録ボタン -->
				<button type="submit" name="action" value="register">新規登録</button>
			</form>
			
			<script>
				// 年の値をテキストフィールドから隠しフィールドにコピーする関数
				function submitYearMonth() {
					// テキストフィールドの値を取得
					var yearValue = document.getElementById('yearInput').value;
					var monthValue = document.getElementById('monthInput').value;
					// 隠しフィールドのvalue属性を更新
					document.getElementById('yearHidden').value = yearValue;
					document.getElementById('monthHidden').value = monthValue;
				}
				
			    // ページのコンテンツが読み込まれた後にグラフを描画する
			    document.addEventListener('DOMContentLoaded', function() {
			    	var monthlyIncomes = JSON.parse('<%= Arrays.toString((int[]) request.getAttribute("monthlyTotalIncomes")) %>');
			    	var monthlySpendings = JSON.parse('<%= Arrays.toString((int[]) request.getAttribute("monthlyTotalSpendings")) %>');
			        
			        var ctx = document.getElementById('monthlyFinanceChart').getContext('2d');
			        var monthlyFinanceChart = new Chart(ctx, {
			        	  type: 'bar', // グラフの種類を棒グラフに指定
			              data: {
			                  // 各月をラベルとして指定
			                  labels: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
			                  datasets: [
			                      {
			                          label: '収入', // 収入のラベル
			                          data: monthlyIncomes, // ここに各月の収入データを配列で指定
			                          backgroundColor: 'rgba(54, 162, 235, 0.2)', // 収入の棒の背景色
			                          borderColor: 'rgba(54, 162, 235, 1)', // 収入の棒の境界色
			                          borderWidth: 1 // 収入の棒の枠線の太さ
			                      },
			                      {
			                          label: '支出', // 支出のラベル
			                          data: monthlySpendings, // ここに各月の支出データを配列で指定
			                          backgroundColor: 'rgba(255, 99, 132, 0.2)', // 支出の棒の背景色
			                          borderColor: 'rgba(255, 99, 132, 1)', // 支出の棒の境界色
			                          borderWidth: 1 // 支出の棒の枠線の太さ
			                      }
			                  ]
			              },
			              options: {
			                  scales: {
			                      x: { // xAxesの設定はここに移動
			                          beginAtZero: true,
			                          barPercentage: 0.4
			                      },
			                      y: { // yAxesの設定はここに移動
			                          beginAtZero: true
			                      }
			                  },
			                  responsive: true, // グラフをレスポンシブにする
			                  maintainAspectRatio: true,
			              }
			          });
			      });
			</script>
</body>
</html>