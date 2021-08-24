var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

$(function () {
    window.pageNum = 1;
    window.pageSize = 5;
    window.keyword = "";

    generatePage();

    $("#searchBtn").click(function () {
        window.keyword = $("#keywordInput").val();
        generatePage();

    });

    $("#showAddModalBtn").click(function () {
        $("#addModal").modal("show");
    });

    // 給新增模態框中的儲存按鈕繫結單擊響應函式
    $("#saveRoleBtn").click(function () {

        // ①獲取使用者在文字框中輸入的角色名稱
        // #addModal 表示找到整個模態框
        // 空格表示在後代元素中繼續查詢
        // [name=roleName]表示匹配name 屬性等於roleName 的元素
        var roleName = $.trim($("#addModal [name=roleName]").val());


        // 呼叫$.ajax()函式發送請求並接受$.ajax()函式的返回值
        // ②發送Ajax 請求
        $.ajax({
            "url": "/role/save.json",
            "type": "post",
            "data": {
                "name": roleName
            },
            "dataType": "json",
            "beforeSend": function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            "success": function (response) {

                var result = response.result;

                if (result == "SUCCESS") {
                    layer.msg("操作成功！");

                    // 將頁碼定位到第一頁
                    window.pageNum = 1;

                    // 重新載入分頁數據
                    generatePage();
                }
                if (result == "FAILED") {
                    layer.msg("操作失敗！" + response.message);
                }

            },
            "error": function (response) {
                layer.msg(response.status + " " + response.statusText);
            }
        });

        // 關閉模態框
        $("#addModal").modal("hide");

        // 清理模態框
        $("#addModal [name=roleName]").val("");
    });

    // 給頁面上的「鉛筆」按鈕繫結單擊響應函式，目的是打開模態框
    // 傳統的事件繫結方式只能在第一個頁面有效，翻頁后失效了
    // $(".pencilBtn").click(function(){
    // alert("aaaa...");
    // });
    // 使用jQuery 對象的on()函式可以解決上面問題
    // ①首先找到所有「動態產生」的元素所附著的「靜態」元素
    // ②on()函式的第一個參數是事件型別
    // ③on()函式的第二個參數是找到真正要繫結事件的元素的選擇器
    // ③on()函式的第三個參數是事件的響應函式
    $("#rolePageBody").on("click", ".pencilBtn", function () {
        // 打開模態框
        $("#editModal").modal("show");

        // 獲取表格中目前行中的角色名稱
        var roleName = $(this).parent().prev().text();

        // 獲取目前角色的id
        // 依據是：var pencilBtn = "<button id='"+roleId+"' ……這段程式碼中我們把roleId 設定到id屬性了
        // 爲了讓執行更新的按鈕能夠獲取到roleId 的值，把它放在全域性變數上
        window.roleId = this.id;

        // 使用roleName 的值設定模態框中的文字框
        $("#editModal [name=roleName]").val(roleName);
    });

    $("#updateRoleBtn").click(function () {
        // ①從文字框中獲取新的角色名稱
        var roleName = $("#editModal [name=roleName]").val();

        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        // ②發送Ajax 請求執行更新
        $.ajax({
            "url": "/role/update.json",
            "type": "post",
            "data": {
                "id": window.roleId,
                "name": roleName
            },
            "beforeSend": function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            "dataType": "json",
            "success": function (response) {

                var result = response.result;

                if (result == "SUCCESS") {
                    layer.msg("操作成功！");

                    // 重新載入分頁數據
                    generatePage();
                }

                if (result == "FAILED") {
                    layer.msg("操作失敗！" + response.message);
                }

            },
            "error": function (response) {
                layer.msg(response.status + " " + response.statusText);
            }
        });

        // ③關閉模態框
        $("#editModal").modal("hide");
    });


    $(function () {
        $(".list-group-item").click(function () {
            if ($(this).find("ul")) {
                $(this).toggleClass("tree-closed");
                if ($(this).hasClass("tree-closed")) {
                    $("ul", this).hide("fast");
                } else {
                    $("ul", this).show("fast");
                }
            }
        });
    });

    $("#rolePageBody").on("click", ".removeBtn", function () {
        // 打開模態框
        $("#confirmModal").modal("show");

        // 獲取表格中目前行中的角色名稱
        var roleName = $(this).parent().prev().text();

        // 獲取目前角色的id
        // 依據是：var pencilBtn = "<button id='"+roleId+"' ……這段程式碼中我們把roleId 設定到id屬性了
        // 爲了讓執行更新的按鈕能夠獲取到roleId 的值，把它放在全域性變數上

        var roleArray = [];

        // 使用this 引用目前遍歷得到的多選框
        var roleId = this.id;

        // 通過DOM 操作獲取角色名稱
        var roleName = $(this).parent().prev().text();

        roleArray.push({
            "roleId": roleId,
            "roleName": roleName
        });

        // 呼叫專門的函式打開模態框
        showConfirmModal(roleArray);
    });

    // 8.點選確認模態框中的確認刪除按鈕執行刪除
    $("#removeRoleBtn").click(function () {

        // 從全域性變數範圍獲取roleIdArray，轉換為JSON 字串
        var requestBody = JSON.stringify(window.roleIdArray);
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $.ajax({
            "url": "/role/remove/by/role/id/array.json",
            "type": "post",
            "data": requestBody,
            "contentType": "application/json;charset=UTF-8",
            "dataType": "json",
            "beforeSend": function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            "success": function (response) {

                var result = response.result;

                if (result == "SUCCESS") {
                    layer.msg("操作成功！");

                    // 重新載入分頁數據
                    generatePage();
                }

                if (result == "FAILED") {
                    layer.msg("操作失敗！" + response.message);
                }

            },
            "error": function (response) {
                layer.msg(response.status + " " + response.statusText);
            }
        });
        // 關閉模態框
        $("#confirmModal").modal("hide");
    });
    // 10.給總的checkbox 繫結單擊響應函式
    $("#summaryBox").click(function () {


        // ①獲取目前多選框自身的狀態
        var currentStatus = this.checked;

        // ②用目前多選框的狀態設定其他多選框
        $(".itemBox").prop("checked", currentStatus);

    });


    // 11.全選、全不選的反向操作
    $("#rolePageBody").on("click", ".itemBox", function () {

        // 獲取目前已經選中的.itemBox 的數量
        var checkedBoxCount = $(".itemBox:checked").length;

        // 獲取全部.itemBox 的數量
        var totalBoxCount = $(".itemBox").length;

        // 使用二者的比較結果設定總的checkbox
        $("#summaryBox").prop("checked", checkedBoxCount == totalBoxCount);

    });

    // 12.給批量刪除的按鈕繫結單擊響應函式
    $("#batchRemoveBtn").click(function () {

        // 建立一個陣列對像用來存放後面獲取到的角色對像
        var roleArray = [];

        // 遍歷目前選中的多選框
        $(".itemBox:checked").each(function () {

            // 使用this 引用目前遍歷得到的多選框
            var roleId = this.id;

            // 通過DOM 操作獲取角色名稱
            var roleName = $(this).parent().next().text();

            roleArray.push({
                "roleId": roleId,
                "roleName": roleName
            });
        });

        // 檢查roleArray 的長度是否為0
        if (roleArray.length == 0) {
            layer.msg("請至少選擇一個執行刪除");
            return;
        }

        // 呼叫專門的函式打開模態框
        showConfirmModal(roleArray);
    });
    $("#rolePageBody").on("click", ".checkBtn", function () {
        // 打開模態框
        window.roleId = this.id;
        $("#assignModal").modal("show");
        // 在模態框中裝載樹 Auth 的形結構數據
        fillAuthTree();
    });
});

