  // エラー表示（LoginFilter から ?error=1 で返す）
  const params = new URLSearchParams(window.location.search);
  if (params.get("error") === "1") {
      alert("ユーザー名またはパスワードが違います");
  }