package cn.tendata.mdcs.mail.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

public class MailDeliveryServerProxyFactory {

    private final Map<String, MailDeliveryServerProxy> serverMap;
    
    public MailDeliveryServerProxyFactory(Collection<MailDeliveryServerProxy> servers) {
        Assert.notEmpty(servers, "'servers' must have elements");
        this.serverMap = new HashMap<>(servers.size());
       /* for (MailDeliveryServerProxy server : servers) {
            serverMap.put(server.getServerKey(), server);
        }*/
    }

    public final MailDeliveryServerProxy getServerProxy(final String serverKey){
        MailDeliveryServerProxy serverProxy = serverMap.get(serverKey);
        if(serverProxy == null){
            throw new MailDeliveryServerNotFoundException("server key:" + serverKey);
        }
        return serverProxy;
    }
}
