package cn.tendata.mdcs.data.repository;

import cn.tendata.mdcs.data.domain.MailDeliveryChannelNode;
import cn.tendata.mdcs.data.domain.QUserMailDeliveryTask;
import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.service.model.SearchKeywordType;
import cn.tendata.mdcs.util.DateTimeQuery;
import cn.tendata.mdcs.util.UserQuery;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.StringPath;
import java.util.UUID;
import org.joda.time.DateTime;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

public abstract class UserMailDeliveryTaskPredicates {

    public static Predicate user(User user) {
        QUserMailDeliveryTask mailDeliveryTask = QUserMailDeliveryTask.userMailDeliveryTask;
        return mailDeliveryTask.user.eq(user);
    }

    public static Predicate listByUser(User user, DateTimeQuery dateTimeQuery, String taskName, UserQuery userQuery) {
        QUserMailDeliveryTask mailDeliveryTask = QUserMailDeliveryTask.userMailDeliveryTask;
        BooleanExpression expr = null;
        if (userQuery != null) {
            if (userQuery.getParentUserId() > 0) {
                expr = mailDeliveryTask.user.eq(user);
            } else if (userQuery.getTargetUserId() > 0) {
                if (userQuery.getTargetUserId() == user.getId()) {
                    expr = mailDeliveryTask.user.eq(user);
                } else {
                    expr = mailDeliveryTask.user.id.eq(userQuery.getTargetUserId())
                            .and(mailDeliveryTask.parentUserId.eq(user.getId()));
                }
            } else {
                expr = mailDeliveryTask.user.eq(user).or(mailDeliveryTask.parentUserId.eq(user.getId()));
            }
        } else {
            expr = mailDeliveryTask.user.eq(user);
        }
        if (dateTimeQuery != null) {
            expr = expr.and(
                    mailDeliveryTask.createdDate.between(dateTimeQuery.getStartDate(), dateTimeQuery.getEndDate()));
        }
        if (StringUtils.hasText(taskName)) {
            expr = expr.and(mailDeliveryTask.name.like("%" + taskName.trim() + "%"));
        }
        return expr;
    }

    public static Predicate list(SearchKeywordType type, String keyword, DateTime start, DateTime end) {
        QUserMailDeliveryTask userMailDeliveryTask = QUserMailDeliveryTask.userMailDeliveryTask;
        BooleanExpression expr = null;
        if (type != null && StringUtils.hasText(keyword)) {
            switch (type) {
                case USER_ID:
                    expr = userMailDeliveryTask.user.id.eq(NumberUtils.parseNumber(keyword, Long.class));
                    break;
                case USERNAME:
                    expr = userMailDeliveryTask.user.username.eq(keyword);
                    break;
                case TASK_ID:
                    expr = userMailDeliveryTask.id.eq(UUID.fromString(keyword));
                    break;
            }
        }
        if (start != null) {
            BooleanExpression startExpr = userMailDeliveryTask.createdDate.goe(start);
            expr = expr != null ? expr.and(startExpr) : startExpr;
        }
        if (end != null) {
            BooleanExpression endExpr = userMailDeliveryTask.createdDate.loe(end);
            expr = expr != null ? expr.and(endExpr) : endExpr;
        }
        return expr;
    }

    private UserMailDeliveryTaskPredicates() {

    }

    public static Predicate list(DateTime start, DateTime end) {
        QUserMailDeliveryTask userMailDeliveryTask = QUserMailDeliveryTask.userMailDeliveryTask;
        BooleanExpression expr = null;
        if (start != null) {
            BooleanExpression startExpr = userMailDeliveryTask.createdDate.goe(start);
            expr = expr != null ? expr.and(startExpr) : startExpr;
        }
        if (end != null) {
            BooleanExpression endExpr = userMailDeliveryTask.createdDate.loe(end);
            expr = expr != null ? expr.and(endExpr) : endExpr;
        }
        return expr;
    }

    public static Predicate list(DateTime start, DateTime end, MailDeliveryChannelNode mailDeliveryChannelNode) {
        QUserMailDeliveryTask userMailDeliveryTask = QUserMailDeliveryTask.userMailDeliveryTask;
        BooleanExpression expr = null;
        if (start != null) {
            BooleanExpression startExpr = userMailDeliveryTask.createdDate.goe(start);
            expr = expr != null ? expr.and(startExpr) : startExpr;
        }
        if (end != null) {
            BooleanExpression endExpr = userMailDeliveryTask.createdDate.loe(end);
            expr = expr != null ? expr.and(endExpr) : endExpr;
        }
        if (null != mailDeliveryChannelNode) {
            BooleanExpression nodeExpr = userMailDeliveryTask.deliveryChannelNode.eq(mailDeliveryChannelNode);
            expr = expr != null ? expr.and(nodeExpr) : nodeExpr;
        }
        return expr;
    }

    public static Predicate listForTaskId(String taskId) {
        BooleanExpression expr = null;
        if (StringUtils.hasText(taskId)) {
            expr = expr != null ? new StringPath(taskId).eq(taskId) : expr;
        }
        return expr;
    }
}
