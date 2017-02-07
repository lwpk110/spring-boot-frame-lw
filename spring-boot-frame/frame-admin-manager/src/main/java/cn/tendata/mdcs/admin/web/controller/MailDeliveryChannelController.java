package cn.tendata.mdcs.admin.web.controller;

import javax.validation.Valid;

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

import cn.tendata.mdcs.admin.web.model.MailDeliveryChannelDto;
import cn.tendata.mdcs.admin.web.util.SecurityAccess;
import cn.tendata.mdcs.admin.web.util.ViewUtils;
import cn.tendata.mdcs.data.domain.MailDeliveryChannel;
import cn.tendata.mdcs.service.MailDeliveryChannelService;

@RestController("admin#mailDeliveryChannelController")
@PreAuthorize(SecurityAccess.HAS_PERMISSION_CHANNEL_MANAGE)
@RequestMapping(ViewUtils.ADMIN_PATH_PREFIX + "/mail-delivery-channels")
public class MailDeliveryChannelController {

	private final MailDeliveryChannelService mailDeliveryChannelService;
	
	@Autowired
	public MailDeliveryChannelController(MailDeliveryChannelService mailDeliveryChannelService) {
        this.mailDeliveryChannelService = mailDeliveryChannelService;
    }

    @PreAuthorize(SecurityAccess.HAS_PERMISSION_CHANNEL_VIEW)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<MailDeliveryChannel>> list ( 
			@PageableDefault (sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam String name) {
		
		Page<MailDeliveryChannel> items = mailDeliveryChannelService.getAll(name, pageable);
		return new ResponseEntity<Page<MailDeliveryChannel>>(items, HttpStatus.OK);
		
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> create (
			@Valid @RequestBody MailDeliveryChannelDto channelDto
			 ) { // create one data
		
		MailDeliveryChannel channel = new MailDeliveryChannel();
		channel.setName(channelDto.getName());
		channel.setDescription(channelDto.getDescription());
		channel.setFee(channelDto.getFee());
		channel.setMaxNumLimit(channelDto.getMaxNumLimit()); 
		channel.setDisabled(true); 
        mailDeliveryChannelService.save(channel);
        
		return new ResponseEntity<Void>(HttpStatus.CREATED);
		
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update (
			@Valid @RequestBody MailDeliveryChannelDto channelDto,
			@PathVariable("id") MailDeliveryChannel channel) { // update one data

		channel.setName(channelDto.getName());
		channel.setDescription(channelDto.getDescription());
		channel.setFee(channelDto.getFee());
		channel.setMaxNumLimit(channelDto.getMaxNumLimit()); 
        mailDeliveryChannelService.save(channel);
        
		return new ResponseEntity<Void>(HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/{id}/change-disabled", method = RequestMethod.PUT)
	public ResponseEntity<MailDeliveryChannel> changeDisabled (
			 @PathVariable("id") MailDeliveryChannel channel,
			 @RequestParam Boolean disabled) { // change status

		channel.setDisabled(disabled); 
		mailDeliveryChannelService.save(channel);
        
        return new ResponseEntity<MailDeliveryChannel>(channel, HttpStatus.OK);
        
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<MailDeliveryChannel> getById (
			 @PathVariable("id") MailDeliveryChannel channel) { // get channel by id
		
        return new ResponseEntity<MailDeliveryChannel>(channel, HttpStatus.OK);
        
	}
	
}
