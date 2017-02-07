package cn.tendata.mdcs.web.controller;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.annotation.JsonView;

import cn.tendata.cas.client.security.core.userdetails.LoginUser;
import cn.tendata.cas.client.security.core.userdetails.LoginUser.ChildUser;
import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserDepositOrderDetail;
import cn.tendata.mdcs.data.domain.UserTransactionDetail;
import cn.tendata.mdcs.service.UserDepositService;
import cn.tendata.mdcs.service.UserService;
import cn.tendata.mdcs.service.UserTransactionDetailService;
import cn.tendata.mdcs.util.CollectionUtils;
import cn.tendata.mdcs.web.bind.annotation.ChildUserVariable;
import cn.tendata.mdcs.web.bind.annotation.CurrentUser;
import cn.tendata.mdcs.web.jackson.WebDataView;
import cn.tendata.mdcs.web.model.AssignBalanceDto;
import cn.tendata.mdcs.web.model.ChildUserDto;
import cn.tendata.mdcs.web.model.DepositOrderDetailDto;
import cn.tendata.mdcs.web.util.DataTablesParameter;
import cn.tendata.mdcs.web.util.DataTablesResult;
import cn.tendata.mdcs.web.util.LoginUserUtils;
import cn.tendata.pay.client.proxy.AccountBalanceResult;
import cn.tendata.pay.client.proxy.AccountPaymentResult;
import cn.tendata.pay.client.proxy.PaymentDetail;
import cn.tendata.pay.client.proxy.PaymentGatewayProxy;

@Controller
@RequestMapping("/user/account")
public class AccountController {

    private final UserService userService;
    private final UserDepositService userDepositService;
    private final UserTransactionDetailService userTransactionDetailService;
    private final PaymentGatewayProxy paymentGatewayProxy;

    @Autowired
    public AccountController(UserService userService, UserDepositService userDepositService,
                             UserTransactionDetailService userTransactionDetailService, PaymentGatewayProxy paymentGatewayProxy) {
        this.userService = userService;
        this.userDepositService = userDepositService;
        this.userTransactionDetailService = userTransactionDetailService;
        this.paymentGatewayProxy = paymentGatewayProxy;
    }

    @RequestMapping({"/", "/index"})
    public String home(ModelMap map, final @CurrentUser User user, final @AuthenticationPrincipal LoginUser loginUser) {
        List<UserTransactionDetail> userTransactionDetails = userTransactionDetailService.getAll(user, 5);
        map.addAttribute("user", user);
        map.addAttribute("loginUser", loginUser);
        map.addAttribute("userTransactionDetails", userTransactionDetails);
        return "account/home";
    }

    @RequestMapping("/deposit")
    public String depositForm(ModelMap map, final @CurrentUser User user) {
        AccountBalanceResult balanceResult = this.paymentGatewayProxy.getAccountBalance(user.getId());
        map.addAttribute("user", user);
        map.addAttribute("balanceResult", balanceResult);
        return "account/deposit_form";
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    public String deposit(@Validated @ModelAttribute("form") DepositOrderDetailDto depositOrderDetailDto,
                          final Errors errors, final RedirectAttributes redirectAttributes, final @CurrentUser User user) {
        if (errors.hasErrors()) {
            return "account/deposit_form";
        }
        int credits = depositOrderDetailDto.getAmount().multiply(BigDecimal.valueOf(100)).intValue();
        UserDepositOrderDetail orderDetail = new UserDepositOrderDetail(credits,
                depositOrderDetailDto.getAmount(), user);
        orderDetail = userDepositService.depositStart(orderDetail);
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setSubject("云邮通点数");
        paymentDetail.setTotalFee(orderDetail.getAmount());
        paymentDetail.setUserId(user.getId());
        paymentDetail.setOutTradeNo(orderDetail.getTradeNo());
        AccountPaymentResult paymentResult = paymentGatewayProxy.pay(paymentDetail);
        redirectAttributes.addFlashAttribute("paymentResult", paymentResult);
        return "redirect:/user/account/deposit_completed";
    }

    @RequestMapping("/deposit_completed")
    public String depositCompleted(ModelMap map) {
        AccountPaymentResult paymentResult = (AccountPaymentResult) map.get("paymentResult");
        if (paymentResult != null) {
            UserDepositOrderDetail orderDetail = userDepositService.depositEnd(paymentResult.getOutTradeNo(), paymentResult.isSuccess());
            map.addAttribute("orderDetail", orderDetail);
        }
        return "account/deposit_completed";
    }

    @RequestMapping(value = "/deposits")
    public String displayDeposits() {
        return "account/deposits";
    }

    @RequestMapping(path = "/deposits", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @JsonView(WebDataView.UI.class)
    public ResponseEntity<DataTablesResult<UserTransactionDetail>> deposits(
            final @CurrentUser User user,
            @RequestParam(name = "startDate", required = false) DateTime startDate,
            @RequestParam(name = "endDate", required = false) DateTime endDate,
            @ModelAttribute("parameter") DataTablesParameter parameter,
            @SortDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Sort sort,
            DataTablesResult<UserTransactionDetail> result) {
        Pageable pageable = new PageRequest(parameter.getPage(), parameter.getiDisplayLength(), sort);
        endDate = endDate != null ? endDate.plusDays(1).minusMillis(1) : null;
        Page<UserTransactionDetail> details = this.userTransactionDetailService.getAll(user, startDate, endDate, pageable);
        result.fillData(details, parameter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping("/child_users")
    public String childUsers(ModelMap map, final @CurrentUser User user, final @AuthenticationPrincipal LoginUser loginUser) {
        List<ChildUserDto> childUsers = Collections.emptyList();
        if (loginUser.getChildUsers() != null) {
            Iterable<Long> userIds = LoginUserUtils.getChildUserIds(loginUser);
            Collection<User> users = CollectionUtils.toList(userService.getAll(userIds));
            childUsers = loginUser.getChildUsers().stream().map(c -> LoginUserUtils.toChildUserDto(c, users)).collect(Collectors.toList());
        }
        map.addAttribute("user", user);
        map.addAttribute("childUsers", childUsers);
        return "account/child_users";
    }

    @RequestMapping(value = "/child_users/{userId}/assign_balance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> assignBalance(@Validated @ModelAttribute AssignBalanceDto assignBalanceDto,
                                              final @ChildUserVariable("userId") ChildUser childUser, final @CurrentUser User user) {
        User child = userService.findById(childUser.getUserId());
        if (child == null) {
            child = new User(childUser.getUserId(), childUser.getUsername());
            child = userService.save(child);
        }
        userService.assignBalance(user, child, assignBalanceDto.getCredits());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
