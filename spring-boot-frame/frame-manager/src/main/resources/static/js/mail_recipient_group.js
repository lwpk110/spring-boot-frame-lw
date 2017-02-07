require.config({
    paths: {
        'jquery': 'lib/jquery-1.8.3.min',
        'myAlert': 'others/myAlert/js/myAlert'
    },
    map: {
        '*': {
            'css': 'css.min'
        }
    },
    shim: {
        'myAlert': {deps: ['jquery', 'css!../js/others/myAlert/css/myalert.css']}
    }
});

require(['basejs', 'jquery', 'myAlert'], function (basejs, jquery, myAlert) {
    var recipients_Dialog = "recipients_Dialog";
    $(".goPreview").on({
        'click': function () {
            var groupId = $(this).attr("recipientGroupId");
            window.open(env.contextUri + "user/recipient_group/" + groupId + "/preview", "_black");
        }
    });
    $('body').append("<div id='" + recipients_Dialog + "' style='display:none;'></div>")
    $(".delete").on({
        'click': function () {
            var id = $(this).attr("id");
            $("#recipients_Dialog").myAlert(
                'dialog',
                {
                    'myalert_title': '删除确认',
                    'cover_id': 'blackcover_senter',
                    'myalert_h': '280px',
                    'dialog_ok_word': '确 定',
                    'dialog_word': "<p class='grey-font'>删除收件人组将无法恢复</p>"
                    + "<p class='error margin-t-40'>确认删除收件人组。</p>",
                    'dialog_ok_callback': function () {
                        $.ajax({
                            "url": "recipient_group/" + id + "/delete",
                            "type": "POST",
                            "success": function (result) {
                                if (result.status = 200) {
                                    $("#" + recipients_Dialog).myAlert(
                                        'alert',
                                        {
                                            'myalert_w': '450px',
                                            'myalert_h': '150px',
                                            'myalert_title': '提示',
                                            'alert_time': '1000',
                                            'alert_word': "<p class='error margin-t-20'>"
                                            + "删除收件人成功"
                                            + "</p>",
                                            alert_callback: function () {
                                                location.reload();
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
});