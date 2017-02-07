require.config({
    paths: {
        'jquery': 'lib/jquery-1.8.3.min',
        'myAlert': 'others/myAlert/js/myAlert',
        'placeholder': 'placeholder_moulde',
        'loading': 'loading_moudle',
        'kalendae': 'lib/Kalendae/kalendae.standalone',
        'handlebars': 'lib/handlebars',
        'he': 'lib/he',
        'ckeditor': 'lib/ckeditor/ckeditor'
    },
    map: {
        '*': {
            'css': 'css.min'
        }
    },
    shim: {
        'Kalendae': {
            exports: 'Kalendae'
        },
        'myAlert': {deps: ['jquery', 'css!../js/others/myAlert/css/myalert.css']},
        'ckeditor': {deps: ['jquery']}
    }
});

require(['basejs', 'jquery', 'myAlert', 'placeholder', 'loading', 'kalendae', 'handlebars', 'he', 'ckeditor'], function (basejs, jquery, myAlert, placeholder, loading, kalendae, handlebars, he, ckeditor) {
    require(["css!../js/lib/Kalendae/kalendae.css"]);
    $('body').append("<div id='mask_new_wapper' style='display:none;'></div>");
    var tasks = {
        init: function () {
            this.redrawHeight();
            this.bindEvents();
            this.fillRemainLetters();
            this.initCkeditor();
        },
        bindEvents: function () {
            $('#sender_select').on('click', this.renderDelivery.bind(this));
            $('#sender_select_list').on('click', 'li', this.deliverySelect.bind(this));

            $('#pipe_select_list>li').on('click', function(){
                var thisItem=$(this);
                thisItem.addClass('active').siblings('li').removeClass('active');
                var fee = $('#pipe_select_list li.active').children('span').attr("fee");
                var banance = $("#remainBalance").text();
                $("#remainLettersCount").text(Math.floor(banance / fee));

            });

            $('.j-recipient-btn').on('click', this.renderRecipient.bind(this));
            $('#recipients_list').on('click', 'li', this.recipientSelect.bind(this));
            $('#recipient-selected').on('click', this.recipientSelected.bind(this));

            $('.j-template-btn').on('click', this.renderMailTemplate.bind(this));
            $('#email_temp_list').on('click', 'li', this.templateSelect.bind(this));
            $('#template-selected').on('click', this.templateSelected.bind(this));

            $('#send-now').on('click', this.sendNow.bind(this));
            $('#send-cron').on('click', this.sendCron.bind(this));
            $('body').on('click', this.closeSelect.bind(this));
        },
        initCkeditor: function () {
            CKEDITOR.replace("myEditor", {height: 700, readOnly:true});
        },
        hideToolbar: function () {
            $("#cke_myEditor").css({"border": "0px", "box-shadow": "none"});
            $("#cke_1_top").removeClass("cke_top").addClass("hidden-item");
            $("#cke_2_top").removeClass("cke_top").addClass("hidden-item");
            $("#cke_1_bottom").removeClass("cke_bottom").addClass("hidden-item");
            $("#cke_2_bottom").removeClass("cke_bottom").addClass("hidden-item");
        },
        renderDelivery: function (event) {
            if (event.stopPropagation) {//防止冒泡
                // this code is for Mozilla and Opera
                event.stopPropagation();
            } else if (window.event) {
                // this code is for IE
                window.event.cancelBubble = true;
            }
            $('#sender_select_list').removeClass('hidden-item');
        },
        renderChannel: function (event) {
            if (event.stopPropagation) {//防止冒泡
                // this code is for Mozilla and Opera
                event.stopPropagation();
            } else if (window.event) {
                // this code is for IE
                window.event.cancelBubble = true;
            }
        },
        renderRecipient: function () {
            $('#recipients_list_wapper').animate({right: '-0%'});
        },
        renderMailTemplate: function () {
            $('#temp_wapper').css('height', '800px');
            $('#temp_sel_area').animate({top: '0px'});
            $('#temp_list_wapper').animate({right: '0px'});
            this.hideToolbar();
        },
        deliverySelect: function (e) {
            var $li = $(e.target).closest('li');
            $li.addClass('sender-select-active').siblings('li').removeClass('sender-select-active');
            $('#sender_select').text($li.remove('i').text());
            this.destroy();
        },
        recipientSelect: function (e) {
            var $li = $(e.target).closest('li');
            //$li.toggleClass('recipients-list-active');
            var class_flag = $li.hasClass('recipients-list-active');
            if (!class_flag) {
                $li.addClass('recipients-list-active');
                $("#recipient-selected").show();
            } else {
                $li.removeClass('recipients-list-active');
                var activeNum = $("#recipients_list>li.recipients-list-active").length;
                if (activeNum == 0) {
                    $("#recipient-selected").hide();
                }
            }
        },
        recipientSelected: function (e) {
            if ($('#recipients_list li.recipients-list-active').length <= 0) {
                $('#mask_new_wapper').myAlert(
                    'alert',
                    {
                        'myalert_w': '450px',
                        'myalert_h': '150px',
                        'myalert_title': '提示',
                        'alert_time': '1000',
                        'alert_word': "<p class='error margin-t-20'>"
                        + "请选择收件人组"
                        + "</p>"
                    }
                );
            } else {
                $('#recipients-sured').removeClass('hidden-item');
                $('#recipients-notsured').addClass('hidden-item');
                //清空原先显示在页面中的收件人列表
                $('#recipients-sured').find('div.recipients-sured-list ul').empty();
                var recipientGroupIds = [];
                $('#recipients_list li.recipients-list-active').each(function (i) {
                    var this_list = $(this);
                    var this_val = this_list.find("div.recipients-list-selinfo h1").text();
                    var val = this_list.children('input').val();
                    recipientGroupIds.push(val);
                    $('#recipients-sured').find("div.recipients-sured-list ul").append("<li data-val='" + val + "'>" + this_val + "</li>");
                });
                this.destroyRecipient();
                this.redrawHeight();
                $.post("recipients_count", {"rgId[]": recipientGroupIds}, function (result) {
                    if (result.status == 200) {
                        var retains = result.content.retains;
                        $("#selectedRecipients").text(retains);
                        $("#allSelectedRecipients").text(result.content.total);
                        var fee = $('#pipe_select_list li.active').children('span').attr("fee");
                        var balance = $("#remainBalance").text();
                        var intRetains = parseInt(retains), intFee = parseInt(fee), intBalance = parseInt(balance);
                        if ($("#no_channel_reminder").length == 0) {
                            $("#checkBalance").parent().removeClass("hidden-item");
                            if (intRetains * intFee > intBalance) {
                                $("#checkBalance").removeClass("hidden-item").prev().addClass("hidden-item");
                                $("#send-now").addClass("disable-button").attr("disabled", "disabled");
                                $("#send-cron").addClass("disable-button").attr("disabled", "disabled");
                            } else {
                                $("#checkBalance").addClass("hidden-item").prev().removeClass("hidden-item");
                                $("#send-now").removeClass("disable-button").removeAttr("disabled");
                                $("#send-cron").removeClass("disable-button").removeAttr("disabled");
                            }
                        } else {
                            $("#checkBalance").parent().addClass("hidden-item");
                        }
                    }
                });
            }
        },
        templateSelect: function (e) {
            var $li, tpl, title;
            $li = $(e.target).closest('li');
            $li.addClass('temp-list-active').siblings('li').removeClass('temp-list-active');
            tpl = $li.children('input.j-tpl').val();
            title = $li.children('span').attr("templateSubject");
            CKEDITOR.tools.callFunction(9, this);
            CKEDITOR.instances.myEditor.setData(he.unescape(tpl));
            //$('.wysiwyg-frame').contents().find('body').empty().html(he.unescape(tpl));
            $('.wysiwyg-title').val(title);
            $('.wysiwyg-title').attr("readonly","readonly");
            $("#template-selected").show();
        },
        templateSelected: function (e) {
            if ($('#email_temp_list li.temp-list-active').length <= 0) {
                $('#mask_new_wapper').myAlert(
                    'alert',
                    {
                        'myalert_w': '450px',
                        'myalert_h': '150px',
                        'myalert_title': '提示',
                        'alert_time': '1000',
                        'alert_word': "<p class='error margin-t-20'>"
                        + "请选择邮件模板"
                        + "</p>"
                    }
                );
            } else {
                var $li = $('#email_temp_list li.temp-list-active');
                var name = $li.children('span').attr("title");
                var val = $li.children('input').val();
                $('#email_temp_notsure').addClass('hidden-item');
                $('#email_temp_sure').removeClass('hidden-item');
                $('#email_temp_sure ul').empty().append("<li data-val='" + val + "'>" + name + "</li>");
                this.destroyTemplate();
            }
            ;
        },
        redrawHeight: function () {
            var recipients_height = $("#recipients_height").height() + 30 - 45;
            $("#recipients_list_wapper div.recipients-list-content").css('height', recipients_height);
        },
        validate: function () {
            var maskName_flag = false;
            var maskNameLength_flag = false;
            var senter_flag = false;
            var Recipients_flag = false;
            var emailTemp_flag = false;
            var template_flag = false;
            var template_title_flag = false;
            var template_title_length_flag = false;
            var status_arr = new Array();
            var signal_arr = ["请填写任务名称", "请填写2-100个字符的收件人", "请设置发件人", "请选择收件人", "请选择邮件模板", "邮件模板不能为空", "请填写邮件标题", "请填写2-100个字符的邮件标题"];
            //验证任务名称
            if (($('#mask_tit_input').val() == "") || ($('#mask_tit_input').val() == null) || ($('#mask_tit_input').val() == "请输入2~100个字符")) {
                maskName_flag = false;
            } else {
                maskName_flag = true;
            }
            ;
            status_arr.push(maskName_flag);

            //验证任务名称长度
            if ($('#mask_tit_input').val().length > 100 || $('#mask_tit_input').val().length < 2) {
                maskNameLength_flag = false;
            } else {
                maskNameLength_flag = true;
            }
            ;
            status_arr.push(maskNameLength_flag);

            //验证发件人
            if ($('#sender_select_list li').hasClass('sender-select-active')) {
                senter_flag = true;
            } else {
                senter_flag = false;
            }
            ;
            status_arr.push(senter_flag);

            //验证收件人
            if ($('#recipients-sured ul li').length <= 0) {
                Recipients_flag = false;
            } else {
                Recipients_flag = true;
            }
            status_arr.push(Recipients_flag);

            //验证邮件模板
            if ($('#email_temp_sure ul li').length <= 0) {
                emailTemp_flag = false;
            } else {
                emailTemp_flag = true;
            }
            ;

            status_arr.push(emailTemp_flag);

            //验证模板内容
            if (CKEDITOR.instances.myEditor.getData() != "") {
                template_flag = true;
            } else {
                template_flag = false;
            }

            status_arr.push(template_flag);

            if ($(".wysiwyg-title").val() != "") {
                template_title_flag = true;
            } else {
                template_title_flag = false;
            }
            status_arr.push(template_title_flag);

            if (1 < $(".wysiwyg-title").val().length && $(".wysiwyg-title").val().length < 99) {
                template_title_length_flag = true;
            } else {
                template_title_length_flag = false;
            }

            status_arr.push(template_title_length_flag);

            if (maskName_flag && senter_flag && Recipients_flag && emailTemp_flag && template_flag && template_title_flag && template_title_length_flag) {
                this.bindFormParam();
                return true;
            } else {//如果验证没有全部通过
                var i = 0;
                for (i; i < status_arr.length; i++) {
                    if (status_arr[i] == false) {
                        break
                    }
                    ;
                }
                ;
                $('#mask_new_wapper').myAlert(
                    'alert',
                    {
                        'myalert_w': '450px',
                        'myalert_h': '150px',
                        'myalert_title': '提示',
                        'alert_time': '1000',
                        'alert_word': "<p class='error margin-t-20'>"
                        + signal_arr[i]
                        + "</p>"
                    }
                );
                return false;
            }
            ;
        },
        bindFormParam: function () {
            this.taskName = $('#mask_tit_input').val();
            this.taskDelivery = $('#sender_select_list li.sender-select-active').children('input').val();
            this.taskChannel = $('#pipe_select_list li.active').children('input').val();
            this.taskRecipient = [];
            var callback = (function (taskRecipient) {
                return function (i) {
                    taskRecipient.push($(this).attr('data-val'));
                }
            })(this.taskRecipient);
            $('#recipients-sured ul li').each(callback);
            this.taskTemplate = $('#email_temp_sure ul li').attr('data-val');
        },
        sendNow: function () {
            if (this.validate()) { //验证通过
                var callback = (function (scope) {
                    return function (data) {
                        data.scheduled = false;
                        var source = $('#send-now-template').html();
                        var template = handlebars.compile(source);
                        var html = template(data);
                        var callback = (function (scope) {
                            return function () {
                                loading();
                                $.ajax({
                                    url : env.contextUri + "user/delivery_task/submit_task",
                                    type : 'post',
                                    data: {
                                        "mailTemplate.body": CKEDITOR.instances.myEditor.getData(),
                                        "mailTemplate.subject": $(".wysiwyg-title").val(),
                                        "mailTemplate.html": true,
                                        name: scope.taskName,
                                        userMailDeliverySettings: scope.taskDelivery,
                                        deliveryChannel: scope.taskChannel,
                                        "userMailRecipientGroups": scope.taskRecipient,
                                        userMailTemplate: scope.taskTemplate,
                                        scheduled: false
                                    },
                                    traditional:true,
                                    success:function(data){
                                        location.href = env.contextUri + "user/delivery_task/create_success";
                                    }
                                });
                            }
                        })(scope);
                        $('#mask_new_wapper').myAlert(
                            'dialog',
                            {
                                'myalert_title': '发送任务',
                                'cover_id': 'blackcover_advice',
                                'myalert_h': '300px',
                                'dialog_ok_word': '确 定',
                                'dialog_word': html,
                                'dialog_ok_callback': callback
                            }
                        );
                    };
                })(this);
                $.get(env.contextUri + "user/recipient_group/jsonRecipientGroupReport", {
                    rgId: this.taskRecipient,
                    cId: this.taskChannel
                }, callback);
            }
        },
        sendCron: function () {
            if (this.validate()) { //验证通过
                $("#task-date").val((new Date()).toLocaleDateString());

                var callback = (function (scope) {
                    return function (data) {
                        var defaultYear = new Date().format("yyyy");
                        var defaultMonth = new Date().format("MM");
                        var defaultDay = new Date().format("dd");
                        var defaultHour = new Date().format("hh");
                        var defaultMinutes = new Date().format("mm");

                        var defaultDate = defaultYear + "/" + defaultMonth + "/" + defaultDay;
                        data.scheduled = true;
                        data.defaultDate = defaultDate;
                        var source = $('#send-now-template').html();
                        var template = handlebars.compile(source);
                        var html = template(data);

                        var validateSchedule = function(nowDate ,scheduleDate) {
                          return scheduleDate < nowDate ;
                        };

                        $('#mask_new_wapper').myAlert(
                            'dialog',
                            {
                                'myalert_title': '定时发送',
                                'cover_id': 'blackcover_advice',
                                'dialog_ok_word': '确 定',
                                'dialog_word': html,
                                'dialog_ok_callback': function () {
                                  var date = $("#task-date").val();
                                  var hour = $("#task-hour").val();
                                  var min = $("#task-min").val();
                                  var minute_threshold = 15;
                                  var datetime = date + "T" + hour + ":" + min;
                                  var scheduleDate = date.replace(new RegExp("-","g"),"/") + "T" + hour + ":" + min;
                                  var nowDate = defaultDate + "T" + defaultHour + ":" +
                                      (Number(defaultMinutes) + Number(minute_threshold));
                                    if(validateSchedule(nowDate,scheduleDate)) {
                                        $('#mask_new_wapper').myAlert(
                                            'alert',
                                            {
                                                'myalert_w': '450px',
                                                'myalert_h': '150px',
                                                'myalert_title': '提示',
                                                'alert_time': '1000',
                                                'alert_word': "<p class='error margin-t-20'>"
                                                + "定时发送时间必须大于现在时间15分钟！"
                                                + "</p>"
                                            }
                                        );
                                        return false;
                                    }

                                    $.ajax({
                                        url : env.contextUri + "user/delivery_task/submit_task",
                                        type : 'post',
                                        data: {
                                            "mailTemplate.body": CKEDITOR.instances.myEditor.getData(),
                                            "mailTemplate.subject": $(".wysiwyg-title").val(),
                                            "mailTemplate.html": true,
                                            name: scope.taskName,
                                            userMailDeliverySettings: scope.taskDelivery,
                                            deliveryChannel: scope.taskChannel,
                                            "userMailRecipientGroups": scope.taskRecipient,
                                            userMailTemplate: scope.taskTemplate,
                                            scheduledDate: datetime,
                                            scheduled: true
                                        },
                                        traditional:true,
                                        success:function(data){
                                            location.href = env.contextUri + "user/delivery_task/create_success";
                                        }
                                    });
                                }
                            }
                        );
                        //加载日期控件
                        var k4 = new Kalendae.Input('sent_date', {
                            months: 1,
                            titleFormat: 'YYYY, MMMM',
                            format: 'YYYY-MM-DD',
                            direction: 'today-future',
                            subscribe: {
                                'change': function () {
                                    var date = this.getSelected();
                                    $("#show_date").text(date);
                                    $('#task-date').val(date);
                                }
                            }
                        });
                        //选择小时时
                        $("#sel_hour").change(function () {
                            var hour_val = $(this).find("option:selected").text().substr(0, 2);
                            $("#show_hour").text(hour_val);
                            $('#task-hour').val(hour_val);
                        });

                        //选择分钟
                        $("#sel_min").change(function () {
                            var min_val = $(this).find("option:selected").text().substr(0, 2);
                            $("#show_min").text(min_val);
                            $('#task-min').val(min_val);
                        });
                    };
                })(this);
                $.get(env.contextUri + "user/recipient_group/jsonRecipientGroupReport", {
                    rgId: this.taskRecipient,
                    cId: this.taskChannel
                }, callback);
            }
        },
        destroy: function () {
            $('#sender_select_list').addClass('hidden-item');
        },
        destroyRecipient: function () {
            $('#sender_select_list').addClass('hidden-item');
            $('#recipients_list_wapper').animate({right: '-100%'});
        },
        destroyTemplate: function () {
            $('#sender_select_list').addClass('hidden-item');
            $('#temp_list_wapper').animate({right: '-331px'});
        },
        fillRemainLetters: function () {
            var fee = $('#pipe_select_list li.active').children('span').attr("fee");
            var banance = $("#remainBalance").text();
            $("#remainLettersCount").text(Math.floor(banance / fee));
        },
        closeSelect: function (event) {
            if (!$('#sender_select_list').hasClass('hidden-item')) {
                $('#sender_select_list').addClass('hidden-item');
            }
        }
    };
    tasks.init();
});