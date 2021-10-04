package com.quest.fragment;

/**
 * Created by NIKUNJ on 23-02-2018.
 */

public class modelDetail {

    String Title;
    String Id;
    String website;
    String image;
    public boolean cbChecked;
    String Data;

    public modelDetail() {
    }

    public modelDetail(String title, String id) {
        Title = title;
        Id = id;
    }



    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public void setCbChecked(boolean cbChecked) {
        this.cbChecked = cbChecked;
    }

    public boolean isCbChecked() {
        return cbChecked;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
