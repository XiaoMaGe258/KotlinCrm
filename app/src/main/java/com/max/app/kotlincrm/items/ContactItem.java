package com.max.app.kotlincrm.items;

import java.io.Serializable;

/**
 * Created by Max on 2018-5-4.
 */

public class ContactItem implements Serializable {
    /*
    {
    "contactId": 1072,
    "tel": "010-56781234,13322554466",
    "telephone": "010-56781234",
    "qq": "440077551",
    "email": "dhj@123.com",
    "name": "后悔过",
    "title": "你的职位",
    "phone": "13322554466",
    "kp": true,
    "contactDept": null,
    "address": "北京市"
    }
     */
    public String contactId;
    public String name;
    public String phone;
    public String telephone;
    public String email;
    public String qq;
    public String title;
    public String address;
    public boolean isKP;
}
