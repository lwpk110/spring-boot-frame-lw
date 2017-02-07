require.config({
    paths: {
        'jquery': 'lib/jquery-1.8.3.min',
        'myAlert': 'others/myAlert/js/myAlert',
        'placeholder_moulde': 'placeholder_moulde',
        'datatables': 'lib/datatables/media/js/jquery.dataTables',
        'ckeditor': 'lib/ckeditor/ckeditor',
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
        },
        'ckeditor': {deps: ['jquery']}
    }
});

require(['basejs', 'jquery', 'myAlert', 'placeholder_moulde', 'datatables', 'ckeditor', 'dataTableParam'], function (basejs, jquery, myAlert, placeholder_moulde, datatables, ckeditor, dataTableParam) {

    var resultTableId = "email_temp_record";	//表格的id

    //加载表格
    var table = $("#" + resultTableId).dataTable($.extend({}, dataTableParam("你还没有模板~"), {
        "sAjaxSource": env.contextUri + "user/template/list",
        "iDisplayLength": 10,
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
                            $("#email_temp_record_paginate").addClass("hidden-item");
                            $("#email_temp_record_info").addClass("hidden-item");
                        } else {
                            $("#email_temp_record_paginate").removeClass("hidden-item");
                            $("#email_temp_record_info").removeClass("hidden-item");
                        }
                    }
                }
            });
        },
        "columns": [

            {
                "title": "时 间", "sWidth": "20%",
                render: function (data, type, row, meta) {
                    var createdDate = new Date(row.createdDate);
                    return "<p class='v-left'>" + createdDate.format("yyyy-MM-dd") + "<span class='grey-font'> " + createdDate.format("hh:mm") + "</span></p>";
                }
            },

            {
                "title": "模板名称", "sWidth": "42%",
                render: function (data, type, row, meta) {
                    return "<p class='tr-singal-line'>" + row.name + "</p>";
                }
            },

            {
                "title": "使用次数", "sWidth": "9%",
                render: function (data, type, row, meta) {
                    return "<p>" + row.useCount + "</p>";
                }
            },
            {
                "title": "审核状态", "sWidth": "9%",
                render: function (data, type, row, meta) {
                    var html="";
                    switch (row.approveStatus){
                        case 6:
                            html = "<p  class='error'>" + row.approveStatusName  + "</p>";
                            break;
                        //case 4: case 5:
                        //    html = "<p  class='success'>" + row.approveStatusName  + "</p>";
                        //    break;
                        default :
                            console.log(row)
                            html = "<p >" + row.approveStatusName  + "</p>";
                            break;
                    }
                    return html;
                }
            },

            {
                "title": " ", "sWidth": "20%",
                render: function (data, type, row, meta) {
                    return "<div class='col-xs-4 td-action v-middle'>"
                        + "<i class='fa fa-eye' title='预览' onclick=previewTmp('" + row.id + "')>" + "</i>"
                        + "</div>"
                        + "<div class='col-xs-4 td-action v-middle'>"
                        + "<i class='fa fa-pencil-square' title='编辑' onclick= window.open('template/'+'" + row.id + "'+'/edit','_self')>" + "</i>"
                        + "</div>"
                        + "<div class='col-xs-4 td-action v-middle'>"
                        + "<i class='fa fa-trash' title='删除' onclick=deleteTemp('" + row.id + "')>" + "</i>"
                        + "</div>"
                }
            }
        ]
    }));

    previewTmp = function (id) {
        window.open("template/" + id + "/preview", "_blank");
    };

    deleteTemp = function (id) {
        var delete_Dialog = "delete_Dialog";
        $('body').append("<div id='" + delete_Dialog + "' style='display:none;'></div>");
        $("#" + delete_Dialog).myAlert(
            'dialog',
            {
                'myalert_title': '删除确认',
                'cover_id': 'blackcover_senter',
                'myalert_h': '280px',
                'dialog_ok_word': '确 定',
                'dialog_word': "<p class='grey-font'>删除模板将无法恢复</p>"
                + "<p class='error margin-t-40'>确认删除模板。</p>",
                'dialog_ok_callback': function () {
                    $.ajax({
                        "url": "template/" + id + "/delete",
                        "type": "POST",
                        "success": function (result) {
                            if (result.status = 200) {
                                $("#" + delete_Dialog).myAlert(
                                    'alert',
                                    {
                                        'myalert_w': '450px',
                                        'myalert_h': '150px',
                                        'myalert_title': '提示',
                                        'alert_time': '1000',
                                        'alert_word': "<p class='error margin-t-20'>"
                                        + "删除模板成功"
                                        + "</p>",
                                        alert_callback: function () {
                                            table.fnClearTable(0);
                                            table.fnDraw();
                                        }
                                    }
                                )
                            }
                        }
                    });
                }
            }
        );
    }
});