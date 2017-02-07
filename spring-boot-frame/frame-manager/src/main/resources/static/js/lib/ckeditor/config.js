/**
 * @license Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	    
    //中文界面
    config.language = 'zh-cn';

    //中文字体
    config.font_names = '宋体/宋体;黑体/黑体;仿宋/仿宋_GB2312;楷体/楷体_GB2312;隶书/隶书;幼圆/幼圆;微软雅黑/微软雅黑;' + config.font_names;
    
    //选用moono主题
    config.skin= 'moono';
    
    //编辑器大小不可拖拽
    config.resize_enabled = false;

    //关闭标签过滤
    config.allowedContent = true;
    
    //行高插件、图片黏贴插件(没有图标不显示在工具栏上)暂时不显示
    config.extraPlugins="inSertMarkName,inSertMarkEmail,inSertTemplate,lineheight";
    
    config.toolbar = 'MyToolbar'; 
    
    config.toolbar_Full =[
        ['Source','-','Save','NewPage','Preview','-','Templates'],
        ['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],
        ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
        ['Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField'],
        '/',
        ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
        ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],
        ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
        ['Link','Unlink','Anchor'],
        ['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
        '/',
        ['Styles','Format','Font','FontSize','lineheight'],
        ['TextColor','BGColor'],
        ['inSertMarkName','-','inSertMarkEmail'],
        ['Maximize'],
        ['inSertTemplate']
    ];
    config.toolbar_MyToolbar =[
        ['Undo','Redo'],
        ['Format','Font','FontSize'],
        ['TextColor','BGColor'],
        ['Bold','Italic','Underline'],
        ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
        ['NumberedList','BulletedList','-','Outdent','Indent'],
        ['Image','Table','HorizontalRule'],
        ['Button', 'ImageButton'],
        ['Source'],
        ['Cut','Copy','Paste'],
        ['Link','Unlink'],
        ['inSertMarkName','-','inSertMarkEmail'],
        ['inSertTemplate']
    ];
    
};
