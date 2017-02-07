package cn.tendata.mdcs.web.util;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.tendata.cas.client.security.core.userdetails.LoginUser;
import cn.tendata.cas.client.security.core.userdetails.LoginUser.ChildUser;
import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.web.model.ChildUserDto;

public abstract class LoginUserUtils {

    public static Iterable<Long> getChildUserIds(LoginUser loginUser){
        return loginUser.getChildUsers().stream().map(ChildUser::getUserId).collect(Collectors.toList());
    }
    
    public static ChildUserDto toChildUserDto(ChildUser childUser, Collection<? extends User> users){
        int credits = 0;
        if(users != null){
            final Optional<? extends User> first = users.stream().filter(u -> u.getId().equals(childUser.getUserId())).findFirst();
            if(first.isPresent()){
                credits = first.get().getBalance();
            }
        }
        return new ChildUserDto(childUser.getUserId(), childUser.getUsername(), credits);
    }
    
    public static ChildUser getChildUser(LoginUser loginUser, long childUserId){
        ChildUser childUser = null;
        if(loginUser.getChildUsers() != null){
             Optional<? extends ChildUser> first = loginUser.getChildUsers().stream().filter(c -> c.getUserId() == childUserId).findFirst();
             childUser = first.isPresent() ? first.get() : null;
        }
        return childUser;
    }
    
    private LoginUserUtils(){
        
    }
}
