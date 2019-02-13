package com.tinymonster.strangerdiary.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by TinyMonster on 31/12/2018.
 */
@Entity
public class DiaryBean implements Serializable {
    private static final long serialVersionUID = 8290859014505222407L;
    @Id(autoincrement = true)
    private Long Disk_id;
    
    private Long id;

    private Long userId;

    private Long date;

    private String weather;

    private String location;

    private String label;

    private String isSyn;

    private String sprit;

    private Integer picNum;

    private String addition1;

    private String addition2;

    private String content;
    @Transient
    private List<String> imagePaths;
    @Generated(hash = 1996476186)
    public DiaryBean(Long Disk_id, Long id, Long userId, Long date, String weather,
            String location, String label, String isSyn, String sprit,
            Integer picNum, String addition1, String addition2, String content) {
        this.Disk_id = Disk_id;
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.weather = weather;
        this.location = location;
        this.label = label;
        this.isSyn = isSyn;
        this.sprit = sprit;
        this.picNum = picNum;
        this.addition1 = addition1;
        this.addition2 = addition2;
        this.content = content;
    }

    @Generated(hash = 1749744078)
    public DiaryBean() {
    }

    public Long getDisk_id() {
        return this.Disk_id;
    }

    public void setDisk_id(Long Disk_id) {
        this.Disk_id = Disk_id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDate() {
        return this.date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getWeather() {
        return this.weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIsSyn() {
        return this.isSyn;
    }

    public void setIsSyn(String isSyn) {
        this.isSyn = isSyn;
    }

    public String getSprit() {
        return this.sprit;
    }

    public void setSprit(String sprit) {
        this.sprit = sprit;
    }

    public Integer getPicNum() {
        return this.picNum;
    }

    public void setPicNum(Integer picNum) {
        this.picNum = picNum;
    }

    public String getAddition1() {
        return this.addition1;
    }

    public void setAddition1(String addition1) {
        this.addition1 = addition1;
    }

    public String getAddition2() {
        return this.addition2;
    }

    public void setAddition2(String addition2) {
        this.addition2 = addition2;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getimagePaths() {
        return imagePaths;
    }

    public void setimagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}
