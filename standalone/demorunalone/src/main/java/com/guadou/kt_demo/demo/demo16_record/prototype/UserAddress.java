package com.guadou.kt_demo.demo.demo16_record.prototype;


import androidx.annotation.NonNull;

public class UserAddress implements Cloneable {
    public String city;
    public String address;

    public UserAddress() {
    }

    public UserAddress(String city, String address) {
        this.city = city;
        this.address = address;
    }

    @NonNull
    @Override
    public UserAddress clone() throws CloneNotSupportedException {
        return (UserAddress) super.clone();
    }
}
