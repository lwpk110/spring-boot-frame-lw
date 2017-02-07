require.config({
    paths : {
        /*'jquery': 'lib/jquery-1.8.3.min'*/
    }  
});

define([],function(){
    
    var validRule = validRule || {};
    
    //验证是否为空
    validRule.nullContent=function(val,placeHolder){
        var reg = /^\s+$/g;
        if( !val || val == placeHolder || reg.test(val)){
            return false;
        }else{
            return true;
        };
    };
    
    //验证邮箱格式
    validRule.emailAddress=function(val){
        var regexp = "^(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*[0-9a-zA-Z-]+))@([a-zA-Z0-9-]+[.])",
            emailRegexp = new RegExp(regexp, "g");
                
        if( emailRegexp.test(val) ){
            return true;
        }else{
            return false;
        }
    };
            
    // 验证最大长度
    validRule.maxLength=function(val,max){
        var len = val.length,
            flag = false;
                
        flag = ( len > max ) ?  false :  true;
                
        return flag;
    };
            
    // 验证最小长度
    validRule.minLength=function(val,min){
        var len = val.length,
            flag = false;
                
        flag = ( len < min ) ?  false :  true;
                
        return flag;
    };
        
    // 验证复选框是否选择
    validRule.checked=function(groupName){
        var count = 0,
            i = 0,
            checkArry = document.getElementsByName(groupName);
        
        for (i = 0; i < checkArry.length; i++){
            if(checkArry[i].checked == true){
                count++; 
            };
        };
        
        if(count == 0){
            return false;
        }else{
            return true;
        };
    };
    
    return validRule;
});