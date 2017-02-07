require.config({
    paths: {
        'jquery': 'lib/jquery-1.8.3.min',
        'errorHandle': 'lib/jquery.handle',
        'ajaxfileupload': 'lib/ajaxfileupload',
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

require(['basejs', 'jquery', 'myAlert', 'placeholder_moulde', 'errorHandle', 'ajaxfileupload'], function (basejs, jquery, myAlert, placeholder_moulde) {

    var recipientObj = {
        invalid : null,
        notExist: null
    };
    var recipients_name = "creat_recipient_name"; //待收件人组名称input的id
    var recipientsDialog_id = "recipientsDialog"; //弹出框id
    $("body").append("<div id='" + recipientsDialog_id + "' style='display:none;'></div>")

    placeholder_moulde(recipients_name, "请输入2~100个字符", "#bfbfbf");

    var not_choose_rec = "not_choose_rec";//未选择csv文件前内容id
    var choose_rec_haserror = "choose_rec_haserror";//已选择文件内有错误
    var choose_rec_button = "choose_rec_button";//选择csv文件按钮区
    var upload_rec_button = "upload_rec_button";//上传csv文件按钮区

    function isIE() {
        var myNav = navigator.userAgent.toLowerCase();
        return (myNav.indexOf('msie') != -1) ? parseInt(myNav.split('msie')[1]) : false;
    }

    function verify(e) {
        var maxFileSize = 1024000;
        var fileSize;

        if (isIE() && isIE() < 10) {
            var filePath = e.value;
            if (filePath != '') {
                try {
                    var AxFSObj = new ActiveXObject("Scripting.FileSystemObject");
                    var AxFSObjFile = AxFSObj.getFile(filePath);
                    fileSize = AxFSObjFile.size;
                }
                catch (e) {
                    alert("请在Internet选项中开启允许允许ActiveX控件！");
                }
            }
        } else {
            if (e.value != '') {
                fileSize = e.files[0].size;
            }
        }
        return fileSize < maxFileSize;
    }

    //点击选择csv按钮
    function selectCSV(e) {
        //清空表格数据
        $("#" + choose_rec_haserror).find('table tbody').empty();
        $("#sureUploadAgainBtn").removeClass("disable-button").removeAttr("disabled");
        var isVerify = verify(e);

        if (isVerify) {
            $(".verifying").removeClass('hidden-item');
            $(".recipient-loading").removeClass('hidden-item')
            $("#" + not_choose_rec).addClass('hidden-item');
            $("#" + choose_rec_button).removeClass('hidden-item');
            $("#" + upload_rec_button).addClass('hidden-item');
            $("#" + choose_rec_haserror).removeClass('hidden-item');
            $(".verify-total").addClass('hidden-item');
            document.getElementById('verify-valid').style.visibility="hidden";
            $(".recipient-detail").addClass('hidden-item')
            $($("#choose_rec_button").find("button")[0]).text("重新选择文件");
            $("#sureUploadAgainBtn").removeClass('hidden-item');
            $("#sureUploadAgainBtn").addClass("disable-button").attr("disabled", "disabled");
            $.ajaxFileUpload({
                url: 'fileupload',
                secureuri: false,
                dataType: 'json',
                fileElementId: $(e).attr("id"),
                error: function (data, status, e)//服务器响应失败处理函数
                {
                    $("#" + recipientsDialog_id).myAlert(
                        'alert',
                        {
                            'myalert_w': '450px',
                            'myalert_h': '150px',
                            'myalert_title': '提示',
                            'alert_time': '1000',
                            'alert_word': "出错啦！"
                        }
                    );
                },
                success: function (data, status)  //服务器成功响应处理函数
                {
                     if (data.status == 200) {
                         $("#sureUploadAgainBtn").removeClass("hidden-item");
                         $(".recipient-detail").removeClass('hidden-item')
                         $(".recipient-loading").addClass('hidden-item')
                         $(".verifying").addClass('hidden-item')
                         $(".verify-total").removeClass('hidden-item')
                         document.getElementById('verify-valid').style.visibility='visible';
                         $("#sureUploadAgainBtn").removeClass("disable-button").removeAttr("disabled");;

                        var invalidRecords = data.content.invalidRecords;
                         var validRecords = data.content.validRecords;
                         var notExistRecords = data.content.notExistRecords;
                         var totality = data.content.totality;
                         var repeatNumber = totality - invalidRecords.length - validRecords.length - notExistRecords.length;
                         $("#totalNumber").text(totality);
                         $("#invalidRecordsNumber").text(invalidRecords.length).parent().removeClass("hidden-item");
                         $("#validRecordsSizeError").text(validRecords.length);
                         $("#notExistNumber").text(notExistRecords.length);
                         $("#validRecordsSize").text(validRecords.length).parent().removeClass("hidden-item");
                         $("#repeatNumber").text(repeatNumber);
                         if (invalidRecords.length == 0) {
                            $(".invalidRecordsNumber").hide()
                        } else {
                             $(".invalidRecordsNumber").show()
                         }

                         if (notExistRecords.length == 0) {
                             $(".notExistNumber").hide()
                         } else {
                             $(".notExistNumber").show()
                         }

                         recipientObj = {
                             invalid: invalidRecords,
                             notExist: notExistRecords
                         };

                         if (validRecords.length == 0) {
                             $("#sureUploadAgainBtn").addClass("disable-button").attr("disabled", "disabled");
                         }
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
                            $("#totality").text(totality);
                            $("#" + not_choose_rec).addClass('hidden-item');
                            $("#" + choose_rec_button).addClass('hidden-item');
                            $("#" + upload_rec_button).removeClass('hidden-item');
                        }
                    } else if (data.status == 400) {
                        $("#" + choose_rec_button).removeClass('hidden-item');
                        $("#" + not_choose_rec).removeClass('hidden-item');
                        $("#" + upload_rec_button).addClass('hidden-item');
                        $("#sureUploadAgainBtn").addClass('hidden-item');
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
                                'alert_time': '1000',
                                'alert_word': "<p class='error margin-t-20'>"
                                + data.message
                                + "</p>",
                                'alert_callback': callback
                            }
                        );
                     }
                }
            });
        } else {
            $("#" + not_choose_rec).removeClass('hidden-item');
            $("#" + choose_rec_button).removeClass('hidden-item');
            $("#" + upload_rec_button).addClass('hidden-item');
            $("#" + choose_rec_haserror).addClass('hidden-item');
            $("#validRecordsSize").parent().addClass("hidden-item");
            $("#" + recipientsDialog_id).myAlert(
                'alert', {
                    'myalert_w': '450px',
                    'myalert_h': '150px',
                    'myalert_title': '提示',
                    'alert_time': '1000',
                    'alert_word': "<p class='error margin-t-20'>上传文件不能大于1MB</p>"
                }
            );
        }
    }

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

    // function previewError(e) {
    //     var $this = $(e).closest('i');
    //
    // }
    //
    $("#file").live('change', function () {
        selectCSV(this)
    });
    $("#fileAgain").live('change', function () {
        selectCSV(this)
    });

    $("#sureUploadBtn").live('click', sureUpload);
    $("#sureUploadAgainBtn").live('click', sureUpload);

    $("#recipient i").on('click', function (e) {
        var $this = $(e.target).closest('i');
        if ($this.hasClass('invalidRecordsNumber')) {
            sessionStorage.setItem("records", JSON.stringify(recipientObj.invalid));
        } else {
            sessionStorage.setItem("records", JSON.stringify(recipientObj.notExist));
        }

        window.open('/user/recipient_group/error_record_preview', '_blank')
    })

});