package cn.tendata.mdcs.data.repository;

import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;

import cn.tendata.mdcs.data.domain.QUser;
import cn.tendata.mdcs.service.model.SearchKeywordType;

public abstract class UserPredicates {
    
    public static Predicate search(SearchKeywordType type, String keyword){
        QUser user = QUser.user;
        BooleanExpression expr = null;
        if(type != null && StringUtils.hasText(keyword)){
            switch (type) {
            case USER_ID:
                expr = user.id.eq(NumberUtils.parseNumber(keyword, Long.class));
                break;
            case USERNAME:
                expr = user.username.like("%" + keyword + "%");
                break;
            }
        }
        return expr;
    }
    
    private UserPredicates(){
        
    }
}
