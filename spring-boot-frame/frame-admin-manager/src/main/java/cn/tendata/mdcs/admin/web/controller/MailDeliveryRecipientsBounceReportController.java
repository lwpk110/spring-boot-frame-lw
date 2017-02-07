package cn.tendata.mdcs.admin.web.controller;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.tendata.mdcs.admin.web.util.SecurityAccess;
import cn.tendata.mdcs.admin.web.util.ViewUtils;
import cn.tendata.mdcs.mail.MailRecipientAction;
import cn.tendata.mdcs.service.UserMailDeliveryTaskReportService;
import cn.tendata.mdcs.service.model.MailDeliveryBounceReport;

@Controller("admin#mailDeliveryBounceReportController")
@PreAuthorize(SecurityAccess.HAS_PERMISSION_ADMIN_REPORT_VIEW)
@RequestMapping(ViewUtils.ADMIN_PATH_PREFIX + "/mail-delivery-bounce-report")
public class MailDeliveryRecipientsBounceReportController {

	private final UserMailDeliveryTaskReportService userMailDeliveryTaskReportService;

	@Autowired
	protected MailDeliveryRecipientsBounceReportController(
			UserMailDeliveryTaskReportService userMailDeliveryTaskReportService) {
		this.userMailDeliveryTaskReportService = userMailDeliveryTaskReportService;
	}

	@RequestMapping(value = "/bounce/recipientBounceSummaryReport", method = RequestMethod.GET)
	public ResponseEntity<List<MailDeliveryBounceReport>> list(@RequestParam DateTime start, @RequestParam DateTime end) {

		List<MailDeliveryBounceReport> rpts = userMailDeliveryTaskReportService.getRecipientBounceSummaryReport(start,end);
		return new ResponseEntity<List<MailDeliveryBounceReport>>(rpts, HttpStatus.OK);

	}

	@RequestMapping(value = "/bounce/recipientBounce")
	public ResponseEntity<List<MailRecipientAction>> recipientBounceList(@RequestParam String recipientsMailSuffix,
			@RequestParam DateTime start, @RequestParam DateTime end) {

		List<MailRecipientAction> recipientBounceList = userMailDeliveryTaskReportService.getAllBounceByRecipientsMailSuffix(
				recipientsMailSuffix, start, end);
		return new ResponseEntity<List<MailRecipientAction>>(recipientBounceList, HttpStatus.OK);
	}
}
