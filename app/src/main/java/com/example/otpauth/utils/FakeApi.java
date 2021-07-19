package com.example.otpauth.utils;

import com.example.otpauth.model.Account;

import java.util.ArrayList;
import java.util.List;

public class FakeApi {
    private  static FakeApi instance;

    private List<Account> accounts = new ArrayList<Account>();

    private FakeApi(){}

    public static FakeApi getInstance(){
        if(instance == null) {
            instance = new FakeApi();
            instance.accounts.add(new Account("risto-trajanov", "Microsoft", "TMH2LQFHNC33DKAV6XBCRFWVDQBRJ3MD"));
            instance.accounts.add(new Account("monika-mateska", "Shopify", "ed63dgmjbpepbsob3fxv4hccf3ofy3gh"));
//            instance.accounts.add(new Account("YouTube", "531523541235", "bill-gates"));
//            instance.accounts.add(new Account("CexIO", "5123451234", "elon-mask"));
        }

        return instance;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addNewAccount(Account Movie) {
        this.accounts.add(Movie);
    }

    public void deleteAccount(Account Movie){
        this.accounts.remove(Movie);
    }
}
