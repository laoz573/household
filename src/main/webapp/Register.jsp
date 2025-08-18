<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="dao.RegisterDAO"%>
<%@ page import="dao.HHD"%>
<%@ page import="java.util.List"%>
<%@ page import="model.SetGetCal"%>

<%
List<HHD> allContents =(List<HHD>)session.getAttribute("allContents");
SetGetCal sgc = (SetGetCal) session.getAttribute("SetGetCal");
int year = sgc.getYear(); // 'sgc'オブジェクトから年を取得して'year'変数に割り当てる
int month = sgc.getMonth(); // 同様に月も取得
int date = sgc.getDate(); // 日も取得
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>登録画面</title>
<link rel="stylesheet" href="./css/Register.css">
</head>
<body>

	<form action="CalendarServlet" method="POST">
		<p class="test">
			<button>カレンダー表示</button>
		</p>
		<input type="hidden" name="Year" value="<%=year %>" /> <input
			type="hidden" name="Month" value="<%=month %>" />
	</form>

	<p class="YMD"><%=year %>年<%=month %>月<%=date %>日
	</p>


	<table class="Content" table align="center" cellspacing="3">
		<tr>
			<th>登録内容</th>
			<th>金額</th>
			<th>支出</th>
			<th>収入</th>
			<th>カテゴリー</th>
			<th>備考</th>
		</tr>

		<tr>
 			<th><input type="text" name="Contents" value="内容を入力" onfocus="clearPlaceholder(this)" onblur="setPlaceholder(this)"></th>
			<th><input type="number" name="Price" value="0"></th>
			<th><input type="radio" id="spending" name="Spending" value="支出"
				onclick="toggleRadio(this);" checked></th>
			<th><input type="radio" id="income" name="Income" value="収入"
				onclick="toggleRadio(this);"></th>
			<th><select name="category">
			<option value="食費">食費</option>
			<option value="外食費">外食費</option>
			<option value="日用品">日用品</option>
			<option value="光熱費">光熱費</option>
			<option value="通信費">通信費</option>
			<option value="サブスク">サブスク</option>
			<option value="その他">その他</option>
			</select></th>
        	<th><input type="text" name="Remarks" value="備考を入力" onfocus="clearPlaceholder(this)" onblur="setPlaceholder(this)"></th>
	</table>

	<p>
	<div class="Register">
		<button type="button" onclick="insertRecord()">登録</button>
	</div>
	</p>


	<div class="Reference">

		<p>
			<select id="searchOption">
				<option value="all">全て</option>
				<option value="spending">支出</option>
				<option value="income">収入</option>
			</select>
			<button onclick="searchTable()">検索</button>
		</p>

		<table id="data-table">
			<tr>
				<th>内容</th>
				<th>金額</th>
				<th>支出</th>
				<th>収入</th>
				<th>カテゴリー</th>
				<th>備考</th>
				<th>操作</th>
			</tr>
			<% for (HHD household : allContents) { %>
			<tr id="row_<%=household.getId()%>">
				<td name=><span><%=household.getContents()%></span></td>
				<td><span><%=household.getPrice()%></span></td>
				<td><span><%=household.getSpending()%></span></td>
				<td><span><%=household.getIncome()%></span></td>
				<td><span><%=household.getRemarks()%></span></td>
				<td><span><%=household.getRemarks()%></span></td>
				<td><button type="button"
						onclick="editRecord(<%=household.getId()%>)">編集</button></td>
				<td><button type="button"
						onclick="deleteRecord(<%=household.getId()%>)">削除</button></td>

			</tr>
			<% } %>
		</table>



		<form action="RegisterServlet" method="POST">
			<p>
				<button>更新</button>
			</p>

			<input type="hidden" name="Year" value="<%=year %>" /> <input
				type="hidden" name="Month" value="<%=month %>" /> <input
				type="hidden" name="Date" value="<%=date %>" />
		</form>
	</div>

	<form action="RegisterServlet" method="POST">
		<p>
		<div class="LastDay">
			<button>前日</button>
			<input type="hidden" name="lastDay" value="前日" />
		</div>
		</p>

		<input type="hidden" name="Year" value="<%=year %>" /> <input
			type="hidden" name="Month" value="<%=month %>" /> <input
			type="hidden" name="Date" value="<%=date %>" />
	</form>

	<form action="RegisterServlet" method="POST">
		<p>
		<div class="NextDay">
			<button>翌日</button>
			<input type="hidden" name="nextDay" value="翌日" />
		</div>
		</p>

		<input type="hidden" name="Year" value="<%=year %>" /> <input
			type="hidden" name="Month" value="<%=month %>" /> <input
			type="hidden" name="Date" value="<%=date %>" />
	</form>

	<script>
	function editRecord(id) {
	    var row = document.getElementById('row_' + id);
	    var cells = row.getElementsByTagName('td');
	    
	  //boolean originalSpending = "支出".equals(someObject.getSpending());と同じようなことをしている
	    var originalSpending = cells[2].innerText === '支出';
	    var originalIncome = cells[3].innerText === '収入';
	    
	    // 内容
	    cells[0].innerHTML = '<input type="text" name="Contents" value="' + cells[0].innerText + '">';
	    // 金額
	    cells[1].innerHTML = '<input type="number" name="Price" value="' + cells[1].innerText + '">';
	    // 支出
	    cells[2].innerHTML = '<input type="radio" id="Espending" name="Spending" value="'+ cells[2].innerText +'" onclick="edittoggleRadio(this);" ' + (originalSpending ? 'checked' : '') + '>';
	    // 収入
	    cells[3].innerHTML = '<input type="radio" id="Eincome" name="Income" value="'+ cells[3].innerText +'" onclick="edittoggleRadio(this);" ' + (originalIncome ? 'checked' : '') + '>';
	    // カテゴリー
	    cells[4].innerHTML = '<select name="Category">' +
	        '<option value="食費"' + (cells[4].innerText === '食費' ? ' selected' : '') + '>食費</option>' +
	        '<option value="外食費"' + (cells[4].innerText === '外食費' ? ' selected' : '') + '>外食費</option>' +
	        '<option value="日用品"' + (cells[4].innerText === '日用品' ? ' selected' : '') + '>日用品</option>' +
	        '<option value="光熱費"' + (cells[4].innerText === '光熱費' ? ' selected' : '') + '>光熱費</option>' +
	        '<option value="通信費"' + (cells[4].innerText === '通信費' ? ' selected' : '') + '>通信費</option>' +
	        '<option value="サブスク"' + (cells[4].innerText === 'サブスク' ? ' selected' : '') + '>サブスク</option>' +
	        '<option value="その他"' + (cells[4].innerText === 'その他' ? ' selected' : '') + '>その他</option>' +
	    '</select>';
		// 備考
	    cells[5].innerHTML = '<input type="text" name="Remarks" value="' + cells[5].innerText + '">';
	    
	    // ボタンのテキストとイベントハンドラを更新
	   var editButton = cells[6].getElementsByTagName('button')[0];
	   var backButton = cells[7].getElementsByTagName('button')[0];
	    editButton.innerText = '保存';
	    backButton.innerText = '編集中止';
	    backButton.onclick = function() { backTd(id); };
	    editButton.onclick = function() { saveRecord(id); };
	}
	

		submitForm = function(action, data) {
			 // フォームの生成
			 var form = document.createElement("form");
			 form.setAttribute("action", action);
			 form.setAttribute("method", "post");
			 form.style.display = "none";
			 document.body.appendChild(form);

			 // パラメーターの設定
			 if(typeof(data) !== "undefined") {
			  for(var paramName in data) {
			   var input = document.createElement("input");
			   input.setAttribute("type", "hidden");
			   input.setAttribute("name", paramName);
			   input.setAttribute("value", data[paramName]);
			   form.appendChild(input);
			  }
			 }
			 // submit
			 form.submit();
			}
		
	function saveRecord(id) {
		console.log(typeof id);
	    var row = document.getElementById('row_' + id);
	    var inputs = row.getElementsByTagName('input');
	    var data = {
	    	"eId": id,
	    	"eContents": row.querySelector('input[name="Contents"]').value,
	    	"ePrice": row.querySelector('input[name="Price"]').value,
	    	"eIncome": row.querySelector('input[name="Income"]').value,
	    	"eSpending" :row.querySelector('input[name="Spending"]').value,
			"eCategory": row.querySelector('select[name="Category"]').value,
	    	"eRemarks": row.querySelector('input[name="Remarks"]').value,
	    	"Year" :<%=year %>,
	    	"Month":<%=month %>,
	    	"Date":<%=date %>,
	    };
		    
		    submitForm("RegisterServlet", data);
		    
		 // フォーム送信後にアラートを表示
	        alert('データ内容を修正しました。\n更新ボタンを押してください。');
	}
	
	
				
			function backTd(id) {
			    var row = document.getElementById('row_' + id);
			    var cells = row.getElementsByTagName('td');
			    
			    for (var i = 0; i < cells.length - 2; i++) {
				var cell = cells[i];
				var input = cell.querySelector('input, select'); // ← input か select を取得
				var value = input ? input.value : cell.innerText; // ← inputがなければテキストを使う
			        cell.innerHTML = value; // セルの内容をテキストに戻す
			    }

			    // ボタンセルを元に戻す
			    var editButtonCell = cells[cells.length - 2];
			    var deleteButtonCell = cells[cells.length - 1];

			    editButtonCell.innerHTML = '<button type="button" onclick="editRecord(' + id + ')">編集</button>';
			    deleteButtonCell.innerHTML = '<button type="button" onclick="deleteRecord(' + id + ')">削除</button>';
			}
			
			
				function deleteRecord(id) {
				    if(confirm('本当に削除しますか？')) {
				        var form = document.createElement('form');
				        form.method = 'post';
				        form.action = 'RegisterServlet';
				        form.innerHTML = '<input type="hidden" name="Id" value="' + id + '">' +
                        '<input type="hidden" name="Year" value="' + <%=year %> + '">' +
                        '<input type="hidden" name="Month" value="' + <%=month %> + '">' +
                        '<input type="hidden" name="Date" value="' + <%=date %> + '">'+
                        '<input type="hidden" name="action" value="delete">'; // ここでアクションを指定;
				        document.body.appendChild(form);
				        form.submit();
				        
				        // フォーム送信後にアラートを表示
				        alert('削除しました。\n更新ボタンを押してください。');
				    }
				}
				
				function insertRecord() {
				    if (confirm('下記の内容で登録しますか？')) {
				        var form = document.createElement('form');
				        form.method = 'post';
				        form.action = 'RegisterServlet';

				        // 元のフォームから値を取得
				        var contents = document.querySelector('[name="Contents"]').value;
				        var price = document.querySelector('[name="Price"]').value;
				        var spending = document.querySelector('[name="Spending"]:checked') ? document.querySelector('[name="Spending"]:checked').value : '';
				        var income = document.querySelector('[name="Income"]:checked') ? document.querySelector('[name="Income"]:checked').value : '';
						var category = document.querySelector('[name="Category"]:checked') ? document.querySelector('[name="Category"]:checked').value : '';
				        var remarks = document.querySelector('[name="Remarks"]').value;

				        // 隠しフィールドに値を設定
				        form.innerHTML = 
				            '<input type="hidden" name="Contents" value="' + contents + '">' +
				            '<input type="hidden" name="Price" value="' + price + '">' +
				            '<input type="hidden" name="Spending" value="' + spending + '">' +
				            '<input type="hidden" name="Income" value="' + income + '">' +
				            '<input type="hidden" name="Remarks" value="' + remarks + '">' +
							'<input type="hidden" name="Category" value="' + category + '">' +
				            '<input type="hidden" name="Year" value="' + <%=year %> + '">' +
				            '<input type="hidden" name="Month" value="' + <%=month %> + '">' +
				            '<input type="hidden" name="Date" value="' + <%=date %> + '">' +
				            '<input type="hidden" name="action" value="insert">'; // ここでアクションを指定;

				        document.body.appendChild(form);
				        form.submit();
				        
				        // フォーム送信後にアラートを表示
				        alert('登録しました。\n更新ボタンを押してください。');
				    }
				}
				
				function toggleRadio(current) {
				    var spending = document.getElementById('spending');
				    var income = document.getElementById('income');

				    if (current.id === 'spending' && current.checked) {
				        income.checked = false;
				    } else if (current.id === 'income' && current.checked) {
				        spending.checked = false;
				    }
				}
				
				function clearPlaceholder(element) {
				    if (element.value == element.defaultValue) {
				        element.value = '';
				    }
				}
				function setPlaceholder(element) {
				    if (element.value === '') {
				        element.value = element.defaultValue;
				    }
				}
				
				function edittoggleRadio(current) {
				    var spending = document.getElementById('Espending');
				    var income = document.getElementById('Eincome');

				    if (current.id === 'Espending' && current.checked) {
				        income.checked = false;
				        income.value = ''; // incomeの値を空文字に設定
				        spending.value='支出';//Spendingの値を"支出"にする
				    } else if (current.id === 'Eincome' && current.checked) {
				        spending.checked = false;
				        spending.value ='';//spendingの値を空文字に設定
				        income.value ='収入';//incomeの値を"収入"にする
				    }
				}
				
				
				function searchTable() {
				    var search = document.getElementById('searchOption').value;
				    var table = document.getElementById('data-table');
				    var tr = table.getElementsByTagName('tr');

				    for (var i = 1; i < tr.length; i++) { // ヘッダー行を除くすべての行をループ
				        var tdSpending = tr[i].getElementsByTagName('td')[2].innerText; // 支出の列
				        var tdIncome = tr[i].getElementsByTagName('td')[3].innerText; // 収入の列

				        if (search === 'all') {
				            tr[i].style.display = ""; // 全ての行を表示
				        } else if (search === 'spending' && tdSpending === '支出') {
				            tr[i].style.display = ""; // 支出のみ表示
				        } else if (search === 'income' && tdIncome === '収入') {
				            tr[i].style.display = ""; // 収入のみ表示
				        } else {
				            tr[i].style.display = "none"; // その他の行を非表示
				        }
				    }
				}
				
				
</script>
</body>
</html>
