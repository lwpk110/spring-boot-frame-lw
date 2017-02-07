require.config({
    paths: {
        'jquery': 'lib/jquery-1.8.3.min',
        'myAlert': 'others/myAlert/js/myAlert',
    },
    map: {
        '*': {
            'css': 'css.min'
        }
    },
    shim: {
        'myAlert': {
            deps: ['jquery', 'css!../js/others/myAlert/css/myalert.css']
        }
    }
});

require(['jquery', 'myAlert'], function (jquery, myAlert) {

    var account_buy_Dialog = "account_buy_Dialog"; //弹出框id
    $('body').append("<div id='" + account_buy_Dialog + "' style='display:none'></div>");

    var set_meal_list = "set_meal_list"; //套餐列表id
    var meal_price_list = { //套餐价格/点数数组
        'meal0': ['10', '1000']
    };
    var account_buy_amount = "account_buy_amount"; //购买数量input的id
    var rest_money_wapper = "rest_money_wapper"; //余额显示id
    var max_num_wapper = "max_num_wapper"; //最多购买点数id
    var total_pay_wapper = "total_pay_wapper"; //总计付款id
    var not_enough_signal = "not_enough_signal"; //余额不足提示
    var pay_button = "pay_button"; //支付按钮id
    //    var restMoney = 60; //设置当前余额为60元
    var restMoney = parseInt($("#j-balance").val());

    //展示初始余额
    $("#" + rest_money_wapper).text(restMoney);

    //展示初始购买最大点数
    maxBuy(0);

    //展示初始合计金额
    $("#" + total_pay_wapper).text(dotNumber(meal_price_list['meal' + 0][0]));

    //展示初始余额是否足够
    checkRestMoney();

    //选择套餐
    $("#" + set_meal_list + " ul li").on('click', function () {
        var this_meal = $(this);
        var meal_number = this_meal.index();
        //当前购买数量
        var now_buy_num = $("#" + account_buy_amount).val();
        this_meal.addClass('set-meal-select');
        this_meal.siblings('li').removeClass('set-meal-select');
        maxBuy(meal_number); //改变最大购买点数
        //改变合计金额
        $("#" + total_pay_wapper)
            .text(dotNumber(now_buy_num * parseInt(meal_price_list['meal' + meal_number][0])));

        //验证月是否足够
        checkRestMoney();
    });

    //数量改变时
    $("#" + account_buy_amount).on('keyup', function () {
        var index_li = $("#" + set_meal_list + " ul li.set-meal-select").index();
        var this_input = $(this);
        var inputVal = this_input.val();
        //限制只能输入正整数
        if (inputVal.match('[^0-9\]', '')) {
            var FilterVal = inputVal.substring(0, inputVal.length - 1);
            this_input.val(FilterVal);
        }
        ;
        var FinalVal = this_input.val();
        var total_pay = FinalVal * parseInt(meal_price_list['meal' + index_li][0]);
        $("#" + total_pay_wapper).text(dotNumber(total_pay));
        checkRestMoney();
    });

    //数字转小数点
    function dotNumber(num) {
        var onsale = 1; //目前没有折扣
        var afterSale = Math.round(num * onsale * 100) / 100; //四舍五入到2位小数点
        var changeVal = afterSale.toString(); //转化成字符
        var rs = changeVal.indexOf('.'); //取小数点后的数字，不足2位不足2位
        if (rs < 0) {
            rs = changeVal.length;
            changeVal += '.';
        }
        ;
        while (changeVal.length <= rs + 2) {
            changeVal += '0';
        }
        ;
        return changeVal;
    };

    //点击支付按钮
    $("#" + pay_button).on('click', function () {
        var index_li = $("#" + set_meal_list + " ul li.set-meal-select").index();
        var pay_money = $("#" + total_pay_wapper).text();
        var buy_num = parseInt($("#" + account_buy_amount).val());
        if (!buy_num) {
            $("#" + account_buy_Dialog).myAlert(
                'alert', {
                    'myalert_w': '450px',
                    'myalert_h': '150px',
                    'myalert_title': '提示',
                    'alert_time': '1000',
                    'alert_word': "<p class='error margin-t-20'>请输入购买数量</p>"
                }
            );
        } else {
            var credits = buy_num * parseInt(meal_price_list['meal' + index_li][1]);
            $("#credits").val(credits);
            $("#amount").val(pay_money);
            $("#" + account_buy_Dialog).myAlert(
                'dialog', {
                    'myalert_title': '支付确认',
                    'cover_id': 'blackcover_advice',
                    'myalert_h': '250px',
                    'dialog_ok_word': '确 定',
                    'dialog_word': "<p class='grey-font'>" + "将从您的钱包支付" + pay_money + "元购买" + buy_num * parseInt(meal_price_list['meal' + index_li][1]) + "个点数。" + "</p>" + "<p class='error margin-t-40'>" + "确定支付。" + "</p>",
                    'dialog_ok_callback': function () {
                        $("#pay_form").submit();
                    }
                }
            );
        }
        return false;
    });

    //验证余额是否足够
    function checkRestMoney() {
        var payMoney = $("#" + total_pay_wapper).text();
        var buyNumber = $("#account_buy_amount").val();
        if (payMoney > restMoney || buyNumber == 0 || buyNumber == ""||buyNumber == null) { //余额不足
            $("#" + not_enough_signal).removeClass('hidden-item');
            $("#" + pay_button).attr('disabled', 'disabled')
                .addClass('disable-button');
            return false;
        } else if (payMoney <= restMoney) {
            $("#" + not_enough_signal).addClass('hidden-item');
            $("#" + pay_button).removeAttr('disabled')
                .removeClass('disable-button');
            return true;
        }
        ;
    };

    //计算最多购买点数
    function maxBuy(meal_num) {
        var num = Math.floor(restMoney / parseInt(meal_price_list['meal' + meal_num][0]));
        var finalNum = num * parseInt(meal_price_list['meal' + meal_num][1]);
        $("#" + max_num_wapper).text(finalNum);
    };


});