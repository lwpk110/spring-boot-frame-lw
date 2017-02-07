package cn.tendata.mdcs.data.repository;

import com.mysema.query.types.Predicate;

import cn.tendata.mdcs.data.domain.QUserMailTemplate;
import cn.tendata.mdcs.data.domain.User;

public abstract class UserMailTemplatePredicates {
    
    public static Predicate user(User user){
        QUserMailTemplate mailTemplate = QUserMailTemplate.userMailTemplate;
        return mailTemplate.user.eq(user);
    }
    
    private UserMailTemplatePredicates(){
        
    }
}
