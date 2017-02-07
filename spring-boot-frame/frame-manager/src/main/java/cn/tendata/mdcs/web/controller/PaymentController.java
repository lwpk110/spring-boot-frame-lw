package cn.tendata.mdcs.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tendata.mdcs.service.UserDepositService;
import cn.tendata.pay.client.PaymentClient;
import cn.tendata.pay.client.PaymentClientHandleResult;

@Controller
@RequestMapping("/pay")
public class PaymentController {

    public static final String DEPOSIT_SUCCESS = "success";
    public static final String DEPOSIT_FAILED = "failed";
    
    private final PaymentClient paymentClient;
    private final UserDepositService userDepositService;
    
    @Autowired
    public PaymentController(PaymentClient paymentClient, UserDepositService userDepositService){
        this.paymentClient = paymentClient;
        this.userDepositService = userDepositService;
    }
    
    @RequestMapping(value = "/notify", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String notify(@RequestParam Map<String, String> params) {
        PaymentClientHandleResult handleResult = paymentClient.doNotify(params);
        userDepositService.depositEnd(handleResult.getOutTradeNo(), handleResult.isSuccess());
        return handleResult.isSuccess() ? DEPOSIT_SUCCESS : DEPOSIT_FAILED;
    }
}
