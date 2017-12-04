package com.zspirytus.dmdemo.JavaSource.ListViewModule;

/**
 * Created by ZSpirytus on 2017/10/15.
 */

public class InfoItem {

    public String rowName;
    public String rowElement;

    public InfoItem(){}

    public void setRowName(String str){
        rowName = str;
    }

    public void setRowElement(String str){
        rowElement = str;
    }

    public String getRowName(){
        return rowName;
    }

    public String getRowElement(){
        return rowElement;
    }
}
