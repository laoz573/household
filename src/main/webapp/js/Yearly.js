  // -----------------------------
  // 初期設定
  // -----------------------------
  const now = new Date();
  let currentYear = now.getFullYear();
  let currentMonth = now.getMonth() + 1;

  // 前画面からの値
  let year = parseInt(sessionStorage.getItem("year"));
  let month = parseInt(sessionStorage.getItem("month"));

  // NaN 対策（初回アクセス時）
  if (isNaN(year)) year = currentYear;
  if (isNaN(month)) month = currentMonth;

  const yearSelect = document.getElementById("yearInput");
  const monthSelect = document.getElementById("monthInput");

  // 年選択肢を生成
  for (let y = 2023; y <= currentYear + 10; y++) {
    const opt = document.createElement("option");
    opt.value = y;
    opt.textContent = y + "年";
    yearSelect.appendChild(opt);
  }

  // ★ 正しい初期値セット
  yearSelect.value = year;
  monthSelect.value = month;

  // -----------------------------
  // API① 年間収支取得
  // -----------------------------
  function loadYearly() {
    const year = parseInt(yearSelect.value);

    fetch(`/api/yearly?year=${year}`)
      .then(res => res.json())
      .then(data => {
        document.getElementById("yearlyIncome").textContent =
          data.yearlyTotalIncome + "円";
        document.getElementById("yearlySpending").textContent =
          data.yearlyTotalSpending + "円";

        drawBarChart(data.monthlyTotalIncomes, data.monthlyTotalSpendings);
      });
  }

  // -----------------------------
  // API② カテゴリ別支出取得
  // -----------------------------
  function loadCategory() {
    const year = parseInt(yearSelect.value);
    const month = parseInt(monthSelect.value);

    fetch(`/api/yearly/category?year=${year}&month=${month}`)
      .then(res => res.json())
      .then(data => {
        const categories = data.categories || {};
        const labels = Object.keys(categories);
        const values = Object.values(categories);

        drawPieChart(labels, values);
      });
  }

  // -----------------------------
  // 棒グラフ描画
  // -----------------------------
  let barChart;
  function drawBarChart(incomes, spendings) {
    const ctx = document.getElementById("monthlyFinanceChart").getContext("2d");

    if (barChart) barChart.destroy();

    barChart = new Chart(ctx, {
      type: "bar",
      data: {
        labels: ["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"],
        datasets: [
          {
            label: "収入",
            data: incomes,
            backgroundColor: "rgba(54, 162, 235, 0.2)",
            borderColor: "rgba(54, 162, 235, 1)",
            borderWidth: 1
          },
          {
            label: "支出",
            data: spendings,
            backgroundColor: "rgba(255, 99, 132, 0.2)",
            borderColor: "rgba(255, 99, 132, 1)",
            borderWidth: 1
          }
        ]
      },
      options: {
        responsive: true,
        scales: {
          y: { beginAtZero: true }
        }
      }
    });
  }

  // -----------------------------
  // 円グラフ描画
  // -----------------------------
 let pieChart;
function drawPieChart(labels, values) {
  const ctx = document.getElementById("categoryPieChart").getContext("2d");

  if (pieChart) pieChart.destroy();

  // ▼ labels と values をセットにして降順ソート
  const sorted = labels
    .map((label, i) => ({ label, value: values[i] }))
    .sort((a, b) => b.value - a.value); // ← 高い順

  const sortedLabels = sorted.map(item => item.label);
  const sortedValues = sorted.map(item => item.value);

  pieChart = new Chart(ctx, {
    type: "doughnut",
    data: {
      labels: sortedLabels.length ? sortedLabels : ["データなし"],
      datasets: [{
        data: sortedValues.length ? sortedValues : [0],
        backgroundColor: [
          "rgba(255, 99, 132, 0.6)",
          "rgba(54, 162, 235, 0.6)",
          "rgba(255, 206, 86, 0.6)",
          "rgba(75, 192, 192, 0.6)",
          "rgba(153, 102, 255, 0.6)"
        ]
      }]
    },
    options: {
      responsive: true,
      plugins: {
        legend: { position: "bottom" },
        datalabels: {
          color: "#000",
          formatter: (value, ctx) => {
            const label = ctx.chart.data.labels[ctx.dataIndex];
            return label + "\n¥" + value.toLocaleString();
          }
        }
      }
    },
    plugins: [ChartDataLabels]
  });
}


  // -----------------------------
  // カレンダーへ遷移
  // -----------------------------
  document.getElementById("goCalendar").onclick = () => {
    sessionStorage.setItem("year", yearSelect.value);
    sessionStorage.setItem("month", monthSelect.value);
    window.location.href = "Calendar.html";
  };

  // -----------------------------
  // 初回ロード
  // -----------------------------
  loadYearly();
  loadCategory();

  // 年 or 月が変わったら再取得
  yearSelect.onchange = () => {
    loadYearly();
    loadCategory();
  };
  monthSelect.onchange = () => {
    loadCategory();
  };

    document.getElementById("registerBtn").onclick = async () => {
    const username = document.getElementById("regUser").value;
    const password = document.getElementById("regPass").value;

    const res = await fetch("/api/user/register", {
      method: "POST",
      headers: {"Content-Type": "application/x-www-form-urlencoded"},
      body: `username=${username}&password=${password}`
    });

    const data = await res.json();

    if (data.success) {
      alert("登録しました");
    } else {
      alert("エラー: " + data.message);
    }
  };