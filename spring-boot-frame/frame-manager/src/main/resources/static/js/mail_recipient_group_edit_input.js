require.config({
    paths: {
        'jquery': 'lib/jquery-1.8.3.min',
        'myAlert': 'others/myAlert/js/myAlert',
        'placeholder_moulde': 'placeholder_moulde'
    },
    map: {
        '*': {
            'css': 'css.min'
        }
    },
    shim: {
        'errorHandle': {deps: ['jquery']},
        'ajaxfileupload': {deps: ['jquery', 'errorHandle']},
        'myAlert': {deps: ['jquery', 'css!../js/others/myAlert/css/myalert.css']}
    }
});

require(['basejs', 'jquery', 'myAlert', 'placeholder_moulde'], function (basejs, jquery, myAlert, placeholder_moulde) {

    var recipientObj = {
        invalid :　null,
        notExist : null
    };
    var recipients_name = "creat_recipient_name"; //待收件人组名称input的id
    var recipientsDialog_id = "recipientsDialog"; //弹出框id
    $("body").append("<div id='" + recipientsDialog_id + "' style='display:none;'></div>")

    placeholder_moulde(recipients_name, "请输入2~100个字符", "#bfbfbf");

    var not_choose_rec = "not_choose_rec";//未选择csv文件前内容id
    var choose_rec_haserror = "choose_rec_haserror";//已选择文件内有错误
    var choose_rec_pass = "choose_rec_pass";//csv文件校验通过
    var choose_rec_button = "choose_rec_button";//选择csv文件按钮区
    var upload_rec_button = "upload_rec_button";//上传csv文件按钮区

    $("#csv_text_save").on('click', function () {
        //清空表格数据
        $("#choose_rec_haserror").find('table tbody').empty();
        $("#csv_text_save").addClass("hidden-item");
        $("#csv_text_resave").removeClass("hidden-item");
        $(".verifying").removeClass('hidden-item');
        $(".recipient-loading").removeClass('hidden-item')
        $("#" + not_choose_rec).addClass('hidden-item');
        $("#" + choose_rec_button).removeClass('hidden-item');
        $("#" + upload_rec_button).addClass('hidden-item');
        $("#" + choose_rec_haserror).removeClass('hidden-item');
        $(".verify-total").addClass('hidden-item');
        document.getElementById('verify-valid').style.visibility='hidden';
        $(".recipient-detail").addClass('hidden-item')
        $("#not_correct_upload").removeClass('hidden-item');
        $("#not_correct_upload").addClass("disable-button").attr("disabled", "disabled");

        $.post("add_by_input", {content: $("#content").val()}, function (data) {
            $("#" + choose_rec_haserror).find(".csv-error-tablewapper").removeClass("hidden-item");
            $("#not_correct_upload").removeClass("disable-button").removeAttr("disabled");
            if (typeof data == "string") {
                data = $.parseJSON(data);
            }
            if (data.status == 200) {


                $("#sureUploadAgainBtn").removeClass("hidden-item");
                $(".recipient-detail").removeClass('hidden-item')
                $(".recipient-loading").addClass('hidden-item')
                $(".verifying").addClass('hidden-item')
                $(".verify-total").removeClass('hidden-item')
                document.getElementById('verify-valid').style.visibility='visible';
                $("#not_correct_upload").removeClass("disable-button").removeAttr("disabled");
                var invalidRecords = data.content.invalidRecords;
                var validRecords = data.content.validRecords;
                var notExistRecords = data.content.notExistRecords;
                var totality = data.content.totality;
                var repeatNumber = totality - invalidRecords.length - validRecords.length - notExistRecords.length;
                if(validRecords.length == 0){
                    $("#not_correct_upload").addClass("disable-button").attr("disabled","disabled");
                }
                $("#repeatNumber").text(repeatNumber);
                $("#totalNumber").text(totality);
                $("#notExistNumber").text(notExistRecords.length);
                $("#invalidRecordsNumber").text(invalidRecords.length);
                $("#validRecordsSize").text(validRecords.length);
                if (invalidRecords.length == 0){
                    $(".invalidRecordsNumber").hide()
                }else{
                    $(".invalidRecordsNumber").show()
                }
                if (notExistRecords.length == 0){
                    $(".notExistNumber").hide()
                }else{
                    $(".notExistNumber").show()
                }

                recipientObj = {
                    invalid :　invalidRecords,
                    notExist : notExistRecords
                };
                if (invalidRecords.length > 0) {//未通过校验
                    $.each(invalidRecords, function (i, item) {
                        $("#" + choose_rec_haserror).find('table tbody')
                            .append("<tr id='row" + i + "'>"
                            + "<td width='10%'>" + item.recordNumber + "</td>"
                            + "<td width='60%'>"
                            + item.email
                            + "</td>"
                            + "<td width='30%'>"
                            + item.fullName
                            + "</td>"
                            + "</tr>");
                    });

                } else {//校验通过
                    $("#" + choose_rec_haserror).removeClass('hidden-item').find(".csv-error-tablewapper").addClass("hidden-item");
                    $("#not_choose_rec").addClass('hidden-item');
                    $("#csv_text_save").addClass('hidden-item');
                    $("#csv_text_resave").removeClass('hidden-item');
                    $("#not_correct_upload").removeClass('hidden-item');
                }
            } else if (data.status == 400) {
                $("#" + choose_rec_button).removeClass('hidden-item');
                $("#" + not_choose_rec).removeClass('hidden-item');
                $("#" + upload_rec_button).addClass('hidden-item');
                $("#" + choose_rec_haserror).addClass('hidden-item');
                $("#invalidRecordsNumber").parent().addClass("hidden-item");
                $("#validRecordsSize").parent().addClass("hidden-item");
                var callback = function () {
                    data.status == 102 ? location.href = env.contextUri + "user/" : "";
                };
                $("#" + recipientsDialog_id).myAlert(
                    'alert',
                    {
                        'myalert_w': '450px',
                        'myalert_h': '150px',
                        'myalert_title': '提示',
                        'alert_time': '2000',
                        'alert_word': "<p class='error margin-t-20'>"
                        + data.message
                        + "</p>",
                        'alert_callback': callback
                    }
                );
            }
        });
    });

    $("#csv_text_resave").on('click', function () {
        $(this).addClass('hidden-item');
        $("#choose_rec_haserror").addClass('hidden-item');
        $("#not_correct_upload").addClass('hidden-item');
        $("#not_choose_rec").removeClass('hidden-item');
        $("#csv_text_save").removeClass('hidden-item');
    });

    //点击确定上传按钮
    function sureUpload() {
        if ($("#creat_recipient_name").val() == "请输入2~100个字符") {
            $("#creat_recipient_name").val("");
        }
        $("#sureUpload").validate({
            onfocusout: false,
            rules: {
                name: {
                    required: true,
                    rangelength: [2, 99]
                }
            },
            submitHandler: function (form) {
                form.submit();
            },
            messages: {
                name: {
                    required: "请输入收件人组名称",
                    rangelength: "请在收件人组名称中输入2-100个字符"
                }
            },
            invalidHandler: function (event, validator) {
                $("#" + recipientsDialog_id).myAlert(
                    'alert', {
                        'myalert_w': '450px',
                        'myalert_h': '150px',
                        'myalert_title': '提示',
                        'alert_time': '1000',
                        'alert_word': "<p class='error margin-t-20'>" + validator.errorList[0].message + "</p>"
                    }
                );

            },
            errorPlacement: function (error, element) {
            },
            showErrors: function (errorMap, errorList) {
            }
        });
    }

    $("#recipient i").on('click', function(e){
        var $this = $(e.target).closest('i');
        if($this.hasClass('invalidRecordsNumber')){
            sessionStorage.setItem("records",JSON.stringify(recipientObj.invalid));
        }else{
            sessionStorage.setItem("records",JSON.stringify(recipientObj.notExist));
        }

        window.open('/user/recipient_group/error_record_preview', '_blank')
    })

    $("#not_correct_upload").live('click', sureUpload);
});