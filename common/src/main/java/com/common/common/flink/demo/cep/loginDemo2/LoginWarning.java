package com.common.common.flink.demo.cep.loginDemo2;

public class LoginWarning {

    private String userId;
    private String type;
    private String ip;
    private String timestap;

    public LoginWarning() {
    }

    public LoginWarning(String userId, String type, String ip,String timestap) {
        this.userId = userId;
        this.type = type;
        this.ip = ip;
        this.timestap = timestap;
    }

    @Override
    public String toString() {
        return "LoginWarning{" +
                "userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", ip='" + ip + '\'' +
                ", timestap='" + timestap + '\'' +
                '}';
    }
}



