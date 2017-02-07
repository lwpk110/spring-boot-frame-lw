package cn.tendata.mdcs.mail.core;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import cn.tendata.mdcs.data.domain.MailDeliveryChannel;
import cn.tendata.mdcs.data.domain.MailDeliveryChannelNode;
import cn.tendata.mdcs.data.repository.MailDeliveryChannelRepository;

public class MailDeliveryChannelNodeChooserFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailDeliveryChannelNodeChooserFactory.class);
    
    private final MailDeliveryChannelRepository channelRepository;
    
    public MailDeliveryChannelNodeChooserFactory(MailDeliveryChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Transactional(readOnly=true)
    public MailDeliveryChannelNodeChooser getChooser(int channelId) {
        final MailDeliveryChannel channel = channelRepository.findOne(channelId);
        if(channel != null){
            if(channel.isDisabled()){
                LOGGER.warn("Mail delivery channel: {}[{}] is disabled.", channel.getName(), channel.getId());
            }
            else{
                List<MailDeliveryChannelNode> availableChannelNodes = new ArrayList<>(channel.getChannelNodes().size());
                for (MailDeliveryChannelNode channelNode : channel.getChannelNodes()) {
                    if(!channelNode.isDisabled()){
                        availableChannelNodes.add(channelNode);
                    }
                }
                if(availableChannelNodes.size() > 0){
                    return new BlockingQueueChannelNodeChooser(availableChannelNodes);
                }
            }
        }
        return new NullChannelNodeChooser();
    }
    
    public static class NullChannelNodeChooser implements MailDeliveryChannelNodeChooser {

        public MailDeliveryChannelNode choose() {
            return null;
        }

        public void restore(MailDeliveryChannelNode channelNode) {
            
        }
    }
}
