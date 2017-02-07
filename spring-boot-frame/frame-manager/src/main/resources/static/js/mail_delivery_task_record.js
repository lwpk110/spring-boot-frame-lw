require.config({
    paths: {
        'jquery': 'lib/jquery-1.8.3.min',
        'myAlert': 'others/myAlert/js/myAlert',
        'placeholder_moulde': 'placeholder_moulde',
        'datatables': 'lib/datatables/media/js/jquery.dataTables',
        'dataTableParam': 'datatable_param_moudle'
    },
    map: {
        '*': {
            'css': 'css.min'
        }
    },
    shim: {
        'myAlert': {deps: ['jquery', 'css!../js/others/myAlert/css/myalert.css']},
        'datatables': {
            deps: ['jquery',
                'css!../js/lib/datatables/media/css/jquery.dataTables.css',
                'css!../js/lib/datatables/media/css/jquery.dataTables_themeroller.css'],
            exports: 'datatables'
        }
    }
});

require(['basejs', 'jquery', 'myAlert', 'placeholder_moulde', 'datatables','dataTableParam'], function (basejs, jquery, myAlert, placeholder_moulde, datatables,dataTableParam) {

    placeholder_moulde("maskmanger_emailadd", "请输入完整邮箱地址", "#bfbfbf");

    var resultTableId = "email_history_record";	//表格的id

    //加载表格
    var table = $("#" + resultTableId).dataTable($.extend({}, dataTableParam("没有查到邮箱记录~"), {
        "sAjaxSource": "mail_record_list",
        "iDisplayLength": 10,
        "sFisrt": true,
        "fnServerData": function (sSource, aoData, fnCallback, oSettings) {
            var inputValue = $.trim($("#email").val());
            if (inputValue == "" || inputValue == "请输入完整邮箱地址" || inputValue == null) {
                $("#emptyResult").addClass("hidden-item");
                $(".search-data-show").addClass("hidden-item");
                $("#mailValueEmpty").removeClass("hidden-item");
            } else {
                oSettings.jqXHR = $.ajax({
                    "type": "GET",
                    "url": sSource,
                    timeout: 20000,
                    "data": aoData,
                    "success": function (e) {
                        if (typeof(e) == "object") {
                            if (e.iTotalDisplayRecords == 0) {
                                $("#emptyResult").removeClass("hidden-item");
                                $(".search-data-show").addClass("hidden-item");
                                $("#mailValueEmpty").addClass("hidden-item");
                            } else {
                                $("#emptyResult").addClass("hidden-item");
                                $(".search-data-show").removeClass("hidden-item");
                                $("#mailValueEmpty").addClass("hidden-item");
                                fnCallback(e);
                            }
                        }
                    }
                });
            }

        },
        "fnServerParams": function (aoData) {
            aoData.push({"name": "email", "value": $("#email").val()});
        },
        "columns": [

            {
                "title": "邮 箱", "sWidth": "35%",
                render: function (data, type, row, meta) {
                    return "<p class='v-left'>" + row.email + "</p>";
                }
            },

            {
                "title": "更新时间（状态）", "sWidth": "21.5%",
                render: function (data, type, row, meta) {
                    var status = "";
                    switch (row.actionStatus){
                        case "SENT_SUCCESS":
                            status = "发送成功";
                            break;
                        case "OPEN":
                            status = "打开";
                            break;
                        case "CLICK":
                            status = "点击";
                            break;
                        case "UNSUBSCRIBE":
                            status = "退订";
                            break;
                        case "HARD_BOUNCE":
                            status = "硬退";
                            break;
                        case "SOFT_BOUNCE":
                            status = "软退";
                            break;
                    }
                    if(row.actionDate){
                        return "<p class='v-left'>" + new Date(row.actionDate).format("yyyy-MM-dd")
                            + "<span class='grey-color'>"+" " + new Date(row.actionDate).format("hh:mm") + "</span>（"+status+"）"
                            + "</p>";
                    }else{
                        return "-";
                    }

                }
            },

            {
                "title": "任务名称", "sWidth": "35%",
                render: function (data, type, row, meta) {
                    return "<p class='v-left'>" + row.taskName + "</p>";
                }
            },

            {
                "title": "", "sWidth": "8.5%",
                render: function (data, type, row, meta) {
                    return "<div class='v-middle td-action'>"
                        + "<i class='fa fa-arrow-circle-right' onclick=\"showTaskDetail('" + row.taskId + "')\" " + "title='查看任务详情'"
                        + "></i>" + "</div>";
                }
            }
        ]
    }));

    $(".button-size-small").on({
        'click': function () {
            var inputValue = $.trim($("#email").val());
            if (inputValue == "" || inputValue == "请输入完整邮箱地址" || inputValue == null) {
                $("#emptyResult").addClass("hidden-item");
                $(".search-data-show").addClass("hidden-item");
                $("#mailValueEmpty").removeClass("hidden-item");
            } else {
                table.fnClearTable(0);
                table.fnDraw();
            }
        }
    }).parent().prev().find("input").on({
        'keydown':function(event){
            if(event.keyCode == 13){
                $(".button-size-small").click();
            }
        }
    });

    showTaskDetail = function (taskId) {
        location.href = env.contextUri + "user/delivery_task/" + taskId + "/report";
    }
});