// 聲明專門的函式用來在分配 Auth 的模態框中顯示 Auth 的樹形結構數據
function fillAuthTree() {
    // 1.發送 Ajax 請求查詢 Auth 數據
    var ajaxReturn = $.ajax({
        "url": "/assgin/get/all/auth.json",
        "type": "post",
        "beforeSend": function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        "dataType": "json",
        "async": false
    });
    if (ajaxReturn.status != 200) {
        layer.msg(" 請 求 處 理 出 錯 ！ 響 應 狀 態 碼 是 ： " + ajaxReturn.status + " 說 明 是 ：" + ajaxReturn.statusText);
        return;

    }

    // 2.從響應結果中獲取 Auth 的 JSON 數據
    // 從伺服器端查詢到的 list 不需要組裝成樹形結構，這裡我們交給 zTree 去組裝
    var authList = ajaxReturn.responseJSON.data;
    // 3.準備對 zTree 進行設定的 JSON 對像
    var setting = {
        "data": {
            "simpleData": {
                // 開啟簡單 JSON 功能：不用我們在後端組tree，只要有pid，ztree會被我們在前端組成tree
                "enable": true,
                // 使用 categoryId 屬性關聯父節點，不用預設的 pId 了
                "pIdKey": "categoryId"
            },
            "key": {
                // 使用 title 屬性顯示節點名稱，不用預設的 name 作為屬性名了
                "name": "title"
            }
        },
        "check": {
            "enable": true
        }
    };
    // 4.產生樹形結構
    // <ul id="authTreeDemo" class="ztree"></ul>
    $.fn.zTree.init($("#authTreeDemo"), setting, authList);
    // 獲取 zTreeObj 對像
    var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
    // 呼叫 zTreeObj 對象的方法，把節點展開
    zTreeObj.expandAll(true);


    // 5.查詢已分配的 Auth 的 id 組成的陣列
    ajaxReturn = $.ajax({
        "url": "/assign/get/assigned/auth/id/by/role/id.json",
        "type": "post",
        "beforeSend": function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        "data": {
            "roleId": window.roleId
        },
        "dataType": "json",
        "async": false

    });
    if (ajaxReturn.status != 200) {
        layer.msg(" 請 求 處 理 出 錯 ！ 響 應 狀 態 碼 是 ： " + ajaxReturn.status + " 說 明 是 ：" + ajaxReturn.statusText);
        return;

    }
    // 從響應結果中獲取 authIdArray
    var authIdArray = ajaxReturn.responseJSON.data;

    // 6.根據 authIdArray 把樹形結構中對應的節點勾選上
    // ①遍歷 authIdArray
    for (var i = 0; i < authIdArray.length; i++) {
        var authId = authIdArray[i];
        // ②根據 id 查詢樹形結構中對應的節點
        var treeNode = zTreeObj.getNodeByParam("id", authId);
        // ③將 treeNode 設定為被勾選
        // checked 設定為 true 表示節點勾選
        var checked = true;
        // checkTypeFlag 設定為 false，表示不「聯動」，不聯動是爲了避免把不該勾選的勾選上
        var checkTypeFlag = false;
        // 執行
        zTreeObj.checkNode(treeNode, checked, checkTypeFlag);
    }

    $("#assignBtn").click(function () {
        // ①收集樹形結構的各個節點中被勾選的節點
        // [1]聲明一個專門的陣列存放 id
        var authIdArray = [];
        // [2]獲取 zTreeObj 對像
        var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
        // [3]獲取全部被勾選的節點
        var checkedNodes = zTreeObj.getCheckedNodes();
        // [4]遍歷 checkedNodes
        for (var i = 0; i < checkedNodes.length; i++) {
            var checkedNode = checkedNodes[i];
            var authId = checkedNode.id;
            authIdArray.push(authId);
        }

        // ②發送請求執行分配
        var requestBody = {
            "authIdArray": authIdArray,
            // 爲了伺服器端 handler 方法能夠統一使用 List<Integer>方式接收數據，roleId 也存入陣列
            "roleId": [window.roleId]
        };
        requestBody = JSON.stringify(requestBody);
        $.ajax({
            "url": "/assign/do/role/assign/auth.json",
            "type": "post",
            "data": requestBody,
            "contentType": "application/json;charset=UTF-8",
            "dataType": "json",
            "beforeSend": function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            "success": function (response) {
                var result = response.result;
                if (result == "SUCCESS") {
                    layer.msg("操作成功！");

                }

                if (result == "FAILED") {
                    layer.msg("操作失敗！" + response.message);

                }

            },
            "error": function (response) {
                layer.msg(response.status + " " + response.statusText);
            }
        });
        $("#assignModal").modal("hide");
    });
}
