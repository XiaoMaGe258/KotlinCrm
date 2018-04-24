package com.max.app.kotlincrm.items;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Max on 2017-1-9.
 */

public class FollowItem implements Serializable {

    /**
     * followupDate : 2018-04-13 14:44:46
     * endDate : null
     * peifang :
     * followupType : 拜访
     * followupObject : 汪先生
     * contactInformation : 18780251376
     * location : 中国北京市东城区和平里北街七区26号楼
     * content : 滚滚滚
     * label : 主动慰问
     * category : 1
     * staffResource : 0
     * name : xszg3
     * followuper : 销售-xszg3
     * contactId : 1357
     * kp : false
     * id : 261
     * status : -1
     * originBdId : 0
     * roleId : 0
     * comments : []
     * locationPicture : null
     */

    private String followupDate;
    private String endDate;
    private String peifang;
    private String followupType;
    private String followupObject;
    private String contactInformation;
    private String location;
    private String content;
    private String label;
    private int category;
    private int staffResource;
    private String name;
    private String followuper;
    private int contactId;
    private boolean kp;
    private int id;
    private int status;
    private int originBdId;
    private int roleId;
    private Object locationPicture;
    private List<FollowItemsItem> comments;

    public String getFollowupDate() {
        return followupDate;
    }

    public void setFollowupDate(String followupDate) {
        this.followupDate = followupDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPeifang() {
        return peifang;
    }

    public void setPeifang(String peifang) {
        this.peifang = peifang;
    }

    public String getFollowupType() {
        return followupType;
    }

    public void setFollowupType(String followupType) {
        this.followupType = followupType;
    }

    public String getFollowupObject() {
        return followupObject;
    }

    public void setFollowupObject(String followupObject) {
        this.followupObject = followupObject;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getStaffResource() {
        return staffResource;
    }

    public void setStaffResource(int staffResource) {
        this.staffResource = staffResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFollowuper() {
        return followuper;
    }

    public void setFollowuper(String followuper) {
        this.followuper = followuper;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public boolean isKp() {
        return kp;
    }

    public void setKp(boolean kp) {
        this.kp = kp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOriginBdId() {
        return originBdId;
    }

    public void setOriginBdId(int originBdId) {
        this.originBdId = originBdId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public Object getLocationPicture() {
        return locationPicture;
    }

    public void setLocationPicture(Object locationPicture) {
        this.locationPicture = locationPicture;
    }

    public List<?> getComments() {
        return comments;
    }

    public void setComments(List<FollowItemsItem> comments) {
        this.comments = comments;
    }
}
