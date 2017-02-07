package cn.tendata.mdcs.mail.core;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import cn.tendata.mdcs.data.domain.MailDeliveryChannelNode;

public class BlockingQueueChannelNodeChooser implements MailDeliveryChannelNodeChooser {

    private final BlockingQueue<MailDeliveryChannelNode> channelNodeQueue;
    
    public BlockingQueueChannelNodeChooser(Collection<MailDeliveryChannelNode> channelNodes) {
        this.channelNodeQueue = new LinkedBlockingQueue<>(channelNodes);
    }

    public MailDeliveryChannelNode choose() {
        try {
            return channelNodeQueue.poll(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new MailDeliveryChannelNodeException("blocking queue poll time out", e);
        }
    }
    
    public void restore(MailDeliveryChannelNode channelNode) {
        channelNodeQueue.offer(channelNode);
    }
}
