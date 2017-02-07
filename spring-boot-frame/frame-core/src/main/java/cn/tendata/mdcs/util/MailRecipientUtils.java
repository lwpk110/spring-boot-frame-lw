package cn.tendata.mdcs.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import cn.tendata.mdcs.data.domain.MailRecipient;
import cn.tendata.mdcs.data.domain.MailRecipientCollection;
import cn.tendata.mdcs.data.domain.UserMailRecipientGroup;

public abstract class MailRecipientUtils {

    public static int getCount(Collection<UserMailRecipientGroup> userMailRecipientGroups){
        int count = 0;
        if(userMailRecipientGroups != null && userMailRecipientGroups.size() > 0){
            for (UserMailRecipientGroup userMailRecipientGroup : userMailRecipientGroups) {
                count += userMailRecipientGroup.getRecipientCollection().getRecipientCount();
            }
        }
        return count;
    }
    
    public static MailRecipientCollection getRecipientCollection(Collection<UserMailRecipientGroup> userMailRecipientGroups){
        MailRecipientCollection recipientGroup = null;
        if(userMailRecipientGroups != null && userMailRecipientGroups.size() > 0){
            Set<MailRecipient> recipients = new HashSet<>(getCount(userMailRecipientGroups));
            for (UserMailRecipientGroup userMailRecipientGroup : userMailRecipientGroups) {
                recipients.addAll(userMailRecipientGroup.getRecipientCollection().getRecipients());
            }
            recipientGroup = new MailRecipientCollection(recipients);
        }
        return recipientGroup;
    }
    
    private MailRecipientUtils(){
        
    }
}
