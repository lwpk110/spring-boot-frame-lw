package cn.tendata.mdcs.admin.web.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MailDeliveryChannelDto {

	private String name;
	private String description;
	private int fee;
	private int maxNumLimit;
	
	public MailDeliveryChannelDto() {}
	
	public MailDeliveryChannelDto(String name, String description, int fee, int maxNumLimit) {
		this.name = name;
		this.description = description;
		this.fee = fee;
		this.maxNumLimit = maxNumLimit;
	}
	
	@NotNull
    @Size(min = 1, max = 100)
	public String getName() {
		return name;
	}
	
	@Size(max = 255)
	public String getDescription() {
		return description;
	}
	
	@Min(0)
	public int getFee() {
		return fee;
	}
	
	@NotNull
	public int getMaxNumLimit() {
		return maxNumLimit;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public void setMaxNumLimit(int maxNumLimit) {
		this.maxNumLimit = maxNumLimit;
	}
	
}
