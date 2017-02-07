(function ($) {

    $.fn.showpalceholder = function (options) {

        //创建一些默认值，拓展任何被提供的选项
        var settings = $.extend({
            "placeWorld":"提示",
            "placeWColor":"#ccc"
        }, options);

        return this.each(function () {
            var thisDOM=$(this);
            var pastcolor=thisDOM.css('color');
            
            thisDOM.removeAttr('placeholder');
            thisDOM.val(settings.placeWorld)
                   .css('color',settings.placeWColor);
            thisDOM.focus(function(){
                var valNow=$(this).val();
                if(valNow == settings.placeWorld){
                  $(this).val('').css("color",pastcolor);
                }              
            });
            thisDOM.blur(function(){
              var valNow=$(this).val();
              if( (valNow == '') || (valNow == null) ){
                var valNow=$(this).val(settings.placeWorld)
                           .css('color',settings.placeWColor);
              }    
            
            })
        });

    };
})(jQuery);