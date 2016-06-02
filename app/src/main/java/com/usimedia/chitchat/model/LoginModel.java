package com.usimedia.chitchat.model;

/**
 * Created by Cheick on 31/05/16.
 */
public class LoginModel {

    private String username;
    private String password;

    public LoginModel() {};

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class Result {
        String name;
        boolean success;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
