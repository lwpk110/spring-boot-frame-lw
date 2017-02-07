(function ($) {
                                 
    $.fn.myAlert = function (method,opt) { 
        
        //公共参数    
        var settings_pub = $.extend({ 
            'myalert_id':'diolag_1', //拟态框id
            'myalert_w': '600px',        //拟态框宽度
            'myalert_h': '400px',       //拟态框高度
            'myalert_p': '35px',     //拟态框的padding值
            'myalert_bg':'#fff',     //拟态框背景色
            'cover_id':'blackcover_1',//遮罩层id
            'cover_color':'#000',    //遮罩层颜色
            'cover_show' :true,    //是否显示遮罩
            'cover_zIndex':'99',      //遮罩层zindex值
            'myalert_title':'提示文字'        //title的文字
            },opt);
  
        //公共function        
        function imsertAlert(dialogWapperId){
            var myalert_ZIndex=parseInt(settings_pub.cover_zIndex)+1;
            $("#"+dialogWapperId).css({'width':'auto','height':'auto','display':'block',
                                       'position':'fixed','z-index':myalert_ZIndex})
                  .append("<div id='"+settings_pub.myalert_id
                                          +"' style='"
                                          +"width:"+settings_pub.myalert_w+";"
                                          +"height:"+settings_pub.myalert_h+";"
                                          +"background:"+settings_pub.myalert_bg+";"
                                          +"padding:"+settings_pub.myalert_p+" ;"
                                          +"position:relative ;top:0 ;left:0;"
                                          +"' ></div>");
            $("#"+settings_pub.myalert_id).append("<div style='width:100%;height:20px;line-height:20px;font-size:16px;'>"
                                                  +"<i style='"
                                                  +"display:block;float:left;margin-top:2px;width:3px;height:16px;background:#25BEC3;margin-right:10px;'>"
                                                  +"</i>"
                                                  +"<span>"+settings_pub.myalert_title+"</span>"
                                                  +"</div>")
            
             DialogPosition(dialogWapperId);
            if(settings_pub.cover_show){//显示遮罩
                $('body').append("<div style='"
                                 +"width:100% ;height:100%;"
                                 +"background:"+settings_pub.cover_color+";"
                                 +"filter:alpha(opacity:40); opacity: 0.4;"
                                 +"position:fixed ;top:0 ;left:0;"
                                 +"z-index:"+settings_pub.cover_zIndex+";"
                                 +"'"
                                 +" id='"+settings_pub.cover_id+"'"
                                 +"></div>");
            }else{//不显示遮罩
                $('body').append("<div style='"
                                 +"width:100% ;height:100%;"
                                 +"background:"+settings_pub.cover_color+";"
                                 +"filter:alpha(opacity:0); opacity: 0;"
                                 +"position:fixed ;top:0 ;left:0;"
                                 +"'></div>");
            }
            
        };
        
         //设置弹出层的位置 
        function DialogPosition(needpositionId){
           var winWidth=$(window).width();//窗口宽度
           var winHeight=$(window).height();//窗口宽度    
           var dialogWidth=settings_pub.myalert_w;//弹出框宽度
           var dialogHeight=settings_pub.myalert_h;//弹出框高度    
           var dialogLeft=(parseInt(winWidth)-parseInt(dialogWidth))/2;//弹出框left值
           var dialogTop=(parseInt(winHeight)-parseInt(dialogHeight))/2;//弹出框top值
          $("#"+needpositionId).css({'left':dialogLeft,'top':dialogTop});  
        }
        
        
         //插件组    
         var methods = {
            //提示框 
            alert: function (opt_alert) {
            var settings_alert = $.extend({    
              'alert_word': '<p>alert内容文字</p>',//内容
              'alert_time':'1000', //显示时间
              'alert_callback':function(){}//回调函数
              }, opt_alert);
            return this.each(function () {
                var thisItem=$(this).attr('id'); //拟态框wapper的id
                imsertAlert(thisItem);
                $(window).resize(function(){
                    DialogPosition(thisItem) 
                });
                
                $("#"+settings_pub.myalert_id).append(settings_alert.alert_word);
                
                $("#"+thisItem).delay(settings_alert.alert_time).fadeOut(500,function(){
                    $("#"+thisItem).empty();
                    $("#"+settings_pub.cover_id).remove();
                    settings_alert.alert_callback.call();//执行回调函数
                });      
                
            });

        },
        
        //对话框
        dialog: function (opt_dialog) {
            var settings_dialog = $.extend({
              'dialog_word': '内容',  //dialog的内容
                
              'dialog_ok_word':'确 定',//ok按钮的文字
              'dialog_ok_autoHidden':true, //按下ok按钮是否自动关闭    
              'dialog_ok_callback':function(){},//ok按钮回调函数 
                
              'dialog_cancel_word':'取 消',//ok按钮的文字 
              'dialog_cancel_callback':function(){}//cancel按钮回调函数
              }, opt_dialog);
             return this.each(function () {
                    var thisItem=$(this).attr('id'); //拟态框wapper的id
                    //dialog的内容框的最大高度:总高度-paddingTop-padding-bottom-按钮高度-title的高度
                    var contentMaxH=parseInt(settings_pub.myalert_h)-(parseInt(settings_pub.myalert_p) * 2)-28-20; 
                    imsertAlert(thisItem);
                    $(window).resize(function(){
                         DialogPosition(thisItem) 
                    });
                    
                    $("#"+settings_pub.myalert_id).append("<div class='alert-close' onclick='close_dialog()'>"+"</div>");
                    
                    $("#"+settings_pub.myalert_id).append("<div class='alert-dialog-content' style='max-height:"+contentMaxH+"px;'>"
                                                          +settings_dialog.dialog_word
                                                          +"</div>");
                 
                    $("#"+settings_pub.myalert_id).append("<div class='alert-button-wapper' style='bottom:"
                                                          +settings_pub.myalert_p+";'>"
                                                            +"<input type='button' value='"
                                                            +settings_dialog.dialog_ok_word
                                                            +"' "
                                                            +"class='alert-button-mr alert-button alert-button-ok' "
                                                            +"onclick='dialog_ok_click()'>"
                                                          
                                                            +"<input type='button' value='"
                                                            +settings_dialog.dialog_cancel_word
                                                            +"' "
                                                            +"class='alert-button alert-button-cancel' "
                                                            +"onclick='dialog_cancel_click()'>"
                                                          +"</div>")
                   
                     dialog_ok_click=function(){//点击ok,回调并返回黑色层id
                        if(settings_dialog.dialog_ok_autoHidden){
                            close_dialog();
                            settings_dialog.dialog_ok_callback(); 
                        }else{
                            settings_dialog.dialog_ok_callback();
                        } 
                     };
                 
                    dialog_cancel_click=function(){//点击cancel,回调
                        close_dialog();
                        settings_dialog.dialog_cancel_callback.call();
                     };
                    
                     close_dialog=function(){//关闭对话框
                        $("#"+thisItem).empty().hide();
                        $("#"+settings_pub.cover_id).remove();
                    };
                })
            }
        };
          
        
        // 方法调用
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error('Method' + method + 'does not exist on jQuery.inputfunction');
        };
      

    };

})(jQuery);