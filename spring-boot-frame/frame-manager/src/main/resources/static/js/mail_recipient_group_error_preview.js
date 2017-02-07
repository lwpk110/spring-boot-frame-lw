require.config({
    paths: {
        'jquery': 'lib/jquery-1.8.3.min',
        'handlebars': 'lib/handlebars',
    },
});

require(['jquery', 'handlebars',], function (jquery, handlebars) {
       var records = sessionStorage.getItem("records"),
           source = $("#error_preview").html(),
           template = handlebars.compile(source),
           html = template(JSON.parse(records));

       $("#error_wapper").append(html)
})

