define(['basejs'], function (basejs) {
    var result = function (emptyContent) {
        return {
            "bProcessing": true,
            "bServerSide": true,
            "bFilter": false,
            "bSort": false,
            "bLengthChange": false,
            "iDisplayLength": 5,
            "sFisrt": true,
            "oLanguage": {
                "sLengthMenu": "每页显示 _MENU_ 条记录",
                "sZeroRecords": "<div class='search-result-empyt'>" +
                "<i class='fa fa-frown-o'></i>" +
                "<p>" + emptyContent + "</p>" +
                "</div>",
                "sInfo": "从 _START_ 到 _END_ ，共 _TOTAL_ 条数据",
                "sInfoEmpty": "没有数据",
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": "上一页",
                    "sNext": "下一页",
                    "sLast": "尾页"
                },
                "sProcessing": "<img src='" + env.contextUri + "img/default_loading_bg_white.gif'>"
            },
            "fnServerData": function (sSource, aoData, fnCallback, oSettings) {
                oSettings.jqXHR = $.ajax({
                    "type": "GET",
                    "url": sSource,
                    timeout: 20000,
                    "data": aoData,
                    "success": function (e) {
                    },
                    complete: function (XMLHttpRequest, status) {
                    }
                });
            }
        };

    };

    return result;

});