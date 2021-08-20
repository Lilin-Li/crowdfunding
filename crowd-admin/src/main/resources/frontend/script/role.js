$(function () {
    window.pageNum = 1;
    window.pageSize = 5;
    window.keyword = "";

    generatePage();

    $("#searchBtn").click(function () {
        window.keyword = $("#keywordInput").val();
        generatePage();

    });

    $("tbody .btn-success").click(function () {
        window.location.href = "assignPermission.html";
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

        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
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
    $("#removeRoleBtn").click(function() {

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
        if(roleArray.length == 0) {
            layer.msg("請至少選擇一個執行刪除");
            return ;
        }

        // 呼叫專門的函式打開模態框
        showConfirmModal(roleArray);
    });

});
