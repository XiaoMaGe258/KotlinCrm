package com.max.app.kotlincrm.items;

import java.io.Serializable;

/**
 * 普通Key, Value 模式Item
 * Created by Max on 2017-3-3.
 */

public class NormalKVItem implements Serializable {
    public boolean isSelect;
    public String id;
    public String name;
    public NormalKVItem(String id, String name){
        this.id = id;
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
