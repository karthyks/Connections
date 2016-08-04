package com.github.karthyks.connections.model;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;

/**
 * Created by karthy07 on 7/19/2016.
 */

public class UserModel {

    public static final String EXTRAS_USER_ID = "user_id";
    public static final String EXTRAS_USERNAME = "user_name";
    public static final String EXTRAS_PASSWORD = "password";
    public static final String EXTRAS_FULL_NAME = "full_name";
    public static final String EXTRAS_EMAIL = "email";
    public static final String EXTRAS_PHONE_NUMBER = "phone_number";
    public static final String EXTRAS_SESSION_EXPIRED = "user_expired";

    private String userId;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;
    private boolean isExpired;

    public String getAccountName() {
        return username;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS_USER_ID, userId);
        bundle.putString(EXTRAS_USERNAME, username);
        bundle.putString(EXTRAS_PASSWORD, password);
        bundle.putString(EXTRAS_FULL_NAME, fullName);
        bundle.putString(EXTRAS_PHONE_NUMBER, phoneNumber);
        bundle.putString(EXTRAS_EMAIL, email);
        bundle.putBoolean(EXTRAS_SESSION_EXPIRED, isExpired);
        return bundle;
    }

    public static UserModel fromAccount(Account account, AccountManager accountManager) {
        UserModel userModel = new UserModel();
        userModel.setUsername(accountManager.getUserData(account, EXTRAS_USERNAME));
        userModel.setPassword(accountManager.getPassword(account));
        userModel.setEmail(accountManager.getUserData(account, EXTRAS_EMAIL));
        userModel.setUserId(accountManager.getUserData(account, EXTRAS_USER_ID));
        userModel.setFullName(accountManager.getUserData(account, EXTRAS_FULL_NAME));
        userModel.setPhoneNumber(accountManager.getUserData(account, EXTRAS_PHONE_NUMBER));
        userModel.setIsExpired(Boolean.parseBoolean(accountManager.getUserData(account,
                EXTRAS_SESSION_EXPIRED)));
        return userModel;
    }

    public void expireSession() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setIsExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }
}
