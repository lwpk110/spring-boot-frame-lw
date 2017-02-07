/// <reference path="../typings/jquery/jquery.d.ts" />

require.config({
    paths : {
        'jquery': 'lib/jquery-1.8.3.min',
        'myAlert': 'others/myAlert/js/myAlert',
        'placeholder_moulde':'placeholder_moulde',
        'insertAtCaret':'others/insertAtCaret',
        'ckeditor':'lib/ckeditor/ckeditor',
        'juicer': 'lib/juicer-min',
        'validRule':'validRule_moulde'
    },
    map: {
        '*': {
            'css': 'css.min'
        }
    },
    shim: {
        'myAlert': {deps : ['jquery','css!others/myAlert/css/myalert.css']},
        'insertAtCaret': {deps : ['jquery']},
        'ckeditor': {deps : ['jquery']},
        'juicer': { exports: 'juicer' }
    }   
}); 

require(['basejs', 'jquery', 'myAlert', 'placeholder_moulde', 'insertAtCaret', 'ckeditor', 'juicer', 'validRule'],
    function(basejs, jquery, myAlert, placeholder_moulde, insertAtCaret, ckeditor, juicer, validRule){
    var tasks = {
        
        init:function(){
            
            // 替换文本编辑器
            var editor=CKEDITOR.replace("myEditor",{
                height:500, 
                templateCallback: this.insertTemp.model.bind(this)
            });
            
            this.getTpl();
            placeholder_moulde("Temp_name","请输入2~100个字符","#bfbfbf");
            placeholder_moulde("email_name","请输入2~100个字符","#bfbfbf");

            this.bindEvents();
        },
        
        // 编辑模板时获取原模板信息
        getTpl:function(){
            CKEDITOR.instances.myEditor.setData( $("#tpl_data").val() );
        },
        
        emailTemple:{
            
            // 验证模板信息
            validate:function(contentDate){
                var nameFlag=false,
                    emailFlag=false,
                    emailContentFlag = false,
                    lengthFlag = false,
                    statusArr=new Array(),
                    signalArr=["请填写模板名称","请填写邮件标题","邮件模板不能为空","请输入2~100个字符"],
                    $nameDom = $("#Temp_name"), 
                    $emailDom = $("#email_name"),
                    i=0,
                    reg = /^\s+$/g;
                
                if( !validRule.nullContent($nameDom.val(),"请输入2~100个字符") ){
                    nameFlag=false;   
                }else{
                    nameFlag=true;
                    if(!validRule.minLength($nameDom.val(),2) || 
                       !validRule.maxLength($nameDom.val(),100)){
                        lengthFlag = false;
                    }else{
                        lengthFlag = true;
                    }
                };
                
                if(!validRule.nullContent($emailDom.val(),"请输入2~100个字符")){
                    emailFlag=false;   
                }else{
                    emailFlag=true;
                    if(!validRule.minLength($emailDom.val(),2) || 
                       !validRule.maxLength($emailDom.val(),100)){
                        lengthFlag = false;
                    }else{
                        
                        // 如果模板名称长度不正确，无论邮件标题长度是否正确，都为false
                        lengthFlag = lengthFlag ? true : false;
                    }
                };
                
                if(reg.test(contentDate) || !contentDate){
                    emailContentFlag = false;
                }else{
                    emailContentFlag = true;
                };
                
                statusArr.push(nameFlag, emailFlag, emailContentFlag, lengthFlag);
                
                if(nameFlag && emailFlag && emailContentFlag && lengthFlag){
                     return true
                }else{
                    
                    for(i;i<statusArr.length;i++){
                        if( statusArr[i] == false ){
                            break;
                        };
                    };
                    
                    $("#temp_editor_dialog").myAlert(
                        'alert',
                        {
                            'myalert_w': '450px',
                            'myalert_h': '150px',    
                            'myalert_title':'提示',
                            'alert_time':'500', 
                            'alert_word': "<p class='error margin-t-20'>"
                                            +signalArr[i]
                                        +"</p>"
                        }
                    );
                    return false
                }
            },
            
            // 保存模板
            save:function(){
                var editor_data = CKEDITOR.instances.myEditor.getData(),
                    
                    // 纯文本
                    contentDate = CKEDITOR.instances.myEditor.document.getBody().getText(),
                    validateFlag=this.emailTemple.validate.call(tasks, editor_data); 
            
                if(validateFlag){ // 验证通过
                    
                    // 模板值存入表单
                    $("#tpl_data").val(editor_data);
                    $("#addTemplate").submit();
                };
            },
            
            // 模板预览
            preview:function(){
                var editor_data = CKEDITOR.instances.myEditor.getData();
                CKEDITOR.instances.myEditor.commands.preview.exec();
                return false;
            }
        },
        
        // 插入模板
        insertTemp : {

            // 手动关闭弹窗
            hideDialog:function(){
                $("#blackcover_template").remove();
                $("#temp_editor_dialog").empty();
            },

            model:function(){
                $.ajax({
                    type: "GET",
                    url: "/user/system_template/list",
                    dataType: "json",
                    success: function(data){
                        var baseHeight = 160,
                            imgW = 110,
                            imgH = 105,
                            imgMB = 25,
                            len = data.content.length,
                            lines = Math.ceil(len/5),
                            alertHeight = baseHeight + (imgH + imgMB) * lines,
                            tempDom = $("#temp_list").html(),
                            tempListData = {
                                'imgW' : imgW + 'px',
                                'imgH' : imgH + 'px',
                                'imgMB' : imgMB + 'px',
                                'list' : data.content
                            },
                            html = juicer(tempDom, tempListData);

                            
                        $("#temp_editor_dialog").myAlert(
                            'dialog',
                            {
                            'myalert_title':'选择模板',
                            'myalert_w' : '730px',
                            'myalert_h': alertHeight + 'px',
                            'cover_id':'blackcover_template',
                            'dialog_ok_autoHidden':false,
                            'dialog_ok_word':'确 定',
                            'dialog_word': html,
                            'dialog_ok_callback':function(){
                                this.insertTemp.callback.apply(this);
                            }.bind(this)
                        });
                    }.bind(this),
                    error : function(xml, err){
                        console.log(err);
                    }
                });
            },

            select : function(e){
                var $this = $(e.target).closest('li');
                
                $this.addClass('active')
                    .siblings('li').removeClass('active');

                $this.find('img[name="dark"]').removeClass('hidden-item');
                $this.find('img[name="light"]').addClass('hidden-item');

                $this.siblings('li').find('img[name="dark"]').addClass('hidden-item');
                $this.siblings('li').find('img[name="light"]').removeClass('hidden-item');

                $("#temp_editor_dialog")
                    .find('p.temp-select-signal')
                    .hide();
            },

            move:function(e){
                var $this =  $(e.target).closest('li'),
                    moveType = e.type;

                if( !$this.hasClass('active') ){
                    if(event.type == "mouseover"){
                        $this.find('img[name="dark"]').removeClass('hidden-item');
                        $this.find('img[name="light"]').addClass('hidden-item');
                    }else if(event.type == "mouseout"){
                        $this.find('img[name="dark"]').addClass('hidden-item');
                        $this.find('img[name="light"]').removeClass('hidden-item');
                    }
                }
            },

            callback:function(){
                var valid = this.insertTemp.valid.apply(this),
                    key = "",
                    idInput =  $("#sel_temp_id").val();

                if(valid){
                    key = $("#temp_editor_dialog li.active").data('key');

                    $.ajax({
                        type: "GET",
                        url: "/user/system_template/"+key,
                        dataType: "json",
                        success: function(data){
                            var result = data.content.htmlContent;

                            $("#sel_temp_id").val(idInput + key + ',');
                            this.insertTemp.hideDialog();
                            CKEDITOR.instances.myEditor.insertHtml(result);

                        }.bind(this),
                        error : function(xml, err){
                            console.log(err);
                        }
                    })
                }
            },

            valid: function(){
                var $dialog= $("#temp_editor_dialog"),
                    $signal = $dialog.find('p.temp-select-signal'),
                    len = $dialog.find('li.active').length;

                if(!len){
                    $signal.show();
                    return false;
                }else{
                    $signal.hide();
                    return true;
                }
            }
        },

        bindEvents:function(){
            
            $("#save_eamil_temp")
                .on( 'click',this.emailTemple.save.bind(this) );
            
            $("#save_preview_temp")
                .on( 'click',this.emailTemple.preview.bind(this) );

            $("#temp_editor_dialog")
                .on('click', 
                    '#tempSelectList > li', 
                    this.insertTemp.select.bind(this));

            $("#temp_editor_dialog")
                .on('mouseover mouseout', 
                    '#tempSelectList > li', 
                    this.insertTemp.move.bind(this));
        }
    };
    
    tasks.init();
    
});