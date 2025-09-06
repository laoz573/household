<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>家計簿　ログイン画面</title>
<link rel="stylesheet" href="./css/Login.css">
</head>
<body>
<div id="container">
  <form action="LoginFilter" method="POST" class="login-form">
      <div class="form-group">
        <h1>家計簿</h1>
          <div id="username">
            <label for="username-input">ユーザー名</label>
            <input type="text" name="username" id="username-input">
          </div>

          <div id="password">
            <label for="password-input">パスワード</label>
            <input type="password" name="password" id="password-input">
          </div>


        <div id ="login">
        <button type="submit" name="action" value="login">ログイン</button>
        </div>
      </div>
  </form>
</div>
</body>
</html>