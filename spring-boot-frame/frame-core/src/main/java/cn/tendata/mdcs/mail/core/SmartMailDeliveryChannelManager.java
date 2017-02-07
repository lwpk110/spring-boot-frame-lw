package cn.tendata.mdcs.mail.core;

import java.util.HashMap;
import java.util.Map;

import cn.tendata.mdcs.data.domain.MailDeliveryChannelNode;

public class SmartMailDeliveryChannelManager extends MailDeliveryChannelManager {

    private final Map<Integer, MailDeliveryChannelNodeChooser> channelNodeChooserMap = new HashMap<>();
    
    private final MailDeliveryChannelNodeChooserFactory channelNodeChooserFactory;
    
    public SmartMailDeliveryChannelManager(MailDeliveryChannelNodeChooserFactory channelNodeChooserFactory) {
        this.channelNodeChooserFactory = channelNodeChooserFactory;
    }

    public MailDeliveryChannelNode retrieve(final int channelId) {
        MailDeliveryChannelNodeChooser channelNodeChooser = channelNodeChooserMap.get(channelId);
        if(channelNodeChooser == null){
            synchronized (channelNodeChooserMap) {
                channelNodeChooser = channelNodeChooserMap.get(channelId);
                if(channelNodeChooser == null){
                    channelNodeChooser = channelNodeChooserFactory.getChooser(channelId);
                    channelNodeChooserMap.put(channelId, channelNodeChooser);
                }
            }
        }
        return channelNodeChooser.choose();
    }
    
    public void release(final MailDeliveryChannelNode channelNode) {
        final MailDeliveryChannelNodeChooser channelNodeChooser = channelNodeChooserMap.get(channelNode.getChannel().getId());
        if(channelNodeChooser != null){
            channelNodeChooser.restore(channelNode);
        }
    }
}
