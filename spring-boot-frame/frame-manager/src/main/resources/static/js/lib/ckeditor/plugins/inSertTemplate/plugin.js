(function(){ 
    //Section 1 : 按下自定义按钮时执行的代码 
    var a= { 
        exec:function(editor){ 
            if(! editor.config.templateCallback ) return;
            editor.config.templateCallback();
        } 
    }, 
    //Section 2 : 创建自定义按钮、绑定方法 
    b='inSertTemplate'; 
    CKEDITOR.plugins.add(b,{ 
        init:function(editor){ 
            editor.addCommand(b,a); 
            editor.ui.addButton('inSertTemplate',{ 
                label:'插入系统模板', 
                icon: this.path + 'tp.png', 
                command:b 
            }); 
        } 
    }); 
})(); 