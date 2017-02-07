require.config({
    paths: {
        'jquery': 'lib/jquery-1.8.3.min',
        'myAlert': 'others/myAlert/js/myAlert',
        'placeholder_moulde': 'placeholder_moulde',
        'handlebars': 'lib/handlebars'
    },
    map: {
        '*': {
            'css': 'css.min'
        }
    },
    shim: {
        'myAlert': {
            deps: ['jquery', 'css!../js/others/myAlert/css/myalert.css']
        }
    }
});

require(['basejs','jquery', 'myAlert', 'placeholder_moulde', 'handlebars'], function (basejs, jquery, myAlert, placeholder_moulde, handlebars) {

    //弹框id
    var child_dotsDialog_id = "child_dotsDialog_id";
    var child_dotsAlert_id = "child_dotsAlert_id";

    $("body").append("<div style='display:none;' id='" + child_dotsDialog_id + "'></div>");
    $("body").append("<div style='display:none;' id='" + child_dotsAlert_id + "'></div>");

    //点击分配
    $("#child_account_wapper div.child-account div.child-account-item").on("click", function () {
        var this_item = $(this);
        //可分配点数
        var total_free_dots = $("#free_dots_total").text();
        var childName = this_item.find("div.child-name").children('span').text();
        var childHasDats = this_item.find('div.even-have-dots').children('span').text();
        var userId = $(this).find("input").val();
        var source = $("#dialog_content").html();
        var template = handlebars.compile(source);
        var context = {
            userId: userId,
            childName: "",
            accountDots: childHasDats
        };
        //输入子账号name
        context.childName = childName;
        var html = template(context);
        $("#" + child_dotsDialog_id).myAlert(
            'dialog', {
                'myalert_title': '点数分配',
                'cover_id': 'blackcover_dots',
                'myalert_h': '300px',
                'dialog_ok_word': '确定',
                'dialog_ok_autoHidden': false,
                'dialog_word': html,
                'dialog_ok_callback': function () {
                    //初始点数
                    var old_dot_val = context.accountDots;
                    //修改完的点数
                    var dot_val = $("#free_dots_input").val();

                    if ((dot_val == "") || (dot_val == null) || (dot_val == "请输入子账户的点数")) {
                        $("#free_dots_singal").removeClass("hidden-item");
                        placeholder_moulde("free_dots_input", "请输入子账户的点数", "#bfbfbf");
                    } else {
                        //扣除增加可分配点数
                        changeFreeDots(total_free_dots, old_dot_val, dot_val);
                        //隐藏弹窗
                        //hidden_dialog();
                    }
                }
            }
        );

        //获得焦点隐藏提示
        $("#free_dots_input").focus(function () {
            $("#free_dots_singal").addClass("hidden-item");
        });

        //只能输入正整数
        $("#free_dots_input").on('keyup', function () {
            var dot_val = $(this).val();
            if (dot_val.match('[^0-9\]', '')) {
                var FilterVal = dot_val.substring(0, dot_val.length - 1);
                $(this).val(FilterVal);
            }
        });
    });

    //隐藏弹窗
    hidden_dialog = function () {
        $("#blackcover_dots").remove();
        $("#" + child_dotsDialog_id).empty();
    };

    //扣除增加可分配点数
    changeFreeDots = function (total_dots, old_val, new_val) {
        var change_val = parseInt(new_val) - parseInt(old_val);
        var new_total_dots = 0;
        var status_val = ["点数分配成功", "余额不足，点数分配失败"];
        var flag = 1; //分配状态，0成功、1余额不足失败

        new_total_dots = parseInt(total_dots) - change_val;

        if ((-new_total_dots) > parseInt(total_dots)) { //超出可分配余额
            flag = 1;
        } else {
            $("#free_dots_total").text(new_total_dots);
            flag = 0;
        }
        if (flag == 0) {
            var userId = $("#user-id").val();
            var credits = $("#free_dots_input").val();
            $.post(env.contextUri + "user/account/child_users/" + userId + "/assign_balance", {
                    credits: credits
                },
                function (data) {
                    hidden_dialog();
                    $("#child_dotsAlert_id").myAlert(
                        'alert', {
                            'myalert_w': '450px',
                            'myalert_h': '150px',
                            'myalert_title': '提示',
                            'alert_time': '1000',
                            'alert_word': "<p class='error margin-t-20'>点数分配成功</p>",
                            'alert_callback': function () {
                                location.reload();
                            }
                        }
                    );
                }, "json");
        } else if (flag == 1) {
            hidden_dialog();
            $("#child_dotsAlert_id").myAlert(
                'alert', {
                    'myalert_w': '450px',
                    'myalert_h': '150px',
                    'myalert_title': '提示',
                    'alert_time': '1000',
                    'alert_word': "<p class='error margin-t-20'>余额不足，点数分配失败</p>"
                }
            );
        }
    };
});