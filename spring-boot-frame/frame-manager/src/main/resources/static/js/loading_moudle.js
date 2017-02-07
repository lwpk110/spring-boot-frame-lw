require.config({
    paths : {
        'jquery': 'lib/jquery-1.8.3.min'
    }  
});

define(['jquery'],function(jquery){
    
    var loading_wait=function(){
        $("body").append("<img src='"+env.contextUri+"img/default_loading_bg_white.gif' alt='laoding' id='loading_img' style='position:fixed;z-index:20;' />");
        var window_W=$(window).width();
        var window_H=$(window).height();
        var loading_l=(parseInt(window_W)-48)/2;
        var loading_t=(parseInt(window_H)-48)/2;
        $("#loading_img").css({'left':loading_l,'top':loading_t});
        $("body").append("<div id='loading_cover' style='position:fixed;z-index:10;left:0px;top:0px;width:100%;height:100%;background:#000;filter:alpha(opacity:40); opacity: 0.4;'></div>");
        
    }
    
    return loading_wait
});