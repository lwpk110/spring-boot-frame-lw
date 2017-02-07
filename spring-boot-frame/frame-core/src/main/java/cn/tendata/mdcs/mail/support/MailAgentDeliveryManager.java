package cn.tendata.mdcs.mail.support;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by jeashi on 2016/7/14.
 */
public class MailAgentDeliveryManager {

    private static final String[] SUFFIX_KEYS = {"group", "china", "abroad"};

    private final DomainSuffixResolver domainSuffixResolver;

    public MailAgentDeliveryManager(DomainSuffixResolver domainSuffixResolver) {
        this.domainSuffixResolver = domainSuffixResolver;
    }

    public String getSenderEmail(String mail, String mailAgentDomain) {
        String[] mailParts = mail.split("@");
        for (String suffixKey : SUFFIX_KEYS) {
            String[] suffixArray = domainSuffixResolver.resolve(suffixKey);
            if (null == suffixArray || suffixArray.length == 0) {
                continue;
            }
            for (int i = 0; i < suffixArray.length; i++) {
                String suffixUnit = suffixArray[i].trim();
                if(StringUtils.isEmpty(suffixUnit)){
                    continue;
                }
                if (mailParts[1].endsWith(suffixUnit)) {
                    String middleEmailStr = mailParts[1].substring(0, mailParts[1].lastIndexOf(suffixUnit));
                    if (middleEmailStr.indexOf('.') > 1) {
                        middleEmailStr = middleEmailStr.substring(middleEmailStr.lastIndexOf('.') + 1, middleEmailStr.length());
                    }
                    return mailParts[0] + "@" + middleEmailStr + "." + mailAgentDomain;
                }
            }
        }
        String middleEmailStr = mailParts[1].substring(0, mailParts[1].lastIndexOf('.'));
        return mailParts[0] + "@" + middleEmailStr + "." + mailAgentDomain;
    }
}
