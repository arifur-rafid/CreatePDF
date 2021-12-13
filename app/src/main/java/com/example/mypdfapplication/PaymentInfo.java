package com.example.mypdfapplication;

import java.util.regex.Pattern;

public class PaymentInfo {
    private String phoneNumber = "";
    private String userName = "";
    private String amount = "";
    private String narration = "";

    public PaymentInfo(String phoneNumber, String userName, String amount, String narration) {
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.amount = amount;
        this.narration = narration;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public boolean phoneNumberValidate() {
        if (isValidMobile()) {
            return true;
        } else return false;
    }

    public boolean nameValidate() {
        if (!phoneNumber.equals("")) {
            return true;
        } else return false;
    }

    public boolean amountValidate() {
        if (!amount.equals("") && Integer.parseInt(amount) > 0) {
            return true;
        } else return false;
    }

    public boolean narrationValidate() {
        if (!narration.equals("")) {
            return true;
        } else return false;
    }

    public boolean isValidMobile() {
        boolean check = false;
        String regex = "^(?:\\+?88)?01[13-9]\\d{8}$";
        if (Pattern.matches(regex, phoneNumber)) {
            check = true;
        } else {
            check = false;
        }
        return check;
    }
}
