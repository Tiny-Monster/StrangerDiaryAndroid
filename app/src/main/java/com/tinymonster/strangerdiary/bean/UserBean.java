package com.tinymonster.strangerdiary.bean;

import android.support.v7.app.AppCompatActivity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by TinyMonster on 03/01/2019.
 */
@Entity
public class UserBean implements Serializable{
    private static final long serialVersionUID = -8323901614699767502L;
    @Id(autoincrement = false)
    private Long id;

    private String account;

    private String nickname;

    private String password;

    private String gesture;

    private Integer diaryNum;

    private Integer diarySpace;

    private Long createtime;

    private String icon;

    private String tag;

    private Integer level;

    private Integer version;

    private String addition1;

    private String addition2;

    private String addition3;

    @Generated(hash = 840224418)
    public UserBean(Long id, String account, String nickname, String password,
            String gesture, Integer diaryNum, Integer diarySpace, Long createtime,
            String icon, String tag, Integer level, Integer version,
            String addition1, String addition2, String addition3) {
        this.id = id;
        this.account = account;
        this.nickname = nickname;
        this.password = password;
        this.gesture = gesture;
        this.diaryNum = diaryNum;
        this.diarySpace = diarySpace;
        this.createtime = createtime;
        this.icon = icon;
        this.tag = tag;
        this.level = level;
        this.version = version;
        this.addition1 = addition1;
        this.addition2 = addition2;
        this.addition3 = addition3;
    }

    @Generated(hash = 1203313951)
    public UserBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGesture() {
        return this.gesture;
    }

    public void setGesture(String gesture) {
        this.gesture = gesture;
    }

    public Integer getDiaryNum() {
        return this.diaryNum;
    }

    public void setDiaryNum(Integer diaryNum) {
        this.diaryNum = diaryNum;
    }

    public Integer getDiarySpace() {
        return this.diarySpace;
    }

    public void setDiarySpace(Integer diarySpace) {
        this.diarySpace = diarySpace;
    }

    public Long getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(Long createtime) {
        this.createtime = createtime;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getLevel() {
        return this.level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public String getAddition3() {
        return this.addition3;
    }

    public void setAddition3(String addition3) {
        this.addition3 = addition3;
    }
    
}
