package cn.tendata.mdcs.service.model;

import cn.tendata.mdcs.mail.MailRecipientAction.MailRecipientActionStatus;

public class MailDeliveryBounceReport {

	private int total;

	private int hardBounce;

	private int softBounce;

	private String recipientsMailSuffix;

	private int bounceRate;

	public MailDeliveryBounceReport() {

	}

	public MailDeliveryBounceReport(MailRecipientActionStatus actionStatus, String recipientsMailSuffix) {
		this.recipientsMailSuffix = recipientsMailSuffix;
		this.total = 1;
		if (actionStatus.equals(MailRecipientActionStatus.HARD_BOUNCE)) {
			this.hardBounce = 1;
			this.softBounce = 0;
			bounceRate = 1;
		} else if (actionStatus.equals(MailRecipientActionStatus.SOFT_BOUNCE)) {
			this.hardBounce = 0;
			this.softBounce = 1;
			bounceRate = 1;
		} else {
			this.hardBounce = 0;
			this.softBounce = 0;
			bounceRate = 0;
		}

	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getHardBounce() {
		return hardBounce;
	}

	public void setHardBounce(int hardBounce) {
		this.hardBounce = hardBounce;
	}

	public int getSoftBounce() {
		return softBounce;
	}

	public void setSoftBounce(int softBounce) {
		this.softBounce = softBounce;
	}

	public String getRecipientsMailSuffix() {
		return recipientsMailSuffix;
	}

	public void setRecipientsMailSuffix(String recipientsMailSuffix) {
		this.recipientsMailSuffix = recipientsMailSuffix;
	}

	public int getBounceRate() {
		return bounceRate;
	}

	public void setBounceRate(int bounceRate) {
		this.bounceRate = bounceRate;
	}

}
