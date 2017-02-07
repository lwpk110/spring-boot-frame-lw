require.config({
    paths : {
        'jquery': 'lib/jquery-1.8.3.min',
        'myAlert': 'others/myAlert/js/myAlert',
        'placeholder_moulde':'placeholder_moulde'
    },
    map: {
        '*': {
            'css': 'css.min'
        }
    },
    shim: {
        'myAlert': {deps : ['jquery','css!../js/others/myAlert/css/myalert.css']}
    }   
}); 

require(['jquery','myAlert','placeholder_moulde'],function(jquery,myAlert,placeholder_moulde){
    var girl_id="girl"; //girl id
    var adviceDialog_id="adviceDiolag"; //建议弹出框id
    var statusDialog_id="statusDialog";//发送是否成功弹出框id
    $('body').append("<aside class='girl-wapper' id='"+girl_id+"'>"
        +"<div class='girl'>"
        +"<div class='girl-close' onclick='closeAdvice()'></div>"
        +"<div class='girl-img-wapper' onclick='showAdviceDialog()'></div>"
        +"</div>"
        +"</aside>"
        +"<div id='"+adviceDialog_id+"' style='display:none'></div>"
        +"<div id='"+statusDialog_id+"' style='display:none'></div>");
    
    closeAdvice=function(){
        $("#"+girl_id).hide(); 
    };
    
    showAdviceDialog=function(){
        $("#"+adviceDialog_id).myAlert(
             'dialog',
             {
                'myalert_title':'我要说一下',
                'cover_id':'blackcover_advice', 
                'dialog_ok_word':'发表', 
                'dialog_ok_autoHidden':false,
                'dialog_word':
                 "<p class='advice-start'>"
                     +"您好，我是 腾道外贸搜 产品经理，欢迎您给我们提产品的使用感受和建议！"
                +"</p>"
                +"<form>"
                   +"<div class='container-fluid'>"
                     +"<div class='row margin-t-10'>"
                        +"<div class='col-xs-2 advice-tit'>"+"事件标题"+"</div>"
                        +"<div class='col-xs-7'>"
                            +"<input type='text' class='input-size-big border-rd-3 grey-border'"
                            +" placeholder='请输入4~30个字符' maxlength='30' id='advice_tit_input'/>"
                        +"</div>"
                        +"<div class='col-xs-3 error advice-singal' style='display:none;' id='advice_tit_singal'>"
                            +"<i class='fa fa-exclamation-triangle error'></i>标题不能为空！"
                        +"</div>"
                     +"</div>"
                    
                     +"<div class='row margin-t-10'>"
                        +"<div class='col-xs-2 advice-tit'>"+"详细内容"+"</div>"
                        +"<div class='col-xs-10'>"
                            +"<textarea class='textarea-size-small border-rd-3 grey-border advice-textarea' placeholder='感谢您给我们提出建议' id='advice_detail_textarea'></textarea>"
                        +"</div>"
                     +"</div>"
                 
                      +"<div class='row margin-t-10' style='display:none;' id='advice_detail_singal'>"
                        +"<div class='col-xs-3 col-xs-offset-2 error'>"
                            +"<i class='fa fa-exclamation-triangle error'></i>内容不能为空！"
                        +"</div>"
                     +"</div>"
                   +"</div>"
                +"</form>",
                
                'dialog_ok_callback':function(){
                    var tit_flag=false;
                    var detail_flag=false;
                    var tit_word=$("#advice_tit_input").val();
                    var textarea_word=$("#advice_detail_textarea").val();
                    //表单验证
                    if( (tit_word == "") || (tit_word == null) || (tit_word == "请输入4~30个字符") ){
                        tit_flag=false;
                        $("#advice_tit_singal").show();
                    }else{
                        tit_flag=true;
                    };
                    
                    if( (textarea_word == "") || (textarea_word == null) || (textarea_word == "感谢您给我们提出建议") ){
                        detail_flag=false;
                        $("#advice_detail_singal").show();
                    }else{
                        detail_flag=true;
                    };
                    
                    //验证通过后
                    if( tit_flag && detail_flag){
                        $("#"+adviceDialog_id).empty().hide();
                        $("#blackcover_advice").remove();
                        $("#"+statusDialog_id).myAlert(
                            'alert',
                            {
                               'myalert_w': '450px',
                               'myalert_h': '150px',    
                               'myalert_title':'提示',
                               'alert_time':'1000',
                               'alert_word': "<p class='error margin-t-20'>"
                                               +"意见发表成功"
                                            +"</p>"
                            }
                        )
                    }
                } 
             }
           );
        
        $("#advice_tit_input,#advice_detail_textarea").focus(function(){
            $("#advice_tit_singal,#advice_detail_singal").hide();
        });
        
        placeholder_moulde("advice_tit_input","请输入4~30个字符","#bfbfbf");
        placeholder_moulde("advice_detail_textarea","感谢您给我们提出建议","#bfbfbf");
    };
    
    
  
});