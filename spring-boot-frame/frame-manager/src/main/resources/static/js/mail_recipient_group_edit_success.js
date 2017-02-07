require.config({
    paths : {
        'jquery': 'lib/jquery-1.8.3.min',
        'myAlert': 'others/myAlert/js/myAlert'
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


require(['basejs','jquery','myAlert']);