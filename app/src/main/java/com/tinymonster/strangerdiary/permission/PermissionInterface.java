package com.tinymonster.strangerdiary.permission;

/**
 * Created by TinyMonster on 16/01/2019.
 */


/**

 * 权限请求接口


 */

public interface PermissionInterface {



    /**

     * 可设置请求权限请求码

     */

    int getPermissionsRequestCode();



    /**

     * 设置需要请求的权限

     */

    String[] getPermissions();



    /**

     * 请求权限成功回调

     */

    void requestPermissionsSuccess();



    /**

     * 请求权限失败回调

     */

    void requestPermissionsFail();

}

