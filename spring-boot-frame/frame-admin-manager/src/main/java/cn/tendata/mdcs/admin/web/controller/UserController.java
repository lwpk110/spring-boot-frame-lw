package cn.tendata.mdcs.admin.web.controller;

import javax.validation.Valid;

import org.jasig.inspektr.audit.annotation.Audit;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.tendata.mdcs.admin.web.util.SecurityAccess;
import cn.tendata.mdcs.admin.web.util.ViewUtils;
import cn.tendata.mdcs.audit.spi.AuditActionResolvers;
import cn.tendata.mdcs.audit.spi.AuditParam;
import cn.tendata.mdcs.audit.spi.AuditResourceResolvers;
import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserTransactionDetail;
import cn.tendata.mdcs.service.UserDepositService;
import cn.tendata.mdcs.service.UserService;
import cn.tendata.mdcs.service.UserTransactionDetailService;
import cn.tendata.mdcs.service.model.DirectDepositDetail;
import cn.tendata.mdcs.service.model.SearchKeywordType;

@RestController("admin#userController")
@RequestMapping(ViewUtils.ADMIN_PATH_PREFIX + "/users")
public class UserController {
	
	private final UserService userService;
	private final UserDepositService userDepositService;
	private final UserTransactionDetailService userTransactionDetailService;
	
	@Autowired
	public UserController(UserService userService, UserDepositService userDepositService, 
			UserTransactionDetailService userTransactionDetailService) {
        this.userService = userService;
        this.userDepositService = userDepositService;
        this.userTransactionDetailService = userTransactionDetailService;
    }

	@PreAuthorize(SecurityAccess.HAS_PERMISSION_ADMIN_USER_VIEW)
    @RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<Page<User>> list ( 
			@PageableDefault (sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam(required=false, name="keyword") String keyword,
			@RequestParam("type") SearchKeywordType type) {
		
		Page<User> items = userService.search(type, keyword, pageable);
		if (null == items) {
			return new ResponseEntity<Page<User>> (HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Page<User>> (items, HttpStatus.OK);
		
	}

	@Audit(action = "USER_DEPOSIT", actionResolverName = AuditActionResolvers.DEFAULT_ACTION_RESOLVER,
			resourceResolverName = AuditResourceResolvers.ANNOTATED_PARAMS_AS_STRING_RESOURCE_RESOLVER)
	@PreAuthorize(SecurityAccess.HAS_PERMISSION_ADMIN_USER_DEPOSIT)
	@RequestMapping(value = "/{userId}/deposit", method = RequestMethod.POST)
	public ResponseEntity<DirectDepositDetail> deposit (
			@PathVariable("userId") long userId,
			@RequestParam(value = "username",required = false) String username,
			@Valid @RequestBody @AuditParam DirectDepositDetail deposit ) {
		User user = this.userService.findById(userId);
		if (user == null) {
			user = new User(userId, username);
		}
		deposit.setUser(user);
		userDepositService.deposit(deposit);

		return new ResponseEntity<DirectDepositDetail>(deposit, HttpStatus.OK);

	}
	
	@PreAuthorize(SecurityAccess.HAS_PERMISSION_ADMIN_USER_VIEW)
    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
	public ResponseEntity<Page<UserTransactionDetail>> listTransactionDetail(
			@PageableDefault (sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam(required=false, name="type") SearchKeywordType type, 
			@RequestParam(required=false, name="keyword") String keyword, 
			@RequestParam(required=false, name="start") DateTime start, 
			@RequestParam(required=false, name="end") DateTime end) {
		
		Page<UserTransactionDetail> items = userTransactionDetailService.getAll(type, keyword, start, end, pageable);
		if (null == items) {
			return new ResponseEntity<Page<UserTransactionDetail>> (HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Page<UserTransactionDetail>> (items, HttpStatus.OK);
		
	}
	
}
