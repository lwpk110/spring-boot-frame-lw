require.config({
    paths: {
        'jquery': 'lib/jquery-1.8.3.min',
        'myAlert': 'others/myAlert/js/myAlert',
        'placeholder_moulde': 'placeholder_moulde',
        'datatables': 'lib/datatables/media/js/jquery.dataTables',
        'handlebars': 'lib/handlebars',
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

require(['basejs', 'jquery', 'myAlert', 'placeholder_moulde', 'datatables', 'handlebars', 'dataTableParam'], function (basejs, jquery, myAlert, placeholder_moulde, datatables, handlebars, dataTableParam) {

    var maskReport_wapper = "maskReport_wapper";//弹窗wapper的id
    //弹窗wapper
    $('body').append("<div id='" + maskReport_wapper + "' style='display:none;'></div>");

    var resultTableId = "email_report_detail";	//表格的id

    var taskId = $(".maskreport-info-tit").attr("taskId");
    $("#" + resultTableId).dataTable($.extend({}, dataTableParam("你还没有任务报告~"), {
        "sAjaxSource": env.contextUri + "user/delivery_task/" + taskId + "/report_detail",
        "iDisplayLength": 20,
        "fnServerData": function (sSource, aoData, fnCallback, oSettings) {
            oSettings.jqXHR = $.ajax({
                "type": "GET",
                "url": sSource,
                timeout: 20000,
                "data": aoData,
                "success": function (e) {
                    if (typeof(e) == "object") {
                        fnCallback(e);
                        if (e.iTotalDisplayRecords == 0) {
                            $("#email_report_detail_paginate").addClass("hidden-item");
                            $("#email_report_detail_info").addClass("hidden-item");
                        } else {
                            $("#email_report_detail_paginate").removeClass("hidden-item").css("float","right").find("span").addClass("hidden-item");
                            $("#email_report_detail_info").addClass("hidden-item");
                        }

                        if(e.aaData.length == 0 ) {
                            $("#" + export_report_pre).addClass('hidden-item');
                            $("#" + add_recipient_pre).addClass('hidden-item');
                        }else {
                            $("#" + export_report_pre).removeClass('hidden-item');
                            $("#" + add_recipient_pre).removeClass('hidden-item');
                        }
                    }
                }
            });
        },
        "columns": [

            {
                "title": "收件人", "sWidth": "33%",
                render: function (data, type, row, meta) {
                    var profix = "<p class='v-left'>";
                    switch (row.actionStatus) {
                        case "SENT_SUCCESS":
                            break;
                        case "OPEN":
                            profix = "<p class='v-left success'>";
                            break;
                        case "CLICK":
                            profix = "<p class='v-left success'>";
                            break;
                        case "UNSUBSCRIBE":
                            profix = "<p class='v-left error'>";
                            break;
                        case "HARD_BOUNCE":
                            profix = "<p class='v-left error'>";
                            break;
                        case "SOFT_BOUNCE":
                            profix = "<p class='v-left error'>";
                    }
                    return profix + row.email + "</p>";
                }
            },

            {
                "title": "状 态", "sWidth": "12%",
                render: function (data, type, row, meta) {
                    var content = "";
                    var profix = "<p class='v-left'>";
                    var surfix = "</p>";
                    switch (row.actionStatus) {
                        case "SENT_SUCCESS":
                            content = "发送成功";
                            break;
                        case "OPEN":
                            content = "打开";
                            profix = "<p class='v-left success'>";
                            break;
                        case "CLICK":
                            content = "点击";
                            profix = "<p class='v-left success'>";
                            break;
                        case "UNSUBSCRIBE":
                            content = "退订";
                            profix = "<p class='v-left error'>";
                            break;
                        case "HARD_BOUNCE":
                            content = "硬退";
                            profix = "<p class='v-left error'>";
                            break;
                        case "SOFT_BOUNCE":
                            content = "软退";
                            profix = "<p class='v-left error'>";
                    }
                    return profix + content + surfix;
                }
            },
            {
                "title": "详 情", "sWidth": "55%",
                render: function (data, type, row, meta) {
                    if (row.description == "" || row.description == null) {
                        return "<p class='v-left'>-</p>";
                    }
                    return "<p class='v-left'>" + row.description + "</p>";
                }
            }
        ]
    }));

    var add_recipient_pre = "add-recipient-pre";//加入待收件人按钮id
    var add_recipient_wait = "add-recipient-wait";//等待加入待收件人id
    var export_report_pre = "export-report-pre";//导出发送报告按钮id
    var wait_export_report = "wait-export-report";//等待导出发送报告id

    //加入待收件人弹窗
    addrecipients = function () {
        var source = $("#add_recipient_group-tpl").html();
        var template = handlebars.compile(source);
        $("#" + maskReport_wapper).myAlert(
            'dialog',
            {
                'myalert_title': '加入待收件人',
                'cover_id': 'blackcover_report',
                'dialog_ok_word': '加 入',
                'dialog_ok_autoHidden': false,
                'dialog_word': template({
                    unOpen: $("#unOpen").text() == 0 ? "disabled" : "",
                    open: $("#open").text() == 0 ? "disabled" : "",
                    click: $("#click").text() == 0 ? "disabled" : "",
                    hard: $("#hard").text() == 0 ? "disabled" : "",
                    soft: $("#soft").text() == 0 ? "disabled" : ""
                }),
                'dialog_ok_callback': function () {
                    var recipient_flag = false;//收件人flag
                    var group_flag = false;//收件人组名称flag

                    //验证复选框
                    if ($("input:checkbox[name='mailRecipientActionStatus']:checked").length > 0) {
                        recipient_flag = true;
                    } else {
                        $("#recipient_signal").css('visibility', 'visible');
                        recipient_flag = false;
                    }
                    //验证收件人组名称
                    if (($("#report_group_name").val() == "") || ($("#report_group_name").val() == null) || ($("#report_group_name").val() == "请输入2~100个字符")) {
                        group_flag = false;
                        $("#group_name_signal").css('visibility', 'visible');
                    } else {
                        group_flag = true;
                    }

                    if (recipient_flag && group_flag) {
                        var taskId = $(".maskreport-info-tit").attr("taskId");
                        $("#" + add_recipient_pre).addClass('hidden-item');
                        $("#" + add_recipient_wait).removeClass('hidden-item');
                        $.ajax({
                            url: env.contextUri + "user/delivery_task/" + taskId + "/add_recipient_group",
                            data: $("#addRecipientGroup").serialize(),
                            type: "POST",
                            success: function (result) {
                                if (result.status == 200) {
                                    $("#" + maskReport_wapper).empty().hide();
                                    $("#blackcover_report").remove();
                                    var this_item = $(this);
                                    this_item.addClass('hidden-item');
                                    $("#" + add_recipient_wait).addClass('hidden-item');
                                    $("#" + add_recipient_pre).removeClass('hidden-item');
                                    $("#" + maskReport_wapper).myAlert(
                                        'alert',
                                        {
                                            'myalert_w': '450px',
                                            'myalert_h': '150px',
                                            'myalert_title': '提示',
                                            'alert_time': '1000',
                                            'alert_word': "<p class='error margin-t-20'>"
                                            + "收件人加入成功"
                                            + "</p>"
                                        }
                                    );
                                }
                            }
                        });

                    }
                }
            }
        );

        $("input:checkbox[name='recipientsGroup']").focus(function () {
            $("#recipient_signal").css('visibility', 'hidden');
        });

        $("#report_group_name").focus(function () {
            $("#group_name_signal").css('visibility', 'hidden');
        });

        placeholder_moulde("report_group_name", "请输入2~100个字符", "#bfbfbf");
    };

    //导出报告弹窗
    exportReport = function () {
        var source = $("#export_delivery_report-tpl").html();
        var template = handlebars.compile(source);
        var taskId = $(".maskreport-info-tit").attr("taskId");
        $("#" + maskReport_wapper).myAlert(
            'dialog',
            {
                'myalert_title': '导出报告',
                'cover_id': 'blackcover_report',
                'dialog_ok_word': '导 出',
                'dialog_ok_autoHidden': false,
                'dialog_word': template({
                    unOpen: $("#unOpen").text() == 0 ? "disabled" : "",
                    open: $("#open").text() == 0 ? "disabled" : "",
                    click: $("#click").text() == 0 ? "disabled" : "",
                    hard: $("#hard").text() == 0 ? "disabled" : "",
                    soft: $("#soft").text() == 0 ? "disabled" : "",
                    taskId: taskId
                }),
                'dialog_ok_callback': function () {
                    var recordType_flag = false;//选择导出报告类型flag
                    if ($("input:checkbox[name='mailRecipientActionStatus']:checked").length > 0) {
                        recordType_flag = true;
                    } else {
                        $("#record_signal").css('visibility', 'visible');
                        recordType_flag = false;
                    }
                    if (recordType_flag) {
                        $("#export").attr("action", env.contextUri+"user/delivery_task/" + taskId + "/export").submit();
                        $("#" + maskReport_wapper).empty().hide();
                        $("#blackcover_report").remove();
                    }
                }
            }
        );

        $("input:checkbox[name='recordType']").focus(function () {
            $("#record_signal").css('visibility', 'hidden');
        });
    }
});