package com.example.administrator.txt;

/**
 * Created by Administrator on 2018\11\2 0002.
 */

public class TxTBean {
    String number;
    String name;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TxTBean{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
