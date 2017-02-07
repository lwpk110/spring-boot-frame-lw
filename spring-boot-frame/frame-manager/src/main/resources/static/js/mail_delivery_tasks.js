require.config({
    paths: {
        'jquery': 'lib/jquery-1.8.3.min',
        'myAlert': 'others/myAlert/js/myAlert',
        'placeholder_moulde': 'placeholder_moulde',
        'Kalendae': 'lib/Kalendae/kalendae.standalone',
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
        'Kalendae': {
            deps: ['css!../js/lib/Kalendae/kalendae.css'],
            exports: 'Kalendae'
        },
        'datatables': {
            deps: ['jquery',
                'css!../js/lib/datatables/media/css/jquery.dataTables.css',
                'css!../js/lib/datatables/media/css/jquery.dataTables_themeroller.css'],
            exports: 'datatables'
        }
    }
});

require(['basejs', 'jquery', 'myAlert', 'Kalendae', 'placeholder_moulde', 'datatables', 'handlebars', 'dataTableParam'], function (basejs, jquery, myAlert, Kalendae, placeholder_moulde, datatables, handlebars, dataTableParam) {

    placeholder_moulde("maskname", "请输入任务名称", "#bfbfbf");

    var delivery_rollback_Dialog = "sentset_Dialog";//加入发件人弹窗id
    $('body').append("<div id='" + delivery_rollback_Dialog + "' style='display:none;'></div>");

    var taskManager = {};

    var status = {};

    status.failedId = "#send_failed_status-tpl";
    status.waittingId = "#send_waitting_status-tpl";
    status.successId = "#send_success_status-tpl";
    status.sendingId = "#send_sending_status-tpl";
    status.wattingReportId = "#send_waitting_report_status-tpl";
    status.cancelId = "#send_cancel_status-tpl";
    taskManager.fillStatus = function (entity) {
        var source, template;
        var totalFee = parseInt(entity.totalFee);
        var fee = parseInt(entity.deliveryChannel.fee);
        var data = {"total": Math.floor(totalFee / fee)};
        switch (entity.deliveryStatus) {
            case 0:
                source = $(status.sendingId).html();
                template = handlebars.compile(source);
                return template(data);
            case 1:
                source = $(status.failedId).html();
                template = handlebars.compile(source);
                return template(data);
            case 2:
                source = $(status.cancelId).html();
                template = handlebars.compile(source);
                return template(data);
            case 100:
                var scheduled = entity.scheduled;
                var now = new Date(env.now).getTime();
                if (scheduled !== true) {
                    var sentNowDate = entity.deliveryDate;
                    if (now - sentNowDate > 3600000) {
                        source = $(status.successId).html();
                        template = handlebars.compile(source);
                        return template(data);
                    } else {
                        source = $(status.wattingReportId).html();
                        template = handlebars.compile(source);
                        return template(data);
                    }
                } else {
                    var scheduledDate = entity.scheduledDate;
                    var date = new Date(scheduledDate);
                    if (now > date.getTime()) {
                        if (now - date.getTime() > 3600000) {
                            source = $(status.successId).html();
                            template = handlebars.compile(source);
                            return template(data);
                        } else {
                            source = $(status.wattingReportId).html();
                            template = handlebars.compile(source);
                            return template(data);
                        }
                    } else {
                        source = $(status.waittingId).html();
                        template = handlebars.compile(source);
                        return template(data);
                    }
                }
        }
    };

    var content = {};

    content.failedId = "#send_failed-tpl";
    content.waittingId = "#send_waitting-tpl";
    content.successId = "#send_success-tpl";
    content.sendingId = "#send_sending-tpl";
    content.wattingReportId = "#send_waitting_report-tpl";
    content.cancelId = "#send_cancel-tpl";
    content.loadingId = "#loading-tpl";
    content.noStatusId = "#no_status-tpl";
    content.serviceErrorId = "#service_error-tpl";
    content.serviceTimeoutErrorId = "#service_timeout-tpl";

    taskManager.tableReload = function () {
        taskManager.table.fnClearTable(0);
        taskManager.table.fnDraw();
    };

    taskManager.fillContent = function (entity) {
        var source, template;
        switch (entity.deliveryStatus) {
            case 0:
                source = $(content.sendingId).html();
                return handlebars.compile(source);
            case 1:
                source = $(content.failedId).html();
                template = handlebars.compile(source);
                return template({taskId: entity.id, userId: entity.user.id});
            case 2:
                source = $(content.cancelId).html();
                template = handlebars.compile(source);
                return template({taskId: entity.id});
            case 100:

                var scheduled = entity.scheduled;
                var now = new Date(env.now).getTime();
                if (scheduled !== true) {
                    var sentNowDate = entity.deliveryDate;
                    if (now - sentNowDate > 3600000) {
                        source = $(content.loadingId).html();
                        template = handlebars.compile(source);
                        return template({taskId: entity.id});
                    } else {
                        var countdown = 3600000 - (now - sentNowDate);
                        var report_time = new Date(countdown).format("mm");
                        source = $(content.wattingReportId).html();
                        template = handlebars.compile(source);
                        return template({report_time: report_time, countdown: countdown});
                    }
                } else {
                    var scheduledDate = entity.scheduledDate;
                    var date = new Date(scheduledDate);
                    if (now > date.getTime()) {
                        if (now - date.getTime() > 3600000) {
                            source = $(content.loadingId).html();
                            template = handlebars.compile(source);
                            return template({taskId: entity.id});
                        } else {
                            var countdownScheduled = 3600000 - (now - scheduledDate);
                            var report_timeScheduled = new Date(countdownScheduled).format("mm");
                            source = $(content.wattingReportId).html();
                            template = handlebars.compile(source);
                            return template({report_time: report_timeScheduled, countdown: countdownScheduled});
                        }
                    } else {
                        source = $(content.waittingId).html();
                        template = handlebars.compile(source);
                        var finalDate = date.format("yyyy") + "年" + date.format("MM") + "月" + date.format("dd") + "日" + " " + date.format("hh:mm") + " ";
                        return template({startExecute: finalDate});
                    }
                }
        }
    };

    taskManager.startTime = function () {
        $(".countdown").each(function (i, item) {
            var SysSecond = parseInt($(item).attr("countdown")) / 1000;
            var InterValObj;
            InterValObj = window.setInterval(SetRemainTime, 1000); //间隔函数，1秒执行
            function SetRemainTime() {
                if (SysSecond > 0) {
                    SysSecond = SysSecond - 1;
                    var second = Math.floor(SysSecond % 60);             // 计算秒
                    var minite = Math.floor((SysSecond / 60) % 60);      //计算分
                    $(item).text(minite + "分钟" + second + "秒");
                } else {//剩余时间小于或等于0的时候，就停止间隔函数
                    window.clearInterval(InterValObj);
                    taskManager.tableReload();
                    //这里可以添加倒计时时间为0后需要执行的事件
                }
            }
        });
    };

    var maskmanger_calendar = "maskmanger_calendar";//任务管理时间段input的id
    //日历时间段
    var maskmanger_cal = new Kalendae.Input(maskmanger_calendar, {
        months: 2,
        titleFormat: 'YYYY, MMMM',
        format: 'YYYY/MM/DD',
        direction: 'today-past',
        mode: 'range',
        subscribe: {
            'hide': function () {
                var date = this.getSelectedAsDates();
                var startDate = date[0].format("yyyy-MM-dd");
                var endDate;
                if (date.length <= 1) {
                    endDate = new Date(env.now).format("yyyy-MM-dd");
                    var regexp = new RegExp("-", "g");
                    $("#maskmanger_calendar").val(startDate.replace(regexp, "/") + " - " + new Date(env.now).format("yyyy/MM/dd"));
                } else {
                    endDate = date[1].format("yyyy-MM-dd");
                }
                $("#startDate").val(startDate);
                $("#endDate").val(endDate);
                $(".term-list-wapper").find("li").removeClass("term-act");
            }
        }
    });


    var resultTableId = "mask_status_record";	//表格的id
    //加载表格
    taskManager.table = $("#" + resultTableId).dataTable(
        $.extend({}, dataTableParam("你还没有任务~"), {
            "sAjaxSource": env.contextUri + "user/delivery_task/list",
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
                                $("#mask_status_record_paginate").addClass("hidden-item");
                                $("#mask_status_record_info").addClass("hidden-item");
                            } else {
                                $("#mask_status_record_paginate").removeClass("hidden-item");
                                $("#mask_status_record_info").removeClass("hidden-item");
                                taskManager.startTime();
                                if ($(".loading").length > 0) {
                                    taskManager.batchReport($(".loading")[0]);
                                }
                                taskManager.goRetry();
                                taskManager.goBackout();
                                taskManager.viewReport();
                                taskManager.deliveryRollback();
                            }
                        }
                    }
                });
            },
            "fnServerParams": function (aoData) {
                if ($("#maskname").val() == "请输入任务名称") {
                    $("#maskname").val("");
                }
                aoData.push({"name": "taskName", "value": $("#maskname").val()});
                var targetUserId = $("#selectedUser  option:selected").val();
                if (!targetUserId) {
                    targetUserId = $("#userId").val();
                }
                aoData.push({"name": "targetUserId", "value": targetUserId});
                if ($("#startDate").val()) {
                    var startWeekDate = new Date(env.now);
                    var startMonthDate = new Date(env.now);
                    startMonthDate.setMonth(startMonthDate.getMonth() - 1);
                    startWeekDate.setHours(startWeekDate.getHours() - (7 * 24));
                    if ($("#startDate").val() == startWeekDate.format("yyyy-MM-dd")) {
                        $(".term-list-wapper ul li").first().removeClass("term-act").next().addClass("term-act").next().removeClass("term-act");
                    } else if ($("#startDate").val() == startMonthDate.format("yyyy-MM-dd")) {
                        $(".term-list-wapper ul li").first().removeClass("term-act").next().removeClass("term-act").next().addClass("term-act");
                    }
                    aoData.push({"name": "startDate", "value": $("#startDate").val()});
                } else {
                    $(".term-list-wapper ul li").first().addClass("term-act").next().removeClass("term-act").next().removeClass("term-act");
                }
                if ($("#endDate").val()) {
                    aoData.push({"name": "endDate", "value": $("#endDate").val()});
                }
            },
            "columns": [

                {
                    "title": "时 间", "sWidth": "12%", "aTargets": [0], "sClass": "th-center-col",
                    render: function (data, type, row, meta) {
                        var createdDate = new Date(row.createdDate);
                        return "<p class='v-middle'>" + createdDate.format("yyyy-MM-dd") + "</p>"
                            + "<p class='grey-color v-middle'>" + createdDate.format("hh:mm") + "</p>";
                    }
                },

                {
                    "title": "任务名称/发送账号", "sWidth": "28%", "aTargets": [1],
                    render: function (data, type, row, meta) {
                        return "<p class='v-left'>" + row.name + "</p>"
                            + "<p class='grey-color v-left tr-singal-line'>" + row.user.username + "</p>";
                    }
                },

                {
                    "title": "总数/状态", "sWidth": "13.5%", "sClass": "th-center-col",
                    render: function (data, type, row, meta) {
                        return taskManager.fillStatus(row);
                    }
                },

                {
                    "title": "<div class='col-xs-4 float-l'>成功</div><div class='col-xs-4 float-l'>打开</div><div class='col-xs-4 float-l'>点击</div>",
                    "sWidth": "28%",
                    render: function (data, type, row, meta) {
                        return taskManager.fillContent(row);
                    }
                },

                {
                    "title": "通道/消费", "sWidth": "10%", "sClass": "th-center-col",
                    render: function (data, type, row, meta) {
                        return "<p class='grey-color v-middle'>" + row.deliveryChannel.name + "</p>"
                            + "<p class='grey-color v-middle'>" + row.totalFee + "点</p>";
                    }
                },

                {
                    "title": "", "sWidth": "8.5%",
                    render: function (data, type, row, meta) {
                        var source, template;
                        switch (row.deliveryStatus) {
                            case 1:
                                var targetUserId = $("#userId").val();
                                if (row.user.id == targetUserId) {
                                    source = $("#send_backout-tpl").html();
                                    template = handlebars.compile(source);
                                    return template({taskId: row.id});
                                } else {
                                    return "";
                                }
                            case 100:
                                var scheduled = row.scheduled;
                                var now = new Date(env.now).getTime();
                                if (scheduled !== true) {
                                    var sentNowDate = row.deliveryDate;
                                    if (now - sentNowDate > 3600000) {
                                        source = $("#send_view-tpl").html();
                                        template = handlebars.compile(source);
                                        return template({taskId: row.id});
                                    }
                                } else {
                                    var scheduledDate = row.scheduledDate;
                                    var date = new Date(scheduledDate);
                                    if (now > date.getTime()) {
                                        if (now - date.getTime() > 3600000) {
                                            source = $("#send_view-tpl").html();
                                            template = handlebars.compile(source);
                                            return template({taskId: row.id});
                                        }
                                    }
                                }
                                return "";
                            default:
                                return "";
                        }
                    }
                }
            ]

        }));

    taskManager.search = $("#search").on({
        'click': function () {
            var date = $("#maskmanger_calendar").val();
            if (date == "自定义查询时间段" || date == "") {
                $("#startDate").val(null);
                $("#endDate").val(null);
            } else {
                var dateArray = date.split(" - ");
                var startDate = dateArray[0];
                var endDate = dateArray[1];
                var regexp = new RegExp("/", "g");
                $("#startDate").val(startDate.replace(regexp, "-"));
                $("#endDate").val(endDate.replace(regexp, "-"));
            }
            taskManager.tableReload();
        }
    }).parent().prev().find("input").on({
        'keydown': function (event) {
            if (event.keyCode == 13) {
                $("#search").click();
            }
        }
    });

    taskManager.selectUser = $("#selectedUser").on({
        'change': function () {
            taskManager.tableReload();
        }
    });

    taskManager.searchByTime = $(".term-list-wapper ul li").first().on({
        'click': function () {
            var lis = $(this).parent().find("li");

            $.each(lis, function (i, item) {
                $(item).removeClass("term-act");
            });
            $(this).addClass("term-act");
            $("#maskmanger_calendar").val("自定义查询时间段");
            $("#startDate").val(null);
            $("#endDate").val(null);
            taskManager.tableReload();
        }
    }).next().on({
        'click': function () {
            var lis = $(this).parent().find("li");

            $.each(lis, function (i, item) {
                $(item).removeClass("term-act");
            });
            $(this).addClass("term-act");
            $("#maskmanger_calendar").val("自定义查询时间段");
            var startDate = new Date(env.now);
            startDate.setHours(startDate.getHours() - (7 * 24));
            var startDateFormatted = startDate.format("yyyy-MM-dd");
            $("#startDate").val(startDateFormatted);
            var endDate = new Date(env.now);
            endDate.setHours(endDate.getHours() + 24);
            var endDateFormatted = endDate.format("yyyy-MM-dd");
            $("#endDate").val(endDateFormatted);
            taskManager.tableReload();
        }
    }).next().on({
        'click': function () {
            var lis = $(this).parent().find("li");

            $.each(lis, function (i, item) {
                $(item).removeClass("term-act");
            });
            $(this).addClass("term-act");
            $("#maskmanger_calendar").val("自定义查询时间段");
            var startDate = new Date(env.now);
            startDate.setMonth(startDate.getMonth() - 1);
            var startDateFormatted = startDate.format("yyyy-MM-dd");
            $("#startDate").val(startDateFormatted);
            var endDate = new Date(env.now);
            endDate.setHours(endDate.getHours() + 24);
            var endDateFormatted = endDate.format("yyyy-MM-dd");
            $("#endDate").val(endDateFormatted);
            taskManager.tableReload();
        }
    });

    var jsonReportCount = 1;
    taskManager.batchReport = function (item) {
        var taskId = $(item).attr("taskId");
        $.ajax({
            url: "delivery_task/" + taskId + "/json_report",
            dataType: "get",
            headers: {
                Accept: "application/json; charset=utf-8"
            },
            complete: function (XMLHttpRequest, textStatus) {
                var source, template, html;
                $(item).empty().removeClass("report-loading");
                if (textStatus == "timeout") {
                    source = $(content.serviceTimeoutErrorId).html();
                    template = handlebars.compile(source);
                    html = template();
                    $(item).append(html);
                }
                if (XMLHttpRequest.status == 200) {
                    var result = XMLHttpRequest.responseText;
                    var jsonObj = $.parseJSON(result);
                    var report = jsonObj.content;
                    var data = {
                        success: report.sent - report.hardBounce - report.softBounce,
                        success_percent: report.total == 0 ? 0 : parseInt((report.sent - report.hardBounce - report.softBounce) / report.total * 100).toFixed(2),
                        /*open: report.openCount,
                         open_percent: parseInt(report.openCount / report.total * 100).toFixed(2),
                         click: report.allClicked,
                         click_percent: parseInt(report.allClicked / report.total * 100).toFixed(2),*/
                        unique_open: report.uniqueOpenCount,
                        unique_open_percent: report.total == 0 ? 0 : parseInt(report.uniqueOpenCount / report.total * 100).toFixed(2),
                        unique_click: report.mailClicked,
                        unique_click_percent: report.total == 0 ? 0 : parseInt(report.mailClicked / report.total * 100).toFixed(2)
                    };
                    source = $(content.successId).html();
                    template = handlebars.compile(source);
                    html = template(data);
                    $(item).append(html);
                } else if (XMLHttpRequest.status == 500) {
                    source = $(content.serviceErrorId).html();
                    template = handlebars.compile(source);
                    html = template();
                    $(item).append(html);
                }
                var loadings = $(".loading");
                if (loadings.length > jsonReportCount) {
                    taskManager.batchReport(loadings[jsonReportCount]);
                    jsonReportCount++;
                } else {
                    jsonReportCount = 1;
                }
            }
        });
    };

    taskManager.goRetry = function () {
        $(".retry").on({
            'click': function () {
                var id = $(this).attr("taskId");
                var userId = $(this).attr("userId");
                var targetUserId = $("#selectedUser  option:selected").val();
                if (userId == targetUserId) {
                    $(this).prev().text("系统繁忙，任务发送失败，点此");
                    $(this).text("重发");
                    $.get("delivery_task/" + id + "/retry", function (result) {
                        if (result.status == 200) {
                            taskManager.tableReload();
                        }
                    });
                } else {
                    $(this).prev().text("系统繁忙，任务发送失败。");
                    $(this).text("");
                }
            }
        });
    };

    taskManager.goBackout = function () {
        $(".backout").on({
            'click': function () {
                var id = $(this).attr("taskId");
                var source = $("#send_backout_dialog-tpl").html();
                var template = handlebars.compile(source);
                $("#" + delivery_rollback_Dialog).myAlert(
                    'dialog',
                    {
                        'myalert_title': '撤销任务',
                        'cover_id': 'blackcover_senter',
                        'dialog_ok_word': '确 定',
                        'dialog_ok_autoHidden': false,
                        'dialog_word': template(),
                        'dialog_ok_callback': function () {
                            $.get("delivery_task/" + id + "/cancel", function (result) {
                                if (result.status = 200) {
                                    $("#" + delivery_rollback_Dialog).empty().hide();
                                    $("#blackcover_senter").remove();
                                    $("#" + delivery_rollback_Dialog).myAlert(
                                        'alert',
                                        {
                                            'myalert_w': '450px',
                                            'myalert_h': '150px',
                                            'myalert_title': '提示',
                                            'alert_time': '1000',
                                            'alert_word': "<p class='error margin-t-20'>"
                                            + "任务撤销成功"
                                            + "</p>",
                                            alert_callback: function () {
                                                taskManager.tableReload();
                                            }
                                        }
                                    );

                                }
                            });
                        }
                    }
                );

            }
        });
    };

    taskManager.viewReport = function () {
        $('.view').on({
            'click': function () {
                var id = $(this).attr("taskId");
                location.open
                window.open("delivery_task/" + id + "/report", "_black");
            }
        });
    };

    taskManager.deliveryRollback = function () {
        $(".deliveryRollback").on({
            'click': function () {
                var source = $("#add_recipient_group-tpl").html();
                var template = handlebars.compile(source);
                var id = $(this).attr("taskId");

                $("#" + delivery_rollback_Dialog).myAlert(
                    'dialog',
                    {
                        'myalert_title': '加入待收件人',
                        'cover_id': 'blackcover_senter',
                        'dialog_ok_word': '保 存',
                        'dialog_ok_autoHidden': false,
                        'dialog_word': template(),
                        'dialog_ok_callback': function () {
                            var group_flag = false;//收件人组名称flag
                            if (($("#report_group_name").val() == "") || ($("#report_group_name").val() == null) || ($("#report_group_name").val() == "请输入2~100个字符")) {
                                group_flag = false;
                                $("#group_name_signal").css('visibility', 'visible');
                            } else {
                                group_flag = true;
                            }
                            if (group_flag) {
                                $.post(env.contextUri + "user/delivery_task/" + id + "/delivery_rollback", {name: $("#report_group_name").val()}, function (result) {
                                    if (result.status == 200) {
                                        $("#" + delivery_rollback_Dialog).empty().hide();
                                        $("#blackcover_senter").remove();
                                        $("#" + delivery_rollback_Dialog).myAlert(
                                            'alert',
                                            {
                                                'myalert_w': '450px',
                                                'myalert_h': '150px',
                                                'myalert_title': '提示',
                                                'alert_time': '1000',
                                                'alert_word': "<p class='error margin-t-20'>"
                                                + "添加收件人成功"
                                                + "</p>",
                                                alert_callback: function () {
                                                }
                                            }
                                        )
                                    }
                                });
                            }
                        }
                    }
                );
            }
        });
    };
});
