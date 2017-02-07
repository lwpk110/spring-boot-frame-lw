require.config({
    paths: {
        'jquery': 'lib/jquery-1.8.3.min',
        'IEselecter': 'others/selectivizr',
        'respond': 'others/respond.min',
        'dateFormat': 'lib/date.format',
        'functionEx': 'lib/function.ex',
        'mail': 'mail',
        'ajaxErrorHandler': 'ajaxErrorHandler_moudle',
        'jqueryValidate': 'lib/jquery.validate.min',
        'statistics': 'statistics',
        'ie8Bind':'others/ie8-bind'
    },
    shim: {
        'IEselecter': {deps: ['jquery']},
        'ajaxErrorHandler': {deps: ['jquery']},
        'jqueryValidate': {deps: ['jquery']}
    }
});

require(['jquery', 'dateFormat', 'functionEx', 'mail', 'ajaxErrorHandler', 'jqueryValidate', 'statistics','ie8Bind'], function (jquery, dateFormat, functionEx, mail, ajaxErrorHandler, jqueryValidate, statistics,ie8Bind) {

    if ($.browser.msie && ( ($.browser.version == "7.0") || ($.browser.version == "8.0") || ($.browser.version == "9.0"))) {
        require(['IEselecter', 'respond'])
    }

    Mail.defaults.action = "http://tendata.mailsou.com/it/";

    $(".youjian").click(function () {
        var url = $(this).attr('url');
        Mail.login(env.userId, env.password, url)
    });

    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        try {
            xhr.setRequestHeader(header, token);
        }
        catch (err) {
        }
    });

    $.ajaxSetup({
        "timeout": 30000
    });

});