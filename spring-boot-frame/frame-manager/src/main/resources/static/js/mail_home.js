require.config({
    paths: {
        'jquery': 'lib/jquery-1.8.3.min',
        'myAlert': 'others/myAlert/js/myAlert',
        'handlebars': 'lib/handlebars',
        'juicer': 'lib/juicer-min',
    },
    map: {
        '*': {
            'css': 'css.min'
        }
    },
    shim: {
        'myAlert': {deps: ['jquery', 'css!../js/others/myAlert/css/myalert.css']},
        'juicer': { exports: 'juicer' }
    }
});

require(['basejs', 'jquery', 'myAlert', 'handlebars', 'juicer'],
    function (basejs, jquery, myAlert,handlebars, juicer) {

        var tasks = {

            username : $('.login span.bule-orange').attr('name'),

            t:null,

            init:function(){
                var username = this.username;

                this.showRecentTask.apply(this);
                if( (!JSON.parse(localStorage.getItem('advflag'))) ||
                    (!JSON.parse(localStorage.getItem('advflag'))[username]) ){
                    clearTimeout(this.t);
                    this.t = setTimeout(this.adv.model.bind(this), 1000);
                }

                this.bindEvents();
            },

            resize : function(){
                this.adv.getLocal.apply(this);
            },

            buyPoints:function(){
                location.href = env.contextUri + "user/account/deposit";
            },

            showRecentTask:function() {
                $.get(env.contextUri + "user/delivery_task/recent_task_list",
                        {number: 5},
                    function (result) {
                    if (result.status == 200) {
                        var source, template,
                            content = result.content,
                            $maskEmpy = $("#masklist-empty"),
                            dayBeforeToday = new Date(new Date().setHours(new Date().getHours() - 24)).format("yyyy-MM-dd"),
                            dayAfterToday = new Date(new Date().setHours(new Date().getHours() + 24)).format("yyyy-MM-dd");

                        if (content.length == 0) {
                            source = $("#no_task-tpl").html();
                            template = handlebars.compile(source);
                            $maskEmpy.empty().append(template);
                        } else {
                            $maskEmpy.addClass("hidden-item");
                            source = $("#recent_task-tpl").html();
                            template = "";
                            $.each(content, function (i, item) {
                                var day = new Date(item.createdDate).format("yyyy-MM-dd"),
                                    hour = new Date(item.createdDate).format("hh:mm"),
                                    data = {},
                                    task = '';

                                switch(day){
                                    case (new Date().format("yyyy-MM-dd")):
                                        day = "今天";
                                        break;

                                    case dayBeforeToday:
                                        day = "昨天";
                                        break;
                                    case dayAfterToday:
                                        day = "明天";
                                        break;

                                    default:
                                        break;
                                }
                                // if (day == new Date().format("yyyy-MM-dd")) {
                                //     day = "今天";
                                // } else if (day == dayBeforeToday) {
                                //     day = "昨天";
                                // } else if (day == dayAfterToday) {
                                //     day = "明天";
                                // }
                                data = {
                                    name: item.name,
                                    dateForDay: day,
                                    dateForHour: hour
                                };
                                task = handlebars.compile(source);
                                template += task(data);
                            });
                            $(".masklist-hasrecord ul").empty().append(template);
                        }
                    }
                });
            },

            adv : {
                model : function(){
                    var advDom = $("#adv_temp").html(),
                        html = juicer(advDom, {});
                    $('body').append(html);
                    this.adv.getLocal.apply(this);
                },
                close : function(){
                    var context = {},
                        username = this.username;
                    context[username] = true;
                    $("#adv_bg").remove();
                    $("#adv_wapper").remove();
                    localStorage.setItem('advflag', JSON.stringify(context));
                },
                getLocal : function(){
                    var $advWapper = $("#adv_wapper"),
                        winWidth = $(window).width(),
                        winHeight = $(window).height(),
                        advWidth = $advWapper.width(),
                        advHeight = $advWapper.height(),
                        left = (winWidth - advWidth) / 2,
                        top = (winHeight - advHeight) / 2;
                    $advWapper.css({'left':left+'px', 'top':top+'px'})
                }
            },

            bindEvents:function() {
                $("#buy").on('click',
                    this.buyPoints.bind(this));

                $("#adv_tit")
                    .on('click', this.adv.model.bind(this));

                $("body")
                    .on('click',
                        '#adv_close',
                        this.adv.close.bind(this));

                $(window)
                    .on('resize' ,this.resize.bind(this));
            }
        }

        tasks.init();

    // var home = {};
    //
    // //点击购买
    // home.buyPoints = $("#buy").on({
    //     'click': function () {
    //         location.href = env.contextUri + "user/account/deposit";
    //     }
    // });
    //
    // home.showRecentTask = function () {
    //     $.get(env.contextUri + "user/delivery_task/recent_task_list", {number: 5}, function (result) {
    //         if (result.status == 200) {
    //             var source, template;
    //             var content = result.content;
    //             if (content.length == 0) {
    //                 source = $("#no_task-tpl").html();
    //                 template = handlebars.compile(source);
    //                 $("#masklist-empty").empty().append(template);
    //             } else {
    //                 $("#masklist-empty").addClass("hidden-item");
    //                 source = $("#recent_task-tpl").html();
    //                 template = "";
    //                 var dayBeforeToday = new Date(new Date().setHours(new Date().getHours() - 24)).format("yyyy-MM-dd");
    //                 var dayAfterToday = new Date(new Date().setHours(new Date().getHours() + 24)).format("yyyy-MM-dd");
    //                 $.each(content, function (i, item) {
    //                     var day = new Date(item.createdDate).format("yyyy-MM-dd");
    //                     var hour = new Date(item.createdDate).format("hh:mm");
    //                     if (day == new Date().format("yyyy-MM-dd")) {
    //                         day = "今天";
    //                     } else if (day == dayBeforeToday) {
    //                         day = "昨天";
    //                     } else if (day == dayAfterToday) {
    //                         day = "明天";
    //                     }
    //                     var data = {
    //                         name: item.name,
    //                         dateForDay: day,
    //                         dateForHour: hour
    //                     };
    //                     var task = handlebars.compile(source);
    //                     template += task(data);
    //                 });
    //                 $(".masklist-hasrecord ul").empty().append(template);
    //             }
    //         }
    //     });
    // };
    // home.showRecentTask();var home = {};
    //
    // //点击购买
    // home.buyPoints = $("#buy").on({
    //     'click': function () {
    //         location.href = env.contextUri + "user/account/deposit";
    //     }
    // });
    //
    // home.showRecentTask = function () {
    //     $.get(env.contextUri + "user/delivery_task/recent_task_list", {number: 5}, function (result) {
    //         if (result.status == 200) {
    //             var source, template;
    //             var content = result.content;
    //             if (content.length == 0) {
    //                 source = $("#no_task-tpl").html();
    //                 template = handlebars.compile(source);
    //                 $("#masklist-empty").empty().append(template);
    //             } else {
    //                 $("#masklist-empty").addClass("hidden-item");
    //                 source = $("#recent_task-tpl").html();
    //                 template = "";
    //                 var dayBeforeToday = new Date(new Date().setHours(new Date().getHours() - 24)).format("yyyy-MM-dd");
    //                 var dayAfterToday = new Date(new Date().setHours(new Date().getHours() + 24)).format("yyyy-MM-dd");
    //                 $.each(content, function (i, item) {
    //                     var day = new Date(item.createdDate).format("yyyy-MM-dd");
    //                     var hour = new Date(item.createdDate).format("hh:mm");
    //                     if (day == new Date().format("yyyy-MM-dd")) {
    //                         day = "今天";
    //                     } else if (day == dayBeforeToday) {
    //                         day = "昨天";
    //                     } else if (day == dayAfterToday) {
    //                         day = "明天";
    //                     }
    //                     var data = {
    //                         name: item.name,
    //                         dateForDay: day,
    //                         dateForHour: hour
    //                     };
    //                     var task = handlebars.compile(source);
    //                     template += task(data);
    //                 });
    //                 $(".masklist-hasrecord ul").empty().append(template);
    //             }
    //         }
    //     });
    // };
    // home.showRecentTask();
});