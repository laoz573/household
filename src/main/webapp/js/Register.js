    // sessionStorage から取得
    let year = parseInt(sessionStorage.getItem("year"));
    let month = parseInt(sessionStorage.getItem("month"));
    let date = parseInt(sessionStorage.getItem("date"));

    // 値がない場合（直接アクセスされた場合）
    if (!year || !month || !date) {
        alert("日付情報がありません。カレンダーから遷移してください。");
    }

    // 日付タイトル
    function updateTitle() {
        document.getElementById("dateTitle").textContent =
            `${year}年${month}月${date}日`;
    }
    updateTitle();

    // -----------------------------
    // ① 指定日のデータ取得（GET）
    // -----------------------------
function loadRecords() {
    fetch(`/api/record?year=${year}&month=${month}&date=${date}`)
        .then(res => res.json())
        .then(list => {

            const table = document.getElementById("recordTable");

            // PC用テーブルのヘッダー以外を削除
            table.querySelectorAll("tr:not(:first-child)").forEach(tr => tr.remove());

            // スマホ用カードを削除
            document.querySelectorAll(".record-card").forEach(e => e.remove());

            list.forEach(r => {

                // -------------------------
                // PC用：テーブル行を生成
                // -------------------------
                const tr = document.createElement("tr");

                tr.innerHTML = `
                    <td><input value="${r.contents}" id="c${r.id}"></td>
                    <td><input value="${r.price}" id="p${r.id}" type="number"></td>
                    <td>
                        <select id="t${r.id}">
                            <option value="収入" ${r.income === "収入" ? "selected" : ""}>収入</option>
                            <option value="支出" ${r.spending === "支出" ? "selected" : ""}>支出</option>
                        </select>
                    </td>
                    <td>
                        <select id="cat${r.id}"></select>
                    </td>
                    <td><input value="${r.remarks}" id="rm${r.id}"></td>
                    <td><input type="date" id="d${r.id}" value="${r.year}-${String(r.month).padStart(2,'0')}-${String(r.date).padStart(2,'0')}"></td>

                    <td id="btnArea${r.id}"></td>
                `;

                const btnArea = tr.querySelector(`#btnArea${r.id}`);

                const updateBtn = document.createElement("button");
                updateBtn.textContent = "更新";

                const deleteBtn = document.createElement("button");
                deleteBtn.textContent = "削除";

                updateBtn.onclick = () => updateRecord(r.id);
                deleteBtn.onclick = () => deleteRecord(r.id);

                btnArea.appendChild(updateBtn);
                btnArea.appendChild(deleteBtn);

                table.appendChild(tr);
                // ★ ここでカテゴリを流し込む
                loadCategoriesForRecord(r.id, r.category, false);

                // -------------------------
                // スマホ用：編集可能カード生成
                // -------------------------
                const card = document.createElement("div");
                card.className = "record-card";

                card.innerHTML = `
                    <div>内容: <input id="c${r.id}_m" value="${r.contents}"></div>
                    <div>金額: <input id="p${r.id}_m" type="number" value="${r.price}"></div>
                    <div>
                        区分:
                        <select id="t${r.id}_m">
                            <option value="支出" ${r.spending === "支出" ? "selected" : ""}>支出</option>
                            <option value="収入" ${r.income === "収入" ? "selected" : ""}>収入</option>
                        </select>
                    </div>
                    <div>
                        カテゴリ:
                        <select id="cat${r.id}_m"></select>
                    </div>
                    <div>備考: <input id="rm${r.id}_m" value="${r.remarks}"></div>
                    <div>日付: <input type="date" id="d${r.id}_m" value="${r.year}-${String(r.month).padStart(2,'0')}-${String(r.date).padStart(2,'0')}"></div>


                    <div class="actions">
                        <button class="update-btn">更新</button>
                        <button class="delete-btn">削除</button>
                    </div>
                `;

                // スマホ用更新ボタン
                card.querySelector(".update-btn").onclick = () => updateRecord(r.id, true);

                // スマホ用削除ボタン
                card.querySelector(".delete-btn").onclick = () => deleteRecord(r.id);

                const reference = document.querySelector(".Reference");
                reference.appendChild(card);
                // ★ ここでカテゴリを流し込む
                loadCategoriesForRecord(r.id, r.category, true);
            });
        });
}

    loadRecords();

    // -----------------------------
    // ② 新規登録（POST）
    // -----------------------------
    document.getElementById("insertBtn").onclick = () => {
        const type = document.querySelector("input[name='type']:checked");

        const data = {
            year: year,
            month: month,
            date: date,
            contents: document.getElementById("contents").value,
            price: parseInt(document.getElementById("price").value),
            income: type && type.value === "収入" ? "収入" : "",
            spending: type && type.value === "支出" ? "支出" : "",
            category: document.getElementById("category").value,
            remarks: document.getElementById("remarks").value
        };

        fetch("/api/record", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(data)
        }).then(() => {
            alert("登録しました");
            loadRecords();
            // ★ 入力欄クリア
            document.getElementById("contents").value = "";
            document.getElementById("price").value = "";
            document.getElementById("spending").checked = true;
            document.getElementById("category").value = "食費";
            document.getElementById("remarks").value = "";
            document.getElementById("categoryAdd").value = "";
            document.getElementById("categoryDelete").value = "";
        });
    };

    // -----------------------------
    // ③ 更新（PUT）
    // -----------------------------
