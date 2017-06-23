package com.easemob.server.example.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easemob.server.example.api.impl.EasemobIMUsers;
import com.easemob.server.example.exception.HuanXinUserException;
import com.easemob.server.example.exception.HuanxinResponseException;
import com.geetion.puputuan.common.utils.MD5Utils;
import io.swagger.client.model.Nickname;
import io.swagger.client.model.RegisterUsers;
import io.swagger.client.model.User;


/**
 * 环信用户接口
 * Created by jian on 2016/3/30.
 */
public class HuanXinUserService {

    private static EasemobIMUsers easemobIMUsers = new EasemobIMUsers();

    /**
     * 返回 IMUser 的api对象
     *
     * @return
     */
//    public static IMUserAPI getUserAPI() {
//        EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();
//        //获得 IMUserAPI 实例
//        return easemobIMUsers;
//    }

    /**
     * 注册环信用户
     *
     * @param userName
     * @param nickName
     * @return
     */
    public static boolean createNewIMUser(String userName, String nickName) throws HuanXinUserException {

        Object result = null;

        //将账号MD5加密后作为密码
        String password = MD5Utils.getMd5(userName);

        RegisterUsers users = new RegisterUsers();
        User user = new User().username(userName).password(password);
        users.add(user);
        try {
            result = easemobIMUsers.createNewIMUserSingle(users);
        } catch (HuanxinResponseException e) {
            throw new HuanXinUserException("注册环信用户失败");
        }

        if (result == null) {
            throw new HuanXinUserException("注册环信用户失败");
        }

        JSONObject jsonObject = JSON.parseObject((String) result);
        JSONArray entities = (JSONArray) jsonObject.get("entities");

        if (entities == null || entities.size() != 1) {
            throw new HuanXinUserException("注册环信用户失败");
        }

        return modifyIMUserNickName(userName, nickName);
    }

    /**
     * 查询是否有这个环信用户
     *
     * @param userName
     * @return
     */
    public static boolean userExistOrNot(String userName) {

        //查询是否有 SysUser 这个用户
        Object responseWrapper = HuanXinUserService.getIMUsersFromHuanXin(userName);

        return responseWrapper != null;
    }


    /**
     * 根据userName获得用户，由于密码是固定的，手动设置即可
     *
     * @param userName
     * @return
     */
//    public static IMUserBody getIMUsersByUserName(String userName, String nickName) throws HuanXinUserException {
//        EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();
//        //获得IMUser实例
//        IMUserAPI imUserAPI = (IMUserAPI) factory.newInstance(EasemobRestAPIFactory.USER_CLASS);
//        //获得user对象
//        return imUserAPI.getIMUsersByUserName(userName);

//        return new IMUserBody(userName, MD5Utils.getMd5(userName), nickName);
//    }

    /**
     * 根据userName获得用户，由于密码是固定的，手动设置即可
     *
     * @param userName
     * @return
     */
    public static Object getIMUsersFromHuanXin(String userName) {
        try {
            return  easemobIMUsers.getIMUserByUserName(userName);
        } catch (HuanxinResponseException e) {
            return null;
        }
    }


    /**
     * 根据userName修改密码
     *
     * @param userName
     * @param password
     * @return
     */
//    public static Object modifyIMUserPassword(String userName, String password) throws Exception {
//
//        IMUserBody imUserBody = new IMUserBody(userName, password, null);
//
//        EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();
//        //获得IMUser实例
//        IMUserAPI imUserAPI = (IMUserAPI) factory.newInstance(EasemobRestAPIFactory.USER_CLASS);
//        //获得user对象
//        return imUserAPI.modifyIMUserPasswordWithAdminToken(imUserBody.getUserName(), imUserBody);
//    }

    /**
     * 根据userName修改密码
     *
     * @param userName
     * @param nickNameStr
     * @return
     */
    public static boolean modifyIMUserNickName(String userName, String nickNameStr) throws HuanXinUserException {

        Nickname nickname = new Nickname();
        nickname.setNickname(nickNameStr);
        Object result = null;

        try {
            result = easemobIMUsers.modifyIMUserNickNameWithAdminToken(userName, nickname);
        } catch (HuanxinResponseException e) {
            throw new HuanXinUserException("修改环信用户失败");
        }

        if (result == null) {
            throw new HuanXinUserException("修改环信用户失败");
        }

        JSONObject jsonObject = JSON.parseObject((String) result);
        JSONArray entities = (JSONArray)jsonObject.get("entities");

        if (entities == null || entities.size() != 1) {
            throw new HuanXinUserException("修改环信用户失败");
        }

        JSONObject entity = entities.getJSONObject(0);
        String rspNickName = (String) entity.get("nickname");

        if (rspNickName == null || !rspNickName.equals(nickNameStr)) {
            throw new HuanXinUserException("修改环信用户失败");
        }

        return true;
    }


    /**
     * 根据userName删除用户
     *
     * @param userName
     * @return
     */
    public static boolean deleteIMUserByUserName(String userName) throws Exception {
        Object response = null;
        try {
            response = easemobIMUsers.deleteIMUserByUserName(userName);

            if (response != null) {
                return true;
            }
        } catch (HuanxinResponseException e) {
            if (e.getCode() == 404) {
                return true;
            }
        }

        return false;
    }

    /**
     * 根据userName查看用户在线状态
     *
     * @param userName
     * @return
     */
    public static Object getIMUserStatus(String userName) throws Exception {
        try {
            //获得user对象
            return easemobIMUsers.getIMUserStatus(userName);
        } catch (HuanxinResponseException e) {
            return null;
        }
    }

    /**
     * 获取群组，参数为空时获取所有群组
     *
     * @param limit  单页数量
     * @param cursor 游标，存在更多记录时产生
     * @return
     */
    public static Object getIMUserBatch(Long limit, String cursor) {
        try {
            return easemobIMUsers.getIMUsersBatch(limit, cursor);
        } catch (HuanxinResponseException e) {
            return null;
        }
    }
}
