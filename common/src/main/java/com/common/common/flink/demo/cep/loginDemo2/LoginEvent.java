package com.common.common.flink.demo.cep.loginDemo2;

public class LoginEvent {

    private String userId;//用户ID
    private String ip;//登录IP
    private String type;//登录类型
    private String timestap;//登录时间

    public LoginEvent() {
    }

    public LoginEvent(String userId, String ip, String type,String timestap) {
        this.userId = userId;
        this.ip = ip;
        this.type = type;
        this.timestap = timestap;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestap() {
        return timestap;
    }

    public void setTimestap(String timestap) {
        this.timestap = timestap;
    }

    @Override
    public String toString() {
        return "LoginEvent{" +
                "userId='" + userId + '\'' +
                ", ip='" + ip + '\'' +
                ", type='" + type + '\'' +
                ", timestap='" + timestap + '\'' +
                '}';
    }
}
