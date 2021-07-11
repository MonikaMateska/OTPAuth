package com.example.otpauth.model;

import java.io.Serializable;
import java.util.Objects;

public class Account implements Serializable {
    final String secretKey;
    final String issuer;
    final String username;

    public Account(String issuer, String secretKey, String username) {
        this.secretKey = secretKey;
        this.issuer = issuer;
        this.username = username;
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