function updateRecord(id, isMobile = false) {
    const suffix = isMobile ? "_m" : "";

    const type = document.getElementById(`t${id}${suffix}`).value;

    // ★ 日付 input の値を取得（yyyy-mm-dd）
    const dateStr = document.getElementById(`d${id}${suffix}`).value;
    const [newYear, newMonth, newDate] = dateStr.split("-").map(Number);

    const data = {
        id,
        year: newYear,
        month: newMonth,
        date: newDate,
        contents: document.getElementById(`c${id}${suffix}`).value,
        price: parseInt(document.getElementById(`p${id}${suffix}`).value),
        income: type === "収入" ? "収入" : "",
        spending: type === "支出" ? "支出" : "",
        category: document.getElementById(`cat${id}${suffix}`).value,
        remarks: document.getElementById(`rm${id}${suffix}`).value
    };

    fetch("/api/record", {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    }).then(() => {
        alert("更新しました");

        updateTitle();
        loadRecords();
    });
}



    // -----------------------------
    // ④ 削除（DELETE）
    // -----------------------------
    function deleteRecord(id) {
        fetch(`/api/record?id=${id}&year=${year}&month=${month}&date=${date}`, {
            method: "DELETE"
        })
        .then(res => res.json())
        .then(() => {
            alert("削除しました");
            loadRecords();
        });
    }

    // -----------------------------
    // ⑤ 前日・翌日
    // -----------------------------
    document.getElementById("prevDay").onclick = () => {
        const d = new Date(year, month - 1, date - 1);
        year = d.getFullYear();
        month = d.getMonth() + 1;
        date = d.getDate();
        updateTitle();
        loadRecords();
    };

    document.getElementById("nextDay").onclick = () => {
        const d = new Date(year, month - 1, date + 1);
        year = d.getFullYear();
        month = d.getMonth() + 1;
        date = d.getDate();
        updateTitle();
        loadRecords();
    };

    document.getElementById("backToCalendar").onclick = () => {
        window.location.href = "Calendar.html";
    };

async function loadCategories() {
    const res = await fetch("/api/category");
    const list = await res.json();

    const select = document.getElementById("category");
    const selectDelete = document.getElementById("categoryDelete");
    const select_m = document.querySelectorAll("[id^='cat'][id$='_m']"); // スマホ用の select 要素も取得

    // 初期化
    select.innerHTML = "";
    selectDelete.innerHTML = "";

    // ★ その他を最後に回す
    const sorted = list.sort((a, b) => {
        if (a.name === "その他") return 1;
        if (b.name === "その他") return -1;
        return 0;
    });

    sorted.forEach(cat => {
        // 登録の選択用
        const opt = document.createElement("option");
        opt.value = cat.name;
        opt.textContent = cat.name;
        select.appendChild(opt);

        // 削除用（id）
        const optDelete = document.createElement("option");
        optDelete.value = cat.id;
        optDelete.textContent = cat.name;
        selectDelete.appendChild(optDelete);

        // スマホ用の選択用
        select_m.forEach(select => {
            const opt = document.createElement("option");
            opt.value = cat.name;
            opt.textContent = cat.name;
            select.appendChild(opt);
        });
    });
}

async function loadCategoriesForRecord(recordId, currentCategory, isMobile = false) {
    const res = await fetch("/api/category");
    const list = await res.json();

    const suffix = isMobile ? "_m" : "";
    const select = document.getElementById(`cat${recordId}${suffix}`);
    select.innerHTML = "";

    const sorted = list.sort((a, b) => {
        if (a.name === "その他") return 1;
        if (b.name === "その他") return -1;
        return 0;
    });

    sorted.forEach(cat => {
        const opt = document.createElement("option");
        opt.value = cat.name;
        opt.textContent = cat.name;

        if (cat.name === currentCategory) {
            opt.selected = true;
        }

        select.appendChild(opt);
    });
}


document.getElementById("addCategoryBtn").onclick = async () => {
    const name = document.getElementById("categoryAdd").value;

    const res = await fetch("/api/category", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({ name })
    });

    const data = await res.json();

    if (data.success) {
        alert("カテゴリを追加しました");
        loadCategories();
    } else {
        alert("追加に失敗しました: " + data.message);
    }
};

// ★ カテゴリ削除処理
document.getElementById("deleteCategoryBtn").onclick = async () => {
    const select = document.getElementById("categoryDelete");
    const categoryId = select.value;

    if (!categoryId) {
        alert("削除するカテゴリを選択してください");
        return;
    }

    const res = await fetch(`/api/category?id=${categoryId}`, {
        method: "DELETE"
    });

    const data = await res.json();

    if (data.success) {
        alert("カテゴリを削除しました");
        loadCategories(); // ← select を更新
    } else {
        alert("削除に失敗しました: " + data.message);
    }
};

// ページ読み込み時にカテゴリ一覧を読み込む
window.onload = () => {
    loadCategories();
};