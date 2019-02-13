package com.tinymonster.strangerdiary.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TinyMonster on 17/01/2019.
 */

public class ImagesUrlBean {
    Map<String,String> hashMap=new HashMap<>();

    public Map<String, String> getImages() {
        return hashMap;
    }

    public void setImages(Map<String, String> images) {
        this.hashMap = images;
    }

    @Override
    public String toString() {
        return "ImagesUrlBean{" +
                "images=" + hashMap.toString() +
                '}';
    }
}
