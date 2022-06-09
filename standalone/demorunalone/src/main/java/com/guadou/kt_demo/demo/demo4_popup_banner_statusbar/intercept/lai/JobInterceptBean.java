package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept.lai;

/**
 * 后台返回的工作校验对象
 */
public class JobInterceptBean {

    public boolean isNewMember;
    public boolean isFillInfo;
    public boolean isMemberApprove;
    public boolean isNOCVUpload;
    public boolean isNeedDepost;
    public boolean isNeedFace;
    public boolean isUnderCompany;
    public boolean isNeedWhatApp;
    public boolean isNeedBankInfo;
    public boolean isNeedSkill;

    public JobInterceptBean() {
    }

    public JobInterceptBean(boolean isNewMember, boolean isFillInfo, boolean isMemberApprove, boolean isNOCVUpload, boolean isNeedDepost, boolean isNeedFace, boolean isUnderCompany, boolean isNeedWhatApp, boolean isNeedBankInfo, boolean isNeedSkill) {
        this.isNewMember = isNewMember;
        this.isFillInfo = isFillInfo;
        this.isMemberApprove = isMemberApprove;
        this.isNOCVUpload = isNOCVUpload;
        this.isNeedDepost = isNeedDepost;
        this.isNeedFace = isNeedFace;
        this.isUnderCompany = isUnderCompany;
        this.isNeedWhatApp = isNeedWhatApp;
        this.isNeedBankInfo = isNeedBankInfo;
        this.isNeedSkill = isNeedSkill;
    }
}
