package cn.tendata.ftp.webpower.util;

import cn.tendata.mdcs.data.elasticsearch.domain.MailRecipientActionDocument;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jeashi on 2016/9/21.
 */
public class MapList extends ArrayList {
    HashMap<Integer, MailRecipientActionDocument> hm = new HashMap(2000, 0.75f); //创建一个2000容量的HashMap

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public boolean add(MailRecipientActionDocument mailRecipientActionDocument) {
        MailRecipientActionDocument userfulMailRecipientActionDocument = null;
        int emailKey = hash(mailRecipientActionDocument.getEmail());
        if (hm.containsKey(emailKey)) {
            MailRecipientActionDocument old = hm.get(emailKey);
            userfulMailRecipientActionDocument = compare(old, mailRecipientActionDocument);
        }
        hm.put(emailKey, userfulMailRecipientActionDocument);

        return super.add(userfulMailRecipientActionDocument);
    }

    private MailRecipientActionDocument compare(MailRecipientActionDocument oldMailRecipientActionDocument,
            MailRecipientActionDocument newMailRecipientActionDocument) {
        if (oldMailRecipientActionDocument.getActionDate().isAfter(newMailRecipientActionDocument.getActionDate())) {
            return oldMailRecipientActionDocument;
        } else {
            super.remove(oldMailRecipientActionDocument);
            return newMailRecipientActionDocument;
        }
    }
}
