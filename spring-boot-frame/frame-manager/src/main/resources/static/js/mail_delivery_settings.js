require.config({
    paths : {
        'basejs':'basejs',
        'jquery': 'lib/jquery-1.8.3.min',
        'myAlert': 'others/myAlert/js/myAlert',
        'handlebars': 'lib/handlebars',
        'placeholder_moulde':'placeholder_moulde'
    },
    map: {
        '*': {
            'css': 'css.min'
        }
    },
    shim: {
        'myAlert': {deps : ['jquery','css!others/myAlert/css/myalert.css']},
        'handlebars' : {exports: 'handlebars'}
    }
});


require(['basejs','jquery','myAlert','placeholder_moulde','handlebars'],function(basejs,jquery,myAlert,placeholder_moulde,handlebars){

    var sendSetting={
        init:function(){
            $(".default-senter").parents('tr').siblings('tr').find('td:last-child i.fa-trash')
                .parent('div').removeClass('hidden-item');
            this.bindEvents();
        },

        ie8PlaceHolder:function(id,text){
            placeholder_moulde(id,text,"#bfbfbf");
        },

        reloadWeb:function(){
            location.reload();
        },

        showAlertDialog:function(title,text,func){
            var source = $("#alert_dialog_content").html(),
                template = handlebars.compile(source),
                context = {"signal":text},
                html = template(context);

            $("#sentset_Dialog").myAlert(
                'alert',
                {
                    'myalert_w': '450px',
                    'myalert_h': '150px',
                    'myalert_title':title,
                    'alert_time':'1000',
                    'alert_word': html,
                    'alert_callback': function () {
                        if(func !== undefined){
                            func();
                        }
                    }
                }
            );
        },

        hideDialogSignal:function(e){
            var k;
            for(k in e.data){
                $("#"+e.data[k]).addClass('hidden-item');
            };
        },

        hideDialog:function(){
            $("#sentset_Dialog").empty().hide();
            $("#blackcover_senter").remove();
        },

        // 验证规则
        validRule:{

            //验证是否为空
            nullContent:function(val,placeHolder){
                var reg = /^\s+$/g;
                if( !val || val == placeHolder || reg.test(val)){
                    return false;
                }else{
                    return true;
                };
            },

            //验证邮箱格式
            emailAddress:function(val){
                var regexp = "^(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*[0-9a-zA-Z-]+))@([a-zA-Z0-9-]+[.])",
                    emailRegexp = new RegExp(regexp, "g");

                if( emailRegexp.test(val) ){
                    return true;
                }else{
                    return false;
                }
            },

            // 验证最大长度
            maxLength:function(val,max){
                var len = val.length,
                    flag = false;

                flag = ( len > max ) ?  false :  true;

                return flag;
            },

            // 验证最小长度
            minLength:function(val,min){
                var len = val.length,
                    flag = false;

                flag = ( len < min ) ?  false :  true;

                return flag;
            }
        },
        getVal:function(ID){
            var formVal = $("#"+ID).val();
            //var formVal = {"nickName": $("#nickName").val(), "achievEmail":$("#achievEmail").val(), "sendEmail" : $("#sendEmail").val() };
            return formVal;
        },

        deleteS:{

            //删除发件人
            sender:function(e){
                var $thisItem = $(e.target).closest('i'),
                    trId = $thisItem.closest('tr').attr('id'),
                    source = $("#delete_content").html(),
                    template = handlebars.compile(source),
                    context = {"warning":"删除发件人将无法恢复", "signal":"确认删除发件人。"},
                    html = template(context);

                $("#sentset_Dialog").myAlert(
                    'dialog',
                    {
                        'myalert_title':'删除确认',
                        'cover_id':'blackcover_senter',
                        'myalert_h': '280px',
                        'dialog_ok_word':'确 定',
                        'dialog_word':html,
                        'dialog_ok_callback':function(){
                            $.ajax({
                                "url":"delivery_settings/"+trId+"/delete",
                                "data":{
                                    "id":trId
                                },
                                "type": "POST",
                                "success":function(result){
                                    this.showAlertDialog( "提示", "发件人删除成功！",this.reloadWeb );
                                }.bind(sendSetting)
                            });
                        }
                    }
                );

            }
        },

        editor:{

            // 验证
            valid:function(){
                var nickNameVal = $("#nickName").val(), $nickNameSingal = $("#nickName_singal"),
                    achievEmailVal = $("#achievEmail").val(), $achievEmailSingal = $("#achievEmail_singal"),
                    sendEmailVal = $("#sendEmail").val(), sendEmail_singal = $("#sendEmail_singal"),
                    nickFlag = false, achievEmailFlag = false, sendEmailFlag = false,
                    flag = false;

                // 昵称
                if( !this.validRule.nullContent(nickNameVal,"你发出的所有邮件中，发件人将显示的昵称") ){
                    $nickNameSingal.children('span').empty().append("昵称不能为空！");
                    this.ie8PlaceHolder("nickName","你发出的所有邮件中，发件人将显示的昵称");
                    $nickNameSingal.removeClass('hidden-item');
                    nickFlag = false;
                }else if( !this.validRule.minLength(nickNameVal,2) || !this.validRule.maxLength(nickNameVal,20) ){
                    $nickNameSingal.children('span').empty().append("昵称过短或过长！");
                    this.ie8PlaceHolder("nickName","你发出的所有邮件中，发件人将显示的昵称");
                    $nickNameSingal.removeClass('hidden-item');
                    nickFlag = false;
                }else{
                    nickFlag = true;
                };

                // 收信箱
                if( !this.validRule.nullContent(achievEmailVal,"对方收到你的邮件后，回复给你的邮箱地址") ){
                    $achievEmailSingal.children('span').empty().append("邮箱不能为空！");
                    $achievEmailSingal.removeClass('hidden-item');
                    this.ie8PlaceHolder("achievEmail","对方收到你的邮件后，回复给你的邮箱地址");
                    achievEmailFlag = false;
                }else if( !this.validRule.minLength(achievEmailVal,2) ){
                    $achievEmailSingal.children('span').empty().append("邮箱长度不正确！");
                    $achievEmailSingal.removeClass('hidden-item');
                    this.ie8PlaceHolder("achievEmail","对方收到你的邮件后，回复给你的邮箱地址");
                    achievEmailFlag = false;
                }else{
                    if( !this.validRule.emailAddress(achievEmailVal) ){
                        $achievEmailSingal.children('span').empty().append("输入正确的邮箱格式！");
                        $achievEmailSingal.removeClass('hidden-item');
                        this.ie8PlaceHolder("achievEmail","对方收到你的邮件后，回复给你的邮箱地址");
                        achievEmailFlag = false
                    }else{
                        achievEmailFlag = true;
                    }
                };

                // 发信箱
                if( $("#sendEmail_wapper").hasClass('hidden-item') ){
                    sendEmailFlag = true;
                }else{
                    if( !this.validRule.nullContent(sendEmailVal,"请填写虚拟邮箱，没有虚拟邮箱请开启发送优化") ){
                        sendEmail_singal.children('span').empty().append("邮箱不能为空！");
                        sendEmail_singal.removeClass('hidden-item');
                        this.ie8PlaceHolder("sendEmail","请填写虚拟邮箱，没有虚拟邮箱请开启发送优化");
                        sendEmailFlag = false;
                    }else if( !this.validRule.minLength(sendEmailVal,2) ){
                        sendEmail_singal.children('span').empty().append("邮箱长度不正确！");
                        sendEmail_singal.removeClass('hidden-item');
                        this.ie8PlaceHolder("sendEmail","请填写虚拟邮箱，没有虚拟邮箱请开启发送优化");
                        sendEmailFlag = false;
                    }else{
                        if( !this.validRule.emailAddress(sendEmailVal) ){
                            sendEmail_singal.children('span').empty().append("输入正确的邮箱格式！");
                            sendEmail_singal.removeClass('hidden-item');
                            this.ie8PlaceHolder("sendEmail","请填写虚拟邮箱，没有虚拟邮箱请开启发送优化");
                            sendEmailFlag = false
                        }else{
                            sendEmailFlag = true;
                        }
                    };
                }

                flag = ( nickFlag  && achievEmailFlag && sendEmailFlag ) ? true : false ;

                return flag;
            },

            // 加载弹框
            loadDialog:function(trId){
                var  url = "delivery_settings/" + trId + "/edit";
                var data = {};
                var source = $("#editor_content").html(),
                    template = handlebars.compile(source),
                    context = {/*"nickName":nickName, "achievEmail":achievEmail,"sendEmail":sendEmail*/},
                    html = null,
                    validResult = false;
                $.ajax({
                    "url": url,
                    "type": "GET",
                    "success": function (result) {
                        data.senter = result.content.deliverySettings.senderName;
                        data.replyEmail = result.content.deliverySettings.replyEmail;
                        data.senderEmail = result.content.deliverySettings.senderEmail;
                        data.agentSend = result.content.deliverySettings.agentSend;
                        dialogHight=( data.agentSend) ? '280px' : '320px',
                        context = {"senderName":data.senter, "replyEmail":data.replyEmail,"senderEmail":data.senderEmail}
                        html = template(context)
                        $("#sentset_Dialog").myAlert(
                            'dialog',
                            {
                                'myalert_title':'编辑发件人',
                                'cover_id':'blackcover_senter',
                                'myalert_h': dialogHight,
                                'dialog_ok_autoHidden':false,
                                'dialog_ok_word':'保存',
                                'dialog_word':html,
                                'dialog_ok_callback':function(){
                                    var senderName = this.getVal("nickName"),
                                        senderEmail = this.getVal("sendEmail"),
                                        replyEmail = this.getVal("achievEmail"),
                                        options={};
                                    if(data.agentSend){
                                        options = {
                                            "deliverySettings.senderName" : senderName,
                                            "deliverySettings.replyEmail" : replyEmail
                                        };
                                    }else{
                                        options = {
                                            "deliverySettings.senderName" : senderName,
                                            "deliverySettings.replyEmail" : replyEmail,
                                            "deliverySettings.senderEmail" : senderEmail
                                        };
                                    };

                                    console.log(senderName+";"+senderEmail+";"+replyEmail);
                                    validResult = this.editor.valid.bind(this)();
                                    if( validResult ){
                                        this.hideDialog();
                                        $.ajax({
                                            "url":url,
                                            "data":options,
                                            "type": "POST",
                                            "success":function(result){
                                                this.showAlertDialog( "提示", "发件人保存成功！",this.reloadWeb );
                                            }.bind(sendSetting)
                                        });
                                    }
                                }.bind(sendSetting)
                            }
                        );

                        if(data.agentSend){
                            $("#sendEmail_wapper").addClass('hidden-item')
                        }else{
                            $("#sendEmail_wapper").removeClass('hidden-item')
                        };
                    }.bind(sendSetting)


                });


            },

            // 编辑发件人
            setting:function(e){
                var $thisItem = $(e.target).closest('i'),
                    trId = $thisItem.closest('tr').attr('id');

                this.editor.loadDialog.call(sendSetting,trId);
            }
        },

        revise:{

            // 修改默认发件人
            sender:function(e){
                var $thisItem = $(e.target).closest('i'),
                     trId = $thisItem.closest('tr').attr('id');
                $.ajax({
                    url: "delivery_settings/" + trId + "/default_delivery_change",
                    type: "GET",
                    success:function(result){

                        $thisItem.addClass('default-senter');

                        $thisItem.closest('tr').find('td:last-child i.fa-trash')
                            .parent('div').addClass('hidden-item');

                        $thisItem.closest('tr').siblings('tr')
                            .find('td:first-child i').removeClass('default-senter');

                        $thisItem.closest('tr').siblings('tr').find('td:last-child i.fa-trash')
                            .parent('div').removeClass('hidden-item');

                        this.showAlertDialog( "提示", "默认发件人修改成功！",this.reloadWeb );

                    }.bind(this)
                })
            }
        },

        addS:{

            //选择邮箱类型
            chooseType:function(e){
                var $thisItem = $(e.target).closest('li'),
                    itemIndex = $thisItem.index();

                $thisItem.addClass('active').siblings('li').removeClass('active');

                switch(itemIndex){
                    case 0:
                        $("#sendEmail_wapper").addClass('hidden-item');
                        $("#hidden_defaultSendBox").val("1");
                        break;

                    case 1:
                        $("#sendEmail_wapper").removeClass('hidden-item');
                        $("#hidden_defaultSendBox").val("0");
                        break;
                }
            },

            // 验证
            valid:function(){
                var $nickName = $("#nickname"), $nickNameSignal = $("#nickname_singal"),
                    $sendEmail = $("#sendEmail"), $sendEmailSignal = $("#sendEmail_singal"),
                    $achieveEmail = $("#achiveEmail"), $achieveEmailSignal = $("#achiveEmail_singal"),
                    nickNameVal = $nickName.val(),
                    sendEmailVal = $sendEmail.val(),
                    achieveEmailVal = $achieveEmail.val(),
                    nickFlag = false, sendFlag = false, achieveFlag = false,
                    flag = false;

                // 昵称
                if( !this.validRule.nullContent(nickNameVal, "你发出的所有邮件中，发件人将显示的昵称") ){
                    $nickNameSignal.children('span').empty().append("昵称不能为空！");
                    $nickNameSignal.removeClass('hidden-item');
                    nickFlag = false ;
                }else if( !this.validRule.minLength(nickNameVal,2) || !this.validRule.maxLength(nickNameVal,20) ){
                    $nickNameSignal.children('span').empty().append("昵称过短或过长！");
                    $nickNameSignal.removeClass('hidden-item');
                    nickFlag = false ;
                }else{
                    nickFlag = true ;
                };

                // 收信箱
                if( !this.validRule.nullContent(achieveEmailVal, "你的邮箱账号，例如：yunyoutong@tendata.com") ){
                    $achieveEmailSignal.children('span').empty().append("邮箱不能为空！");
                    $achieveEmailSignal.removeClass('hidden-item');
                    achieveFlag = false ;
                }else if( !this.validRule.minLength(achieveEmailVal,2) ){
                    $achieveEmailSignal.children('span').empty().append("邮箱长度不正确！");
                    $achieveEmailSignal.removeClass('hidden-item');
                    achieveFlag = false ;
                }else{
                    if( !this.validRule.emailAddress(achieveEmailVal) ){
                        $achieveEmailSignal.children('span').empty().append("输入正确的邮箱！");
                        $achieveEmailSignal.removeClass('hidden-item');
                        achieveFlag = false ;
                    }else{
                        achieveFlag = true ;
                    };
                };

                // 发信箱
                if( $("#sendEmail_wapper").hasClass('hidden-item') ){
                    sendFlag = true;
                }else{
                    if( !this.validRule.nullContent(sendEmailVal, "你的邮箱账号，例如：yunyoutong@tendata.com") ){
                        $sendEmailSignal.children('span').empty().append("邮箱不能为空！");
                        $sendEmailSignal.removeClass('hidden-item');
                        sendFlag = false ;
                    }else if( !this.validRule.minLength(sendEmailVal,2) ){
                        $sendEmailSignal.children('span').empty().append("邮箱长度不正确！");
                        $sendEmailSignal.removeClass('hidden-item');
                        sendFlag = false ;
                    }else{
                        if( !this.validRule.emailAddress(sendEmailVal) ){
                            $sendEmailSignal.children('span').empty().append("输入正确的邮箱！");
                            $sendEmailSignal.removeClass('hidden-item');
                            sendFlag = false ;
                        }else{
                            sendFlag = true ;
                        };
                    };
                };

                flag = ( nickFlag && sendFlag && achieveFlag ) ? true : false;

                return flag;
            },

            // 加载表格
            loadDialog:function(){
                var source = $("#add_content").html(),
                    template = handlebars.compile(source),
                    context = {},
                    html = template(context),
                    validResult = false;

                $("#sentset_Dialog").myAlert(
                    'dialog',
                    {
                        'myalert_title':'添加发件人',
                        'cover_id':'blackcover_senter',
                        'myalert_h': '415px',
                        'dialog_ok_autoHidden':false,
                        'dialog_ok_word':'保存',
                        'dialog_word':html,
                        'dialog_ok_callback':function(){
                            var nickname = this.getVal("nickname"),
                                sendEmail = this.getVal("sendEmail"),
                                achiveEmail = this.getVal("achiveEmail"),
                                agentSend = this.getVal("hidden_defaultSendBox");
                            validResult = this.addS.valid.bind(this)();
                            if( validResult ){
                                this.hideDialog();
                                $.ajax({
                                    "url":"delivery_settings/add",
                                    "data":{
                                        "deliverySettings.senderName" : nickname,
                                        "deliverySettings.senderEmail" :sendEmail,
                                        "deliverySettings.replyEmail" : achiveEmail,
                                        "deliverySettings.agentSend":agentSend
                                    },
                                    "type": "POST",
                                    "success":function(result){
                                        this.showAlertDialog( "提示", "发件人保存成功！",this.reloadWeb );
                                    }.bind(sendSetting)
                                });
                            }
                        }.bind(sendSetting)
                    }
                );

                this.ie8PlaceHolder("nickname","你发出的所有邮件中，发件人将显示的昵称");
                this.ie8PlaceHolder("sendEmail","你的邮箱账号，例如：yunyoutong@tendata.com");
                this.ie8PlaceHolder("achiveEmail","你的邮箱账号，例如：yunyoutong@tendata.com");
            },


            // 解释定位
            localExplain:function(){
                var explainWidth = $("#explain_wapper").width(),
                    texWidth = $("#explain_text").width(),
                    textLeft = $("#explain_text").offset().left - $("#sentset_Dialog").offset().left,
                    textTop = $("#explain_text").offset().top - $("#sentset_Dialog").offset().top,
                    subWidth = (explainWidth - texWidth)/2;

                $("#explain_wapper").css({left : textLeft - subWidth, top:textTop+20});

            },

            // 隐藏解释
            hideExplain:function(){
                $("#explain_wapper").addClass('hidden-item');
            },

            // 显示解释
            showExplain:function(){
                this.addS.localExplain();
                $("#explain_wapper").removeClass('hidden-item');
            },

            // 添加发件人
            sender:function(){
                this.addS.loadDialog.call(sendSetting);
            }
        },

        bindEvents:function(){

            // 编辑发件人
            $("#sentset_table>tbody")
                .on( 'click'," td i.fa-pencil-square",this.editor.setting.bind(this) );

            // 编辑发件人隐藏提示
            $("#sentset_Dialog")
                .on(
                'focus',
                "#nickName,#achievEmail,#sendEmail",
                {"id0":"nickName_singal", "id1":"achievEmail_singal", "id2":"sendEmail_singal"},
                this.hideDialogSignal.bind(this)
            );

            // 删除发件人
            $("#sentset_table>tbody")
                .on( 'click'," td i.fa-trash",this.deleteS.sender.bind(this) );

            //修改默认发件人
            $("#sentset_table>tbody")
                .on( 'click'," tr td:first-child i",this.revise.sender.bind(this) );

            // 添加发件人
            $("#add_sender").on( 'click',this.addS.sender.bind(this) );

            // 添加发件人选择邮箱类型
            $("#sentset_Dialog").on( 'click','#choose_type>li',this.addS.chooseType.bind(this) );

            // 添加发件人隐藏提示
            $("#sentset_Dialog")
                .on(
                'focus',
                "#nickname,#sendEmail,#achiveEmail",
                {"id0":"nickname_singal", "id1":"sendEmail_singal", "id2":"achiveEmail_singal"},
                this.hideDialogSignal.bind(this)
            );

            // 添加发件人显示解释
            $("#sentset_Dialog").on( 'mouseover',"#explain_text",this.addS.showExplain.bind(this) )

            // 添加发件人隐藏解释虚拟邮箱
            $("#sentset_Dialog").on( 'mouseout',"#explain_text",this.addS.hideExplain.bind(this) )
        }

    };

    sendSetting.init();

});