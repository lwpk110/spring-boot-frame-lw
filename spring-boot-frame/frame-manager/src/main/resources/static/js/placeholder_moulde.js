require.config({
    paths : {
        'jquery' : 'lib/jquery-1.8.3.min',
        'showplaceholder':'others/showplaceholder'
    },
    shim: {
        'showplaceholder': {deps : ['jquery']}
    }   
}); 

define(['jquery','showplaceholder'],function(jquery){
    var placeholder_moulde=function(holderId,holderword,holderbg){
        if($.browser.msie&&( ($.browser.version == "7.0") || ($.browser.version == "8.0") 
           ||($.browser.version == "9.0"))){
                $('#'+holderId).showpalceholder({
                    "placeWorld":holderword,
                    "placeWColor":holderbg   
                });
            }
    };
    
    return placeholder_moulde
});