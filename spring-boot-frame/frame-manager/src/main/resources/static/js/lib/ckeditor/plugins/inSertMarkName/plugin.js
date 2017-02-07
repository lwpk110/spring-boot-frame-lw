(function(){ 
    //Section 1 : 按下自定义按钮时执行的代码 
    var a= { 
        exec:function(editor){ 
            var ckId = editor.name;
            CKEDITOR.instances[ckId].insertHtml('{{{name}}}'); 
        } 
    }, 
    //Section 2 : 创建自定义按钮、绑定方法 
    b='inSertMarkName'; 
    CKEDITOR.plugins.add(b,{ 
        init:function(editor){ 
            editor.addCommand(b,a); 
            editor.ui.addButton('inSertMarkName',{ 
                label:'插入收件人姓名', 
                icon: this.path + 'xm.png', 
                command:b 
            }); 
        } 
    }); 
})(); 