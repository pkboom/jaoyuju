package com.sujichim.jasanjao2.card;

import java.io.Serializable;

/**
 * Created by yedam on 15. 10. 16.
 * when adding a column, add
 * 1. String newColumn
 * 2. getNewColumn
 * 3. setNewColumn
 */
public class Record implements Serializable {
    int id;
    String date;
    String name;
    String memo;

    // Empty constructor
    public Record() {
    }

    // constructor
    public Record(String name) {
        this.name = name;
    }

    // constructor
    public Record(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getMemo() {
        return memo;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getShortInfo() {
        return getDate() + " " + getName();
    }



}
