package cn.tendata.mdcs.admin.web.controller;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.tendata.mdcs.admin.web.model.MailDeliveryChannelNodeDto;
import cn.tendata.mdcs.admin.web.util.SecurityAccess;
import cn.tendata.mdcs.admin.web.util.StringJsonToMapConverter;
import cn.tendata.mdcs.admin.web.util.ViewUtils;
import cn.tendata.mdcs.data.domain.MailDeliveryChannel;
import cn.tendata.mdcs.data.domain.MailDeliveryChannelNode;
import cn.tendata.mdcs.service.MailDeliveryChannelNodeService;

@RestController("admin#mailDeliveryChannelNodeController")
@PreAuthorize(SecurityAccess.HAS_PERMISSION_CHANNEL_MANAGE)
@RequestMapping(ViewUtils.ADMIN_PATH_PREFIX + "/mail-delivery-channels")
public class MailDeliveryChannelNodeController {

	private final MailDeliveryChannelNodeService mailDeliveryChannelNodeService;
	
	@Autowired
	public MailDeliveryChannelNodeController(MailDeliveryChannelNodeService mailDeliveryChannelNodeService) {
        this.mailDeliveryChannelNodeService = mailDeliveryChannelNodeService;
    }

	@PreAuthorize(SecurityAccess.HAS_PERMISSION_CHANNEL_VIEW)
    @RequestMapping("/{channelId}/nodes")
	public ResponseEntity<List<MailDeliveryChannelNode>> list (
			@PathVariable("channelId") MailDeliveryChannel channel) {

		List<MailDeliveryChannelNode> items = mailDeliveryChannelNodeService
				.getAll(channel);
		
		if (null == items) {
			return new ResponseEntity<List<MailDeliveryChannelNode>>(Collections.<MailDeliveryChannelNode>emptyList(), HttpStatus.OK);
		}
		
		return new ResponseEntity<List<MailDeliveryChannelNode>> (items, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/{channelId:[1-9]\\d*}/nodes/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update (
				@Valid @RequestBody MailDeliveryChannelNodeDto channelNodeDto,
				@PathVariable("id") MailDeliveryChannelNode channelNode
			 ) { // update one data
		
		channelNode.setName(channelNodeDto.getName());
		channelNode.setConfigProps(new StringJsonToMapConverter().convert(channelNodeDto.getConfigProps()));
		channelNode.setServerKey(channelNodeDto.getServerKey());
		channelNode.setNeedCampaigns(channelNodeDto.isNeedCampaigns());
        mailDeliveryChannelNodeService.save(channelNode);
        
        return new ResponseEntity<Void> (HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/{channelId}/nodes", method = RequestMethod.POST)
	public ResponseEntity<Void> create (
				@Valid @RequestBody MailDeliveryChannelNodeDto channelNodeDto,
				@PathVariable("channelId") MailDeliveryChannel channel
			) { // create one data
		 
		MailDeliveryChannelNode channelNode = new MailDeliveryChannelNode();
		channelNode.setChannel(channel);
		channelNode.setName(channelNodeDto.getName());
		channelNode.setConfigProps(new StringJsonToMapConverter().convert(channelNodeDto.getConfigProps())); 
		channelNode.setServerKey(channelNodeDto.getServerKey()); 
		channelNode.setDisabled(true);
		channelNode.setNeedCampaigns(channelNodeDto.isNeedCampaigns());
        mailDeliveryChannelNodeService.save(channelNode);
        
        return new ResponseEntity<Void> (HttpStatus.CREATED);
		
	}
	
	@RequestMapping(value = "/{channelId:[1-9]\\d*}/nodes/{id}/change-disabled", method = RequestMethod.PUT)
	public ResponseEntity<MailDeliveryChannelNode> changeDisabled (
			@PathVariable("id") MailDeliveryChannelNode channelNode,
			@RequestParam("disabled") Boolean disabled) { // change status
		
		channelNode.setDisabled(disabled); 
        mailDeliveryChannelNodeService.save(channelNode);
        
        return new ResponseEntity<MailDeliveryChannelNode> (channelNode, HttpStatus.OK);
        
	}
	
	@RequestMapping(value = "/{channelId:[1-9]\\d*}/nodes/{id}", method = RequestMethod.GET)
	public ResponseEntity<MailDeliveryChannelNode> getById (
			@PathVariable("id") MailDeliveryChannelNode channelNode) { // get node by id
		
        return new ResponseEntity<MailDeliveryChannelNode> (channelNode, HttpStatus.OK);
        
	}
	
}
