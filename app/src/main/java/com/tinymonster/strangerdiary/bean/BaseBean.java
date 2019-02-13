package com.tinymonster.strangerdiary.bean;

/**
 * Created by TinyMonster on 10/01/2019.
 */

public class BaseBean<T> {

    /**
     * 服务器返回的状态吗
     */
    public boolean success;

    /**
     * 服务器返回的提示信息
     */
    public String error;

    /**
     * 服务器返回的数据
     */
    public T data;

    public BaseBean(boolean success, String error, T data) {
        this.success = success;
        this.error = error;
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "success=" + success +
                ", error='" + error + '\'' +
                ", data=" + data +
                '}';
    }
}
