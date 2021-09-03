$(function () {
    generateMenuTree();
})


var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

function generateMenuTree() {
    // 1.準備產生樹形結構的 JSON 數據，數據的來源是發送 Ajax 請求得到
    $.ajax({
        "url": "/menu/get/whole/tree.json",
        "type": "post",
        "dataType": "json",
        "beforeSend": function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        "success": function (response) {
            var result = response.result;
            if (result == "SUCCESS") {
                // 2.建立 JSON 對像用於儲存對 zTree 所做的設定
                var setting = {
                    "view": {
                        "addDiyDom": myAddDiyDom,
                    }
                };

                // 3.從響應體中獲取用來產生樹形結構的 JSON 數據
                var zNodes = response.data;
                // 4.初始化樹形結構
                $.fn.zTree.init($("#treeMenu"), setting, zNodes);
            }

            if (result == "FAILED") {
                layer.msg(response.message);
            }
        }
    });

}