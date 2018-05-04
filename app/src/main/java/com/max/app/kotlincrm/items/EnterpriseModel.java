package com.max.app.kotlincrm.items;

import java.io.Serializable;

/**
 * Created by Max on 2018-5-4.
 */

public class EnterpriseModel implements Serializable {

    /**
     * name : 福建南平南孚电池北京办事处
     * district :
     * location :
     * contact : 汪先生
     * title : 公关
     * kp : 否
     * tel : 13838383388,84848844
     * qq : 9527
     * email : 9527@qq.com
     * scale : 15人以下
     * property : 私企
     * about : 一次兼职，一次成长。欢迎您加入本公司，一起奋斗，一起收获。
     * website :
     * ct : 2017-08-02 16:39:41
     * note :
     * hideContactInfo : false
     * photos :
     * license :
     * logo :
     */

    private String name;
    private String district;
    private String location;
    private String contact;
    private String title;
    private String kp;
    private String tel;
    private String qq;
    private String email;
    private String scale;
    private String property;
    private String about;
    private String website;
    private String ct;
    private String note;
    private boolean hideContactInfo;
    private String photos;
    private String license;
    private String logo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKp() {
        return kp;
    }

    public void setKp(String kp) {
        this.kp = kp;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isHideContactInfo() {
        return hideContactInfo;
    }

    public void setHideContactInfo(boolean hideContactInfo) {
        this.hideContactInfo = hideContactInfo;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
