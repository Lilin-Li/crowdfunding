// 修改預設的圖示
function myAddDiyDom(treeId, treeNode) {
    // treeId 是整個樹形結構附著的 ul 標籤的 id
    console.log("treeId=" + treeId);
    // 目前樹形節點的全部的數據，包括從後端查詢得到的 Menu 對象的全部屬性
    console.log(treeNode);
    // zTree 產生 id 的規則
    // 例子：treeDemo_7_ico
    // 解析：ul 標籤的 id_目前節點的序號_功能
    // 提示：「ul 標籤的 id_目前節點的序號」部分可以通過訪問 treeNode 的 tId 屬性得到
    // 根據 id 的產生規則拼接出來 span 標籤的 id
    var spanId = treeNode.tId + "_ico";
    //根據控制圖示的 span 標籤的 id 找到這個 span 標籤
    // 刪除舊的 class
    // 新增新的 class
    $("#" + spanId)
        .removeClass()
        .addClass(treeNode.icon);
}

// 在滑鼠移入節點範圍時新增按鈕組
function myAddHoverDom(treeId, treeNode) {
    // 按鈕組的標籤結構：<span><a><i></i></a><a><i></i></a></span>
    // 按鈕組出現的位置：節點中 treeDemo_n_a 超鏈接的後面
    // 爲了在需要移除按鈕組的時候能夠精確定位到按鈕組所在 span，需要給 span 設定有規律的id
    var btnGroupId = treeNode.tId + "_btnGrp";
    // 判斷一下以前是否已經新增了按鈕組
    if ($("#" + btnGroupId).length > 0) {
        return;
    }

    // 準備各個按鈕的 HTML 標籤
    var addBtn = "<a id='" + treeNode.id + "' class='addBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0 px;' href='#' title=' 新增子節點'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
    var removeBtn = "<a id='" + treeNode.id + "' class='removeBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0 px;' href='#' title=' 刪 除 節 點 '>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";
    var editBtn = "<a id='" + treeNode.id + "' class='editBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0 px;' href='#' title=' 修 改 節 點 '>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";


    // 獲取目前節點的級別數據
    var level = treeNode.level;
    // 聲明變數儲存拼裝好的按鈕程式碼
    var btnHTML = "";
    // 判斷目前節點的級別
    if (level == 0) {
        // 級別為 0 時是根節點，只能新增子節點
        btnHTML = addBtn;

    }

    if (level == 1) {
        // 級別為 1 時是分支節點，可以新增子節點、修改
        btnHTML = addBtn + " " + editBtn;
        // 獲取目前節點的子節點數量
        var length = treeNode.children.length;
        // 如果沒有子節點，可以刪除
        if (length == 0) {
            btnHTML = btnHTML + " " + removeBtn;
        }
    }

    if (level == 2) {
        // 級別為 2 時是葉子節點，可以修改、刪除
        btnHTML = editBtn + " " + removeBtn;

    }

    // 找到附著按鈕組的超鏈接
    var anchorId = treeNode.tId + "_a";
    // 執行在超鏈接後面附加 span 元素的操作
    $("#" + anchorId).after("<span id='" + btnGroupId + "'>" + btnHTML + "</span>");
}


// 在滑鼠離開節點範圍時刪除按鈕組
function myRemoveHoverDom(treeId, treeNode) {
    // 拼接按鈕組的 id
    var btnGroupId = treeNode.tId + "_btnGrp";
    // 移除對應的元素
    $("#" + btnGroupId).remove();
}