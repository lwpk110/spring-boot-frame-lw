require.config({
    path: {
        jquery: 'lib/jquery/jquery.min',
        'myAlert': 'others/myAlert/js/myAlert'
    },
    shim: {
        "myAlert": {deps: ['jquery']}
    }
});
define(['jquery', 'myAlert'], function (jquery, myAlert) {

    var handler = {};

    handler.createDialog = function (id) {
        $('body').append("<div id='" + id + "' style='display:none;'></div>");
    };

    handler.showDialog = function (id, word, callback) {
        $("#" + id).myAlert(
            'alert',
            {
                'myalert_w': '450px',
                'myalert_h': '150px',
                'myalert_title': '提示',
                'alert_time': '1000',
                'alert_word': "<p class='error margin-t-20'>"
                + word
                + "</p>",
                'alert_callback': callback
            });
    };

    handler.handle = function () {
        $(document).ajaxComplete(function (event, xhr, settings) {
            if (xhr.statusText == "timeout") {
                var _timeout_Dialog = "_timeout_Dialog";
                $("#loading_img,#loading_cover").remove();//loading消失
                if ($("#" + _timeout_Dialog).length == 0) {
                    handler.createDialog(_timeout_Dialog);
                    handler.showDialog(_timeout_Dialog, "请求超时,请稍后再试！", function () {
                        location.reload();
                    });
                }
                return;
            }
            var text = xhr.responseText;
            try {
                var obj = JSON.parse(text);
                switch (xhr.status) {
                    case 400:
                        var _400_Dialog = "_400_Dialog";
                        handler.createDialog(_400_Dialog);
                        if (obj.errors) {
                            handler.showDialog(_400_Dialog, obj.message + ":" + obj.errors[0].message, function () {
                            });
                        } else {
                            handler.showDialog(_400_Dialog, obj.message, function () {
                            });
                        }
                        break;
                    case 200:
                        if (!obj.success) {
                            var status = obj.status;
                            if (status == "102") {
                                var _102_Dialog = "_102_Dialog";
                                if ($("#" + _102_Dialog).length == 0) {
                                    handler.createDialog(_102_Dialog);
                                    handler.showDialog(_102_Dialog, "您已在别处登录,一秒后将返回登录页面!", function () {
                                        location.href = env.contextUri + "user/";
                                    });
                                }
                            }
                        }
                        break;
                }
            } catch (e) {
            }

        });
    };

    handler.handle();
});