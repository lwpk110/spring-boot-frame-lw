package cn.tendata.ftp.webpower.util;

import cn.tendata.mdcs.mail.MailRecipientAction.MailRecipientActionStatus;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ernest on 2016/10/13.
 */
public class WebpowerRecipientActionStateUtils {

    public static final int PROCESS_FLAG = 1;
    public static final int FINAL_FLAG = 2;
    protected static Map<Integer, MailRecipientActionStatus[]> stateMap = new HashMap<>();
    /**
     * 1.两种状态，过程状态和最终状态 2.先过滤内存中的记录再过滤es中的
     */

    public static final MailRecipientActionStatus[] PROCESS_STATE = {
            MailRecipientActionStatus.SENT_SUCCESS,
            MailRecipientActionStatus.OPEN,
            MailRecipientActionStatus.CLICK
    };
    public static final MailRecipientActionStatus[] FINAL_STATE = {
            MailRecipientActionStatus.HARD_BOUNCE,
            MailRecipientActionStatus.SOFT_BOUNCE,
            MailRecipientActionStatus.UNSUBSCRIBE,
            MailRecipientActionStatus.SPAM_COMPLAINT
    };

    static {
        stateMap.put(PROCESS_FLAG, PROCESS_STATE);
        stateMap.put(FINAL_FLAG, FINAL_STATE);
    }

    public static boolean isContain(MailRecipientActionStatus mailRecipientActionStatus,
            int stateFlag) {
        boolean f = false;
        List<MailRecipientActionStatus> stateList;
        MailRecipientActionStatus[] resolveArr = choose(stateFlag);
        stateList = Arrays.asList(resolveArr);
        if (stateList.contains(mailRecipientActionStatus)) {
            f = true;
        }
        return f;
    }

    /**
     * p判断m1状态的index > m2 的index
     */
    public static boolean isAfter(MailRecipientActionStatus m1,
            MailRecipientActionStatus m2, int stateFlag) {
        boolean f = false;
        MailRecipientActionStatus[] resolveArr = choose(stateFlag);
        if (getIndex(resolveArr, m1) > getIndex(resolveArr, m2)) {
            f = true;
        }
        return f;
    }

    /**
     * 判断 index(m1) == index(m2)
     */
    public static boolean isEquals(MailRecipientActionStatus m1,
            MailRecipientActionStatus m2, int stateFlag) {
        boolean f = false;
        MailRecipientActionStatus[] resolveArr = choose(stateFlag);
        if (getIndex(resolveArr, m1) == getIndex(resolveArr, m2)) {
            f = true;
        }
        return f;
    }

    public static int getIndex(MailRecipientActionStatus[] resolveArr, MailRecipientActionStatus actionStatus) {
        MailRecipientActionStatusWrapper actionStatusWrapper =
                new MailRecipientActionStatusWrapper(resolveArr, actionStatus);
        return actionStatusWrapper.getIndex();
    }

    private static MailRecipientActionStatus[] choose(int stateFlag) {
        return stateMap.get(stateFlag);
    }
}
