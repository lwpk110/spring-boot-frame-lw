require.config({
    paths: {
        'jquery': 'lib/jquery-1.8.3.min',
        'myAlert': 'others/myAlert/js/myAlert',
        'Kalendae': 'lib/Kalendae/kalendae.standalone',
        'datatables': 'lib/datatables/media/js/jquery.dataTables',
        'dataTableParam': 'datatable_param_moudle'
    },
    map: {
        '*': {
            'css': 'css.min'
        }
    },
    shim: {
        'myAlert': {
            deps: ['jquery', 'css!../js/others/myAlert/css/myalert.css']
        },
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

require(['basejs', 'jquery', 'myAlert', 'datatables', 'Kalendae', 'dataTableParam'], function (basejs, jquery, myAlert, datatables, Kalendae, dataTableParam) {
    //加载表格
    var dt = $("#buy-record-detail").dataTable($.extend({}, dataTableParam("你还没有流水记录~"), {
        "sAjaxSource": env.contextUri + "user/account/deposits",
        "fnServerData": function (sSource, aoData, fnCallback, oSettings) {
            oSettings.jqXHR = $.ajax({
                "type": "GET",
                "dataType": "json",
                "url": sSource,
                timeout: 20000,
                "data": aoData,
                "success": function (e) {
                    if (typeof(e) == "object") {
                        fnCallback(e);
                        if (e.iTotalDisplayRecords == 0) {
                            $("#buy-record-detail_paginate").addClass("hidden-item");
                            $("#buy-record-detail_info").addClass("hidden-item");
                        } else {
                            $("#buy-record-detail_paginate").removeClass("hidden-item");
                            $("#buy-record-detail_info").removeClass("hidden-item");
                        }
                    }
                }
            });
        },
        "fnServerParams": function (aoData) {
            if ($("#startDate").val()) {
                aoData.push({
                    "name": "startDate",
                    "value": $("#startDate").val()
                });
            }
            if ($("#endDate").val()) {
                aoData.push({
                    "name": "endDate",
                    "value": $("#endDate").val()
                });
            }
        },
        "columns": [
            {
                "title": "时 间",
                "width": "20%",
                "data": "createdDate",
                "render": function (data, type, full, meta) {
                    var createdDate = new Date(data);
                    return "<span class='v-left'>" + createdDate.format("yyyy-MM-dd") + "</span>  " + "<span class='grey-color v-left'>" + createdDate.format("hh:mm:ss") + "</span>";
                }
            },
            {
                "title": "类 型",
                "width": "20%",
                "data": "transactionType",
                "render": function (data, type, full, meta) {
                    var val;
                    switch (data) {
                        case "DEPOSIT":
                            val = "购买";
                            break;
                        case "BALANCE_ASSIGN":
                            val = "分配";
                            break;
                        case "SEND":
                            val = "发送任务";
                            break;
                        case "SEND_CANCEL":
                            val = "取消任务";
                            break;

                    }
                    return val;
                }
            },
            {
                "title": "点 数",
                "width": "15%",
                "data": "credits"
            },
            {
                "title": "备 注",
                "width": "45%",
                "data": "comment"
            }
        ]

    }));

    function tableReload() {
        dt.fnClearTable(0);
        dt.fnDraw();
    };

    $(".term-list-wapper ul li").first().on({
        'click': function () {
            var lis = $(this).parent().find("li");

            $.each(lis, function (i, item) {
                $(item).removeClass("term-act");
            });
            $(this).addClass("term-act");
            $("#buyrecord_calendar").val("自定义查询时间段");
            $("#startDate").val(null);
            $("#endDate").val(null);
            $(".term-list-wapper ul li")
            tableReload();
        }
    }).next().on({
        'click': function () {
            var lis = $(this).parent().find("li");

            $.each(lis, function (i, item) {
                $(item).removeClass("term-act");
            });
            $(this).addClass("term-act");
            $("#buyrecord_calendar").val("自定义查询时间段");
            var startDate = new Date();
            startDate.setHours(startDate.getHours() - (7 * 24));
            var startDateFormatted = startDate.format("yyyy-MM-dd");
            $("#startDate").val(startDateFormatted);
            var endDate = new Date();
            endDate.setHours(endDate.getHours() + 24);
            var endDateFormatted = endDate.format("yyyy-MM-dd");
            $("#endDate").val(endDateFormatted);
            tableReload();
        }
    }).next().on({
        'click': function () {
            var lis = $(this).parent().find("li");

            $.each(lis, function (i, item) {
                $(item).removeClass("term-act");
            });
            $(this).addClass("term-act");
            $("#buyrecord_calendar").val("自定义查询时间段");
            var startDate = new Date();
            startDate.setMonth(startDate.getMonth() - 1);
            var startDateFormatted = startDate.format("yyyy-MM-dd");
            $("#startDate").val(startDateFormatted);
            var endDate = new Date();
            endDate.setHours(endDate.getHours() + 24);
            var endDateFormatted = endDate.format("yyyy-MM-dd");
            $("#endDate").val(endDateFormatted);
            tableReload();
        }
    });

    $("#search").click(function () {
        tableReload();
    });

    //日历时间段
    var maskmanger_cal = new Kalendae.Input("buyrecord_calendar", {
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
                    endDate = new Date().format("yyyy-MM-dd");
                    var regexp = new RegExp("-", "g");
                    $("#buyrecord_calendar").val(startDate.replace(regexp, "/") + " - " + new Date().format("yyyy/MM/dd"));
                } else {
                    endDate = date[1].format("yyyy-MM-dd");
                }
                $("#startDate").val(startDate);
                $("#endDate").val(endDate);
                $(".term-list-wapper").find("li").removeClass("term-act");
            }
        }
    });

});