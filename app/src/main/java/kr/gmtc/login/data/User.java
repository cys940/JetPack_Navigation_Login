package kr.gmtc.login.data;


import java.util.Date;
import java.util.HashMap;

public class User {
    private String userId;
    private String userPw;
    private String userPinNm;
    private String userPattern;
    private Date createDT = new Date();
    private HashMap<Integer, UserPastLog> userPastPwLogs = new HashMap<>();
    private HashMap<Integer, UserPastLog> userPastPinLogs = new HashMap<>();
    private HashMap<Integer, UserPastLog> userPastPatternLogs = new HashMap<>();

    public String getUserId() {
        return userId;
    }

    public User setUserId(String userId) {
        this.userId = userId;

        return this;
    }

    public String getUserPw() {
        return userPw;
    }

    public User setUserPw(String userPw) {
        this.userPw = userPw;

        return this;
    }

    public String getUserPinNm() {
        return userPinNm;
    }

    public User setUserPinNm(String userPinNm) {
        this.userPinNm = userPinNm;

        return this;
    }

    public String getUserPattern() {
        return userPattern;
    }

    public User setUserPattern(String userPattern) {
        this.userPattern = userPattern;

        return this;
    }

    public Date getCreateDT() {
        return createDT;
    }

    public void setCreateDT(Date createDT) {
        this.createDT = createDT;
    }

    public HashMap<Integer, UserPastLog> getUserPastPwLogs() {
        return userPastPwLogs;
    }

    public User setUserPastPwLogs(HashMap<Integer, UserPastLog> userPastPwLogs) {
        this.userPastPwLogs = userPastPwLogs;

        return this;
    }

    public HashMap<Integer, UserPastLog> getUserPastPinLogs() {
        return userPastPinLogs;
    }

    public User setUserPastPinLogs(HashMap<Integer, UserPastLog> userPastPinLogs) {
        this.userPastPinLogs = userPastPinLogs;

        return this;
    }

    public HashMap<Integer, UserPastLog> getUserPastPatternLogs() {
        return userPastPatternLogs;
    }

    public User setUserPastPatternLogs(HashMap<Integer, UserPastLog> userPastPatternLogs) {
        this.userPastPatternLogs = userPastPatternLogs;

        return this;
    }
}
