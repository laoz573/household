<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.Day"%>
<%@ page import="model.SetGetCal"%>
<%@ page import="dao.userID"%>


<% Day day = (Day)request.getAttribute("Days");
int monthlyTotalIncome =(int)request.getAttribute("monthlyTotalIncome");
int monthlyTotalSpending =(int)request.getAttribute("monthlyTotalSpending");
int userId = (int) session.getAttribute("userID");
SetGetCal sgc =(SetGetCal)request.getAttribute("SetGetCal");
int year = sgc.getYear();
int month = sgc.getMonth();
%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>カレンダー</title>
<link rel="stylesheet" href="./css/Calendar.css">
</head>
<body>

	<div class="hamburger-menu" onclick="toggleMenu()">
		☰
		<div class="menu-content" id="dropdownMenu">
			<% for (int i = 1; i <= 12; i++) { %>
			<a href="#" onclick="changeMonth(<%= i %>)"><%= i %>月</a>
			<% } %>
		</div>
	</div>

	<form action="YearlyServlet" method="GET">
		<p>
			<button>年間表示</button>
		</p>
	</form>

	<p><%=year %>年
	</p>
	<p><%=month %>月
	</p>

	<p class="MonthIncome">
		総収入：<%=monthlyTotalIncome %>円
	</p>
	<p class="MonthSpending">
		総支出：<%=monthlyTotalSpending %>円
	</p>

	<table class="Calendar" border="1" table align="center">
		<tr>
			<th>日</th>
			<th>月</th>
			<th>火</th>
			<th>水</th>
			<th>木</th>
			<th>金</th>
			<th>土</th>
		</tr>



		<tr>
			<%= day.Days(year , month , userId) %>
		</tr>


	</table>

	<form action="CalendarServlet" method="POST">
		<p>
		<div class="LastMonth">
			<button type="submit">前月</button>
			<input type="hidden" name="lastmonth" value="前月" />
		</div>
		</p>

		<input type="hidden" name="Year" value="<%=year %>" /> 
		<input type="hidden" name="Month" value="<%=month %>" />

	</form>

	<form action="CalendarServlet" method="POST">
		<p>
		<div class="NextMonth">
			<button type="submit">次月</button>
			<input type="hidden" name="nextmonth" value="次月" />
		</div>
		</p>

		<input type="hidden" name="Year" value="<%=year %>" /> <input
			type="hidden" name="Month" value="<%=month %>" />

	</form>


	<script>
	function dateRecord(date) {
	    if(confirm('移動しますか？')) {
	        var form = document.createElement('form');
	        form.method = 'post';
	        form.action = 'RegisterServlet';
	        form.innerHTML = '<input type="hidden" name="Year" value="' + <%=year %> + '">' +
				            '<input type="hidden" name="Month" value="' + <%=month %> + '">' +
				            '<input type="hidden" name="Date" value="' + date + '">';
	        document.body.appendChild(form);
	        form.submit();
	    }
	}
	
	function toggleMenu() {
	    var menu = document.getElementById("dropdownMenu");
	    if (menu.style.display === "block") {
	        menu.style.display = "none";
	    } else {
	        menu.style.display = "block";
	    }
	}

	function changeMonth(month) {
	    var form = document.createElement('form');
	    form.method = 'post';
	    form.action = 'CalendarServlet';
	    form.innerHTML = '<input type="hidden" name="Year" value="<%=year %>">' +
	                     '<input type="hidden" name="Month" value="' + month + '">';
	    document.body.appendChild(form);
	    form.submit();
	}
	
	window.onload = function() {
	    // ページが読み込まれたときにメニューを閉じる
	    var menu = document.getElementById("dropdownMenu");
	    menu.style.display = "none";
	}
    </script>

</body>
</html>