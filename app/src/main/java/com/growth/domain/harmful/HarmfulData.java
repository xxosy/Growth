package com.growth.domain.harmful;

/**
 * Created by SSL-D on 2016-09-28.
 */

public class HarmfulData {
    int id;
    String imgurl;
    String title;
    String description;

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getTitle() {
        return title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImgurl(String imgUrl) {
        this.imgurl = imgUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
