package com.guadou.kt_demo.demo.demo16_record.prototype;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class UserProfile implements Cloneable {

    public String userId;
    public String name;
    public String age;
    public UserAddress address;
    public ArrayList<String> skills = new ArrayList<>();

    public UserProfile() {
    }

    public UserProfile(String userId, String name, String age) {
        this.userId = userId;
        this.name = name;
        this.age = age;
    }

    @NonNull
    @Override
    public UserProfile clone() throws CloneNotSupportedException {

        UserProfile profile = (UserProfile) super.clone();

        // 把地址也做一次克隆，达到深拷贝
//        profile.address = address.clone();

        return profile;
    }
}
