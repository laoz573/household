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
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>登録画面</title>
<link rel="stylesheet" href="./css/Register.css">
</head>
<body>
<div id="container">
		<form action="CalendarServlet" method="POST">
			<p><button>カレンダー表示</button></p>
			<input type="hidden" name="Year" value="<%=year %>" /> 
			<input type="hidden" name="Month" value="<%=month %>" />
		</form>

		<p class="YMD"><%=year %>年<%=month %>月<%=date %>日
		</p>


			<div class="form-section">
				<div class="form-row">
					<label for="Contents">登録内容</label>
					<input type="text" name="Contents" id="Contents" value="内容を入力" onfocus="clearPlaceholder(this)"  onblur="setPlaceholder(this)">
				</div>

				<div class="form-row">
					<label for="Price">金額</label>
					<input type="number" name="Price" id="Price" value="0" onfocus="clearPlaceholder(this)" onblur="setPlaceholder(this)">
				</div>

				<div class="form-row">
					<label>支出収入</label>
					<div class="radio-group">
					<label>
					<input type="radio" id="spending" name="Spending" value="支出"
					onclick="toggleRadio(this);" checked> 支出
					</label>
					
					<label>
					<input type="radio" id="income" name="Income" value="収入"
					onclick="toggleRadio(this);"> 収入
					</label>
					</div>
				</div>

				<div class="form-row">
					<label for="category">カテゴリー</label>
					<select name="category" id="category">
						<option value="食費">食費</option>
						<option value="外食費">外食費</option>
						<option value="日用品">日用品</option>
						<option value="光熱費">光熱費</option>
						<option value="通信費">通信費</option>
						<option value="サブスク">サブスク</option>
						<option value="その他">その他</option>
					</select>
				</div>

				<div class="form-row">
					<label for="Remarks">備考</label>
					<input type="text" name="Remarks" id="Remarks" value="">
				</div>

				<div class="Register">
					<button type="button" onclick="insertRecord()">登録</button>
				</div>
			</div>

		<div class="Reference">
				<select id="searchOption">
					<option value="all">全て</option>
					<option value="spending">支出</option>
					<option value="income">収入</option>
				</select>
				<button onclick="searchTable()">検索</button>

				<table id="data-table">
						<% for (HHD household : allContents) { %>
						<div class="record-card" id="row_<%=household.getId()%>">
							<div><%=household.getContents()%></div>
							<div><%=household.getPrice()%></div>
							<div><%=household.getSpending()%></div>
							<div><%=household.getIncome()%></div>
							<div><%=household.getCategory()%></div>
							<div><%=household.getRemarks()%></div>
							<div class="actions">
							<button type="button" onclick="editRecord(<%=household.getId()%>)">編集</button>
							<button type="button" onclick="deleteRecord(<%=household.getId()%>)">削除</button>
							</div>
						</div>
						<% } %>
				</table>

			<form action="RegisterServlet" method="POST">
					<button>更新</button>
				<input type="hidden" name="Year" value="<%=year %>" /> <input
					type="hidden" name="Month" value="<%=month %>" /> <input
					type="hidden" name="Date" value="<%=date %>" />
			</form>
		</div>

	<div class="DayNavigation">
	<form action="RegisterServlet" method="POST">
		<button type="submit">前日</button>
		<input type="hidden" name="lastDay" value="前日" />
		<input type="hidden" name="Year" value="<%=year %>" />
		<input type="hidden" name="Month" value="<%=month %>" />
		<input type="hidden" name="Date" value="<%=date %>" />
	</form>

	<form action="RegisterServlet" method="POST">
		<button type="submit">翌日</button>
		<input type="hidden" name="nextDay" value="翌日" />
		<input type="hidden" name="Year" value="<%=year %>" />
		<input type="hidden" name="Month" value="<%=month %>" />
		<input type="hidden" name="Date" value="<%=date %>" />
	</form>
	</div>

		<script>
		function editRecord(id) {
		const row = document.getElementById('row_' + id);
		const divs = row.querySelectorAll('div:not(.actions)');
			
		//boolean originalSpending = "支出".equals(someObject.getSpending());と同じようなことをしている
			var originalSpending = divs[2].innerText === '支出';
			var originalIncome = divs[3].innerText === '収入';
			
			// 内容
			divs[0].innerHTML = '<input type="text" name="Contents" value="' + divs[0].innerText + '">';
			// 金額
			divs[1].innerHTML = '<input type="number" name="Price" value="' + divs[1].innerText + '">';
			// 支出
			divs[2].innerHTML = '<input type="radio" id="Espending" name="Spending" value="'+ divs[2].innerText +'" onclick="edittoggleRadio(this);" ' + (originalSpending ? 'checked' : '') + '>支出';
			// 収入
			divs[3].innerHTML = '<input type="radio" id="Eincome" name="Income" value="'+ divs[3].innerText +'" onclick="edittoggleRadio(this);" ' + (originalIncome ? 'checked' : '') + '>収入';
			// カテゴリー
			divs[4].innerHTML = '<select name="Category">' +
				'<option value="食費"' + (divs[4].innerText === '食費' ? ' selected' : '') + '>食費</option>' +
				'<option value="外食費"' + (divs[4].innerText === '外食費' ? ' selected' : '') + '>外食費</option>' +
				'<option value="日用品"' + (divs[4].innerText === '日用品' ? ' selected' : '') + '>日用品</option>' +
				'<option value="光熱費"' + (divs[4].innerText === '光熱費' ? ' selected' : '') + '>光熱費</option>' +
				'<option value="通信費"' + (divs[4].innerText === '通信費' ? ' selected' : '') + '>通信費</option>' +
				'<option value="サブスク"' + (divs[4].innerText === 'サブスク' ? ' selected' : '') + '>サブスク</option>' +
				'<option value="その他"' + (divs[4].innerText === 'その他' ? ' selected' : '') + '>その他</option>' +
			'</select>';
			// 備考
			divs[5].innerHTML = '<input type="text" name="Remarks" value="' + divs[5].innerText + '">';
			
			// ボタンのテキストとイベントハンドラを更新
			const buttons = row.querySelectorAll('.actions button');
			buttons[0].innerText = '保存';
			buttons[1].innerText = '編集中止';
			buttons[0].onclick = () => saveRecord(id);
			buttons[1].onclick = () => backTd(id);
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
				console.log("保存処理開始: ID=" + id);
				console.log("保存されるデータ:", data);

				submitForm("RegisterServlet", data);
				
			// フォーム送信後にアラートを表示
				alert('データ内容を修正しました。\n更新ボタンを押してください。');
		}
		
		
					
					function backTd(id) {
					const row = document.getElementById('row_' + id);
					const divs = row.querySelectorAll('div:not(.actions)');

					// 元の値を取得（保存されている innerText を使う場合は別途保持が必要）
					const originalValues = {
						contents: divs[0].querySelector('input')?.value || '',
						price: divs[1].querySelector('input')?.value || '',
						spending: divs[2].querySelector('input')?.checked ? '支出' : '',
						income: divs[3].querySelector('input')?.checked ? '収入' : '',
						category: divs[4].querySelector('select')?.value || '',
						remarks: divs[5].querySelector('input')?.value || ''
					};

					// 元の表示に戻す
					divs[0].innerHTML = originalValues.contents;
					divs[1].innerHTML = originalValues.price;
					divs[2].innerHTML = originalValues.spending;
					divs[3].innerHTML = originalValues.income;
					divs[4].innerHTML = originalValues.category;
					divs[5].innerHTML = originalValues.remarks;

					// ボタンを元に戻す
					const buttons = row.querySelectorAll('.actions button');
					buttons[0].innerText = '編集';
					buttons[1].innerText = '削除';
					buttons[0].onclick = () => editRecord(id);
					buttons[1].onclick = () => deleteRecord(id);
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
							var category = document.querySelector('[name="category"]').value;
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
		const search = document.getElementById('searchOption').value;
		const cards = document.querySelectorAll('.record-card');

		cards.forEach(card => {
			const spendingText = card.children[2]?.innerText || '';
			const incomeText = card.children[3]?.innerText || '';

			if (search === 'all') {
			card.style.display = '';
			} else if (search === 'spending' && spendingText === '支出') {
			card.style.display = '';
			} else if (search === 'income' && incomeText === '収入') {
			card.style.display = '';
			} else {
			card.style.display = 'none';
			}
		});
		}
					
					
	</script>
</div>
</body>
</html>
