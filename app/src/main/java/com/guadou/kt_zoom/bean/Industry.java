package com.guadou.kt_zoom.bean;

public class Industry {

    /**
     * industry_id : 1
     * industry_name : Cleaning Service
     * industry_image : http://yyjobs-admin-dev.guabean.com/storage/201807/09/kRqiB08Lnr41tPUVaOsUgzfFOtYIhLB3mkWBjqsg.png
     * industry_image_new : http://yyjobs-admin-dev.guabean.com/storage/201808/07/qGtQRxzLv52DmmBU5ocFf29o6E2dfxMBklk8S3TX.png
     */

    public int industry_id;
    public String industry_name;
    public String industry_image;
    public String industry_image_new;

    @Override
    public String toString() {
        return "Industry{" +
                "industry_id=" + industry_id +
                ", industry_name='" + industry_name + '\'' +
                ", industry_image='" + industry_image + '\'' +
                ", industry_image_new='" + industry_image_new + '\'' +
                '}';
    }
}
