<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>家計簿　ログイン画面</title>
<link rel="stylesheet" href="./css/Login.css">
</head>
<body>

<div class="login-container">
  <h1>家計簿</h1>
  <form action="LoginFilter" method="POST" class="login-form">
      <div class="form-group">
        <label for="username">ユーザー名:</label>
        <input type="text" name="username">
      </div>
      <div class="form-group">
        <label for="password">パスワード:</label>
        <input type="password" name="password">
      </div>
      <div class="form-group">
        <button type="submit" name="action" value="login">ログイン</button>
      </div>
  </form>
</div>
</body>
</html>