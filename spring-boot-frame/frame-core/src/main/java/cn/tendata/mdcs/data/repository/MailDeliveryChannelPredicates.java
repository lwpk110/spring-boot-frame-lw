package cn.tendata.mdcs.data.repository;

import com.mysema.query.types.Predicate;

import cn.tendata.mdcs.data.domain.QMailDeliveryChannel;

public abstract class MailDeliveryChannelPredicates {

    public static Predicate channels(boolean disabled){
        QMailDeliveryChannel mailDeliveryChannel = QMailDeliveryChannel.mailDeliveryChannel;
        return mailDeliveryChannel.disabled.eq(disabled);
    }
    
    private MailDeliveryChannelPredicates(){
        
    }
}
