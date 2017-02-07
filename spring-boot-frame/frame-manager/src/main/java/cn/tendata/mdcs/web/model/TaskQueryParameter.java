package cn.tendata.mdcs.web.model;

import cn.tendata.mdcs.util.DateTimeQuery;
import cn.tendata.mdcs.util.UserQuery;
import org.joda.time.DateTime;

public class TaskQueryParameter {

    private DateTime startDate;
    private DateTime endDate;
    private String taskName;
    private long targetUserId;

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate.plusDays(1).minusMillis(1);
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(long targetUserId) {
        this.targetUserId = targetUserId;
    }


    public DateTimeQuery getDateTimeQuery() {
        if (getStartDate() == null || getEndDate() == null) {
            return null;
        }
        return new DateTimeQuery(getStartDate(), getEndDate());
    }

    public UserQuery getUserQuery(long parentUserId) {
        return new UserQuery(getTargetUserId(), parentUserId);
    }
}
