package cn.tendata.mdcs.web.util;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.util.Assert;

import cn.tendata.mdcs.data.domain.MailRecipient;
import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.mail.MailRecipientAction;
import cn.tendata.mdcs.mail.MailRecipientAction.MailRecipientActionStatus;

public abstract class MailRecipientActionUtils {

    public static Collection<MailRecipientAction> filterByActionStatus(final List<MailRecipientAction> recipientActions,
            final EnumSet<MailRecipientActionStatus> actionStatusSet) {
        Assert.notNull(recipientActions, "'recipientActions' must not be null");
        Assert.notNull(actionStatusSet, "'actionStatusSet' must not be null");
        if (!actionStatusSet.containsAll(EnumSet.allOf(MailRecipientActionStatus.class))) {
            return Collections.unmodifiableCollection(
                    recipientActions.stream()
                        .filter(a -> actionStatusSet.contains(a.getActionStatus()))
                        .collect(Collectors.toSet()));
        }
        return Collections.unmodifiableList(recipientActions);
    }
    
    public static Collection<MailRecipient> getMailRecipients(final UserMailDeliveryTask task, final List<MailRecipientAction> recipientActions,
            final EnumSet<MailRecipientActionStatus> actionStatusSet) {
        Assert.notNull(task, "'task' must not be null");
        Collection<MailRecipientAction> filteredList = filterByActionStatus(recipientActions, actionStatusSet);
        if(filteredList.size() > 0){
            Map<String, MailRecipientAction> dict = filteredList.stream().collect(
                    Collectors.toMap(MailRecipientAction::getEmail, Function.identity()));
            return Collections.unmodifiableList(
                    task.getRecipientCollection().getRecipients().stream()
                        .filter(x -> dict.containsKey(x.getEmail()))
                        .collect(Collectors.toList()));
        }
        return Collections.emptyList();
    }
    
    private MailRecipientActionUtils(){
        
    }
}
