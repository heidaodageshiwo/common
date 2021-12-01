package com.common.common.flink.demo.cep.loginDemo1;

public class LoginWarning {

    private String userId;
    private String type;
    private String ip;

    public LoginWarning() {
    }

    public LoginWarning(String userId, String type, String ip) {
        this.userId = userId;
        this.type = type;
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "LoginWarning{" +
                "userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}



