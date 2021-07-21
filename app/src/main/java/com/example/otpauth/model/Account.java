package com.example.otpauth.model;

import java.io.Serializable;
import java.util.Objects;

import kotlin.jvm.JvmField;

public class Account implements Serializable {
    final String secretKey;
    final String issuer;
    final String username;
    int timer;
    String otp;

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getUsername() {
        return username;
    }

    public void setOTP(String otp) { this.otp = otp; }

    public String getOtp() { return otp; }

    public Account(String username, String issuer, String secretKey) {
        this.secretKey = secretKey;
        this.issuer = issuer;
        this.username = username;
        this.timer = 15;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return secretKey.equals(account.secretKey) &&
                issuer.equals(account.issuer) &&
                username.equals(account.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(secretKey, issuer, username);
    }


}
