package cn.tendata.mdcs.data.repository;

import org.joda.time.DateTime;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;

import cn.tendata.mdcs.data.domain.QUserTransactionDetail;
import cn.tendata.mdcs.service.model.SearchKeywordType;

public abstract class UserTransactionDetailPredicates {

    public static Predicate list(SearchKeywordType type, String keyword, DateTime start, DateTime end){
        QUserTransactionDetail userTransactionDetail = QUserTransactionDetail.userTransactionDetail;
        BooleanExpression expr = null;
        if(type != null && StringUtils.hasText(keyword)){
            switch (type) {
            case USER_ID:
                expr = userTransactionDetail.user.id.eq(NumberUtils.parseNumber(keyword, Long.class));
                break;
            case USERNAME:
                expr = userTransactionDetail.user.username.like("%" + keyword + "%");
                break;
            }
        }
        if(start != null){
            BooleanExpression startExpr = userTransactionDetail.createdDate.goe(start);
            expr = expr != null ? expr.and(startExpr) : startExpr;
        }
        if(end != null){
            BooleanExpression endExpr = userTransactionDetail.createdDate.loe(end);
            expr = expr != null ? expr.and(endExpr) : endExpr;
        }
        return expr;
    }
    
    private UserTransactionDetailPredicates(){
        
    }
}
