function generatePage() {

    var pageInfo = getPageInfoRemote();
    fillTableBody(pageInfo);

}

// 遠端訪問伺服器端程式獲取pageInfo 數據
function getPageInfoRemote() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    // 呼叫$.ajax()函式發送請求並接受$.ajax()函式的返回值
    var ajaxResult = $.ajax({
        "url": "/role/get/page/info.json",
        "type":"post",
        "beforeSend": function(xhr){xhr.setRequestHeader(header, token);},
        "data": {
            "pageNum": window.pageNum,
            "pageSize": window.pageSize,
            "keyword": window.keyword
        },
        "async":false,
        "dataType":"json"
    });

    console.log(ajaxResult);

    // 判斷目前響應狀態碼是否為200
    var statusCode = ajaxResult.status;

    // 如果目前響應狀態碼不是200，說明發生了錯誤或其他意外情況，顯示提示訊息，讓目前函式停止執行
    if(statusCode != 200) {
        layer.msg("失敗！響應狀態碼="+statusCode+" 說明資訊="+ajaxResult.statusText);
        return null;
    }

    // 如果響應狀態碼是200，說明請求處理成功，獲取pageInfo
    var resultEntity = ajaxResult.responseJSON;

    // 從resultEntity 中獲取result 屬性
    var result = resultEntity.result;

    // 判斷result 是否成功
    if(result == "FAILED") {
        layer.msg(resultEntity.message);
        return null;
    }

    // 確認result 為成功后獲取pageInfo
    var pageInfo = resultEntity.data;
    return pageInfo;
}


// 填充表格
function fillTableBody(pageInfo) {

    // 清除tbody 中的舊的內容
    $("#rolePageBody").empty();

    // 這裡清空是爲了讓沒有搜索結果時不顯示頁碼導航條
    $("#Pagination").empty();

    // 判斷pageInfo 對象是否有效
    if(pageInfo == null || pageInfo == undefined || pageInfo.list == null || pageInfo.list.length == 0) {
        $("#rolePageBody").append("<tr><td colspan='4' align='center'>抱歉！沒有查詢到您搜索的數據！</td></tr>");

    return ;
    }

    // 使用pageInfo 的list 屬性填充tbody
    for(var i = 0; i < pageInfo.list.length; i++) {

        var role = pageInfo.list[i];

        var roleId = role.id;

        var roleName = role.name;

        var numberTd = "<td>"+(i+1)+"</td>";
        var checkboxTd = "<td><input type='checkbox'></td>";
        var roleNameTd = "<td>"+roleName+"</td>";

        var checkBtn = "<button type='button' class='btn btn-success btn-xs'><i class='glyphicon glyphicon-check'></i></button>";
        var pencilBtn = "<button type='button' class='btn btn-primary btn-xs'><i class='glyphicon glyphicon-pencil'></i></button>";
        var removeBtn = "<button type='button' class='btn btn-danger btn-xs'><i class='glyphicon glyphicon-remove'></i></button>";

        var buttonTd = "<td>"+checkBtn+" "+pencilBtn+" "+removeBtn+"</td>";

        var tr = "<tr>"+numberTd+checkboxTd+roleNameTd+buttonTd+"</tr>";

        $("#rolePageBody").append(tr);
    }

    // 產生分頁導航條
    generateNavigator(pageInfo);
}

// 產生分頁頁碼導航條
function generateNavigator(pageInfo) {

    // 獲取總記錄數
    var totalRecord = pageInfo.total;

    // 聲明相關屬性
    var properties = {
        "num_edge_entries": 3,
        "num_display_entries": 5,
        "callback": paginationCallBack,
        "items_per_page": pageInfo.pageSize,
        "current_page": pageInfo.pageNum - 1,
        "prev_text": "上一頁",
        "next_text": "下一頁"
    }

    // 呼叫pagination()函式
    $("#Pagination").pagination(totalRecord, properties);
}


// 翻頁時的回撥函式
function paginationCallBack(pageIndex, jQuery) {

    // 修改window 對象的pageNum 屬性
    window.pageNum = pageIndex + 1;

    // 呼叫分頁函式
    generatePage();

    // 取消頁碼超鏈接的預設行為
    return false;

}