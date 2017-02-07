package cn.tendata.mdcs.core;

import java.util.List;

import org.joda.time.DateTime;

import cn.tendata.mdcs.mail.MailRecipientAction;
import cn.tendata.mdcs.service.model.MailDeliveryBounceReport;

public interface MailDeliveryBounceReportManager {

	List<MailDeliveryBounceReport> getRecipientBounceSummaryReport(DateTime start, DateTime end);
	
	List<MailRecipientAction> getAllBounceByRecipientsMailSuffix(String recipientsMailSuffix, DateTime start, DateTime end);

}
