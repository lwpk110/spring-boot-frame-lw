package cn.tendata.mdcs.web.model;

public class ChildUserDto {

    private final long userId;
    private final String username;
    private final int credits;
    
    public ChildUserDto(long userId, String username, int credits) {
        this.userId = userId;
        this.username = username;
        this.credits = credits;
    }

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public int getCredits() {
        return credits;
    }
}
