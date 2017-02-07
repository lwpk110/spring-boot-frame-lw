package cn.tendata.mdcs.data.repository;

import com.mysema.query.types.OrderSpecifier;

import cn.tendata.mdcs.data.domain.QMailDeliveryChannel;

public abstract class MailDeliveryChannelOrderSpecifiers {

    public static OrderSpecifier<Integer> idDesc(){
        return QMailDeliveryChannel.mailDeliveryChannel.id.desc();
    }
    
    public static OrderSpecifier<Integer> sequenceDesc(){
        return QMailDeliveryChannel.mailDeliveryChannel.sequence.desc();
    }
    
    private MailDeliveryChannelOrderSpecifiers(){
        
    }
}
