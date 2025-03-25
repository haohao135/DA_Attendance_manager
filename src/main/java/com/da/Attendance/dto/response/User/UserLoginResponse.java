package com.da.Attendance.dto.response.User;

public class UserLoginResponse {
    private String name;
    private String jwt;

    public UserLoginResponse() {
    }

    public UserLoginResponse(String name, String jwt) {
        this.name = name;
        this.jwt = jwt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
