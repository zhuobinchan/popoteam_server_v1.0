package com.easemob.server.example.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easemob.server.example.api.ChatGroupAPI;
import com.easemob.server.example.api.impl.EasemobChatGroup;
import com.easemob.server.example.exception.HuanXinChatGroupException;
import com.easemob.server.example.exception.HuanxinResponseException;
import io.swagger.client.model.*;


/**
 * 环信群组接口
 * Created by jian on 2016/3/30.
 */
public class HuanXinChatGroupService {


    private static EasemobChatGroup easemobChatGroup = new EasemobChatGroup();

    /**
     * 返回 ChatGroupAPI 的api对象
     *
     * @return
     */
    public static ChatGroupAPI getChatGroupAPI() {
        return easemobChatGroup;
    }


    /**
     * 获取群组，参数为空时获取所有群组
     *
     * @param limit  单页数量
     * @param cursor 游标，存在更多记录时产生
     * @return
     */
    public static Object getChatGroups(Long limit, String cursor) {
        try {
            return getChatGroupAPI().getChatGroups(limit, cursor);
        } catch (HuanxinResponseException e) {
            return null;
        }
    }

    /**
     * 获取一个或者多个群组的详情
     *
     * @param groupIds 群组ID数组
     * @return
     */
    public static Object getChatGroupDetails(String... groupIds) throws HuanXinChatGroupException {

//        ChatGroupBody chatGroupBody = new ChatGroupBody("group", "desc", false, null, true, "owner", null);

        try {
            return getChatGroupAPI().getChatGroupDetails(groupIds);
        } catch (HuanxinResponseException e) {
            if (e.getCode() == 404) {
                throw new HuanXinChatGroupException(e.getCode(), e.getMessage());
            }

            return null;
        }
    }

    /**
     * 创建一个群组
     *
     * @param groupName 群组名称， 任意字符串
     * @param desc      群组描述， 任意字符串
     * @param isPublic  true 公开群，false 私有群
     * @param maxUsers  群成员上限，创建群组的时候设置，可修改
     * @param approval  true
     * @param owner     群主的username， 例如：{“owner”: “13800138001”}
     * @param members   群成员的username ， 例如： {“member”:”xc6xrnbzci”}
     * @return
     */
    public static String createChatGroup(String groupName, String desc, Boolean isPublic, Long maxUsers,
                                         Boolean approval, String owner, String[] members) throws HuanXinChatGroupException {

//        ChatGroupBody chatGroupBody = new ChatGroupBody(groupName, desc, isPublic, maxUsers, approval, owner, members);
        UserName userName = new UserName();
        for (String member: members ) {
            userName.add(member);
        }

        Group group = new Group();
        group.groupname(groupName).desc(desc)._public(isPublic).maxusers(maxUsers.intValue()).approval(approval).owner(owner).members(userName);
        Object responseWrapper = null;
        try {
            responseWrapper = getChatGroupAPI().createChatGroup(group);
        } catch (HuanxinResponseException e) {
            throw new HuanXinChatGroupException("创建环信群组失败");
        }

        if (responseWrapper != null) {
            JSONObject data = JSON.parseObject((String) responseWrapper).getJSONObject("data");
            if (data != null) {
                return data.getString("groupid");
            }
        }

        throw new HuanXinChatGroupException("创建环信群组失败");
    }

    /**
     * 修改群组信息
     *
     * @param groupId   群组标识
     * @param groupName 群组名称， 任意字符串
     * @param desc      群组描述， 任意字符串
     * @param maxUsers  群成员上限，创建群组的时候设置，可修改
     * @return
     */
    public static boolean modifyChatGroup(String groupId, String groupName, String desc, Long maxUsers)
            throws HuanXinChatGroupException {
//        ChatGroupBody chatGroupBody = new ChatGroupBody(groupName, desc, isPublic, maxUsers, approval, owner, null);

        ModifyGroup group = new ModifyGroup();
        group.description(desc).groupname(groupName).maxusers(maxUsers.intValue());
        Object responseWrapper = null;
        try {
            responseWrapper = getChatGroupAPI().modifyChatGroup(groupId, group);
        } catch (HuanxinResponseException e) {
            throw new HuanXinChatGroupException("修改环信群组失败");
        }

        //有返回JSON body表示处理成功.
        if (responseWrapper != null) {
            return true;
        } else {
            throw new HuanXinChatGroupException("修改环信群组失败");
        }
    }

    /**
     * 删除群组
     *
     * @param groupId 群组标识
     * @return
     */
    public static boolean deleteChatGroup(String groupId) throws HuanXinChatGroupException {

        //环信所有的接口都是调用同一个sendRequest方法的，有些没有body参数还是上传了，这里补一个不需要的body
//        ChatGroupBody chatGroupBody = new ChatGroupBody("group", "desc", false, null, true, "owner", null);

        Object responseWrapper = null;
        try {
            responseWrapper = getChatGroupAPI().deleteChatGroup(groupId);
        } catch (HuanxinResponseException e) {
            //如果返回404错误,则群组不存在,认为删除成功.
            if (e.getCode() == 404) {
                return true;
            }

            throw new HuanXinChatGroupException("删除环信群组失败");
        }

        if (responseWrapper == null) {
            throw new HuanXinChatGroupException("删除环信群组失败");
        }

        JSONObject data = JSON.parseObject((String) responseWrapper).getJSONObject("data");
        if (data == null) {
            throw new HuanXinChatGroupException("删除环信群组失败");
        }

        boolean success = data.getBooleanValue("success");
        String rspGroupId = data.getString("groupid");

        if (success && groupId.equals(rspGroupId)) {
            return true;
        } else {
            throw new HuanXinChatGroupException("删除环信群组失败");
        }
    }

    /**
     * 获取群组所有用户
     *
     * @param groupId 群组标识
     * @return
     */
    public static boolean getChatGroupUsers(String groupId) throws HuanXinChatGroupException {

        Object responseWrapper = null;
        try {
            responseWrapper = getChatGroupAPI().getChatGroupUsers(groupId);
        } catch (HuanxinResponseException e) {
            throw new HuanXinChatGroupException("获取环信群组所有用户失败");
        }

        if (responseWrapper != null) {
            return true;
        } else {
            throw new HuanXinChatGroupException("获取环信群组所有用户失败");
        }
    }

    /**
     * 群组加人[单个]
     *
     * @param groupId 群组标识
     * @param userId  用户ID或用户名
     * @return
     */
    public static boolean addSingleUserToChatGroup(String groupId, String userId) throws HuanXinChatGroupException {

        //环信所有的接口都是调用同一个sendRequest方法的，有些没有body参数还是上传了，这里补一个不需要的body
//        ChatGroupBody chatGroupBody = new ChatGroupBody("group", "desc", false, null, true, "owner", null);

        Object responseWrapper = null;
        try {
            responseWrapper = getChatGroupAPI().addSingleUserToChatGroup(groupId, userId);
        } catch (HuanxinResponseException e) {
            //403（IM用户已经是群成员）,则认为添加成功返回true.
            if (e.getCode() == 403) {
                return true;
            }
            throw new HuanXinChatGroupException("加入环信群组失败");
        }

        if (responseWrapper == null) {
            throw new HuanXinChatGroupException("加入环信群组失败");
        }

        JSONObject data = JSON.parseObject((String) responseWrapper).getJSONObject("data");
        if (data == null) {
            throw new HuanXinChatGroupException("加入环信群组失败");
        }

        boolean result = data.getBooleanValue("result");
        String rspGroupId = data.getString("groupid");
        String rspUserId = data.getString("user");

        if (result && groupId.equals(rspGroupId) && userId.equals(rspUserId)) {
            return true;
        } else {
            throw new HuanXinChatGroupException("加入环信群组失败");
        }
    }

    /**
     * 群组加人[批量]
     *
     * @param groupId 群组标识
     * @param userIds 用户ID或用户名，数组形式
     * @return
     */
    public static boolean addBatchUsersToChatGroup(String groupId, String[] userIds) throws HuanXinChatGroupException {

//        UserNamesBody userNamesBody = new UserNamesBody(userIds);

        UserNames userNames = new UserNames();
        UserName userList = new UserName();
        for (String userId: userIds) {
            userList.add(userId);
        }

        if (userList.size() == 0) {
            return true;
        }

        userNames.usernames(userList);
        Object responseWrapper = null;
        try {
            responseWrapper = getChatGroupAPI().addBatchUsersToChatGroup(groupId, userNames);
        } catch (HuanxinResponseException e) {
            //403（所有用户均已是群成员）,则认为添加成功返回.
            if (e.getCode() == 403) {
                return true;
            }
            throw new HuanXinChatGroupException("批量加入环信群组失败");
        }

        if (responseWrapper == null) {
            throw new HuanXinChatGroupException("批量加入环信群组失败");
        }

        JSONObject data = JSON.parseObject((String) responseWrapper).getJSONObject("data");
        if (data == null) {
            throw new HuanXinChatGroupException("批量加入环信群组失败");
        }

        String rspGroupId = data.getString("groupid");

        if (groupId.equals(rspGroupId)) {
            return true;
        } else {
            throw new HuanXinChatGroupException("批量加入环信群组失败");
        }
    }

    /**
     * 群组减人[单个]
     *
     * @param groupId 群组标识
     * @param userId  用户ID或用户名
     * @return
     */
    public static boolean removeSingleUserFromChatGroup(String groupId, String userId) throws HuanXinChatGroupException {
        //环信所有的接口都是调用同一个sendRequest方法的，有些没有body参数还是上传了，这里补一个不需要的body
//        ChatGroupBody chatGroupBody = new ChatGroupBody("group", "desc", false, null, true, "owner", null);

        Object responseWrapper = null;
        try {
            responseWrapper = getChatGroupAPI().removeSingleUserFromChatGroup(groupId, userId);
        } catch (HuanxinResponseException e) {
            //403（被移除用户不在群组里等）、404（被移除的IM用户不存在，此群组id不存在）,则认为移除成功返回.
            if (e.getCode() == 403 || e.getCode() == 404) {
                return true;
            }
            throw new HuanXinChatGroupException("删除环信群组成员失败");
        }

        //等于200代表删除成功
        if (responseWrapper == null) {
            throw new HuanXinChatGroupException("删除环信群组成员失败");
        }

        JSONObject data = JSON.parseObject((String) responseWrapper).getJSONObject("data");
        if (data == null) {
            throw new HuanXinChatGroupException("删除环信群组成员失败");
        }

        boolean result = data.getBooleanValue("result");
        String rspGroupId = data.getString("groupid");
        String rspUserId = data.getString("user");

        if (result && groupId.equals(rspGroupId) && userId.equals(rspUserId)) {
            return true;
        } else {
            throw new HuanXinChatGroupException("删除环信群组成员失败");
        }
    }

    /**
     * 群组减人[批量]
     *
     * @param groupId 群组标识
     * @param userIds 用户ID或用户名，数组形式
     * @return
     */
    public static boolean removeBatchUsersFromChatGroup(String groupId, String[] userIds) throws HuanXinChatGroupException {

        Object responseWrapper = null;
        try {
            responseWrapper = getChatGroupAPI().removeBatchUsersFromChatGroup(groupId, userIds);
        } catch (HuanxinResponseException e) {
            throw new HuanXinChatGroupException("批量删除环信群组成员失败");
        }

        if (responseWrapper != null) {
            return true;
        } else {
            throw new HuanXinChatGroupException("批量删除环信群组成员失败");
        }

    }

    /**
     * 群组转让 <br>
     * PUT
     *
     * @param groupId  群组标识
     * @param newOwnerStr 新群主ID或用户名
     * @return
//     * @see com.easemob.server.example.comm.body.GroupOwnerTransferBody
     */
    public static boolean transferChatGroupOwner(String groupId, String newOwnerStr) throws HuanXinChatGroupException {

//        GroupOwnerTransferBody groupOwnerTransferBody = new GroupOwnerTransferBody(newOwner);

        NewOwner newOwner = new NewOwner();
        newOwner.newowner(newOwnerStr);
        Object responseWrapper = null;
        try {
            responseWrapper = getChatGroupAPI().transferChatGroupOwner(groupId, newOwner);
        } catch (HuanxinResponseException e) {
            throw new HuanXinChatGroupException("转让环信群组失败");
        }

        if (responseWrapper != null) {
            return true;
        } else {
            throw new HuanXinChatGroupException("转让环信群组失败");
        }
    }

    /**
     * 查询群组黑名单
     *
     * @param groupId 群组标识
     * @return
     */
    public static Object getChatGroupBlockUsers(String groupId) {
        try {
            return getChatGroupAPI().getChatGroupBlockUsers(groupId);
        } catch (HuanxinResponseException e) {
            return null;
        }
    }

    /**
     * 群组黑名单个添加
     *
     * @param groupId 群组标识
     * @param userId  用户ID或用户名
     * @return
     */
    public static Object addSingleBlockUserToChatGroup(String groupId, String userId) {
        try {
            return getChatGroupAPI().addSingleBlockUserToChatGroup(groupId, userId);
        } catch (HuanxinResponseException e) {
            return null;
        }
    }

    /**
     * 群组黑名单批量添加
     *
     * @param groupId 群组标识
     * @param userIds 用户ID或用户名，数组形式
     * @return
     */
    public static Object addBatchBlockUsersToChatGroup(String groupId, String[] userIds) {
//        UserNamesBody userNamesBody = new UserNamesBody(userIds);


        UserNames userNames = new UserNames();
        UserName userList = new UserName();
        for (String userId: userIds) {
            userList.add(userId);
        }
        userNames.usernames(userList);

        try {
            return getChatGroupAPI().addBatchBlockUsersToChatGroup(groupId, userNames);
        } catch (HuanxinResponseException e) {
            return null;
        }
    }

    /**
     * 群组黑名单单个删除
     *
     * @param groupId 群组标识
     * @param userId  用户ID或用户名
     * @return
     */
    public static Object removeSingleBlockUserFromChatGroup(String groupId, String userId) {

        try {
            return getChatGroupAPI().removeSingleBlockUserFromChatGroup(groupId, userId);
        } catch (HuanxinResponseException e) {
            return null;
        }

    }

    /**
     * 群组黑名单批量删除
     *
     * @param groupId 群组标识
     * @param userIds 用户ID或用户名，数组形式
     * @return
     */
    public static Object removeBatchBlockUsersFromChatGroup(String groupId, String[] userIds) {

        try {
            return getChatGroupAPI().removeBatchBlockUsersFromChatGroup(groupId, userIds);
        } catch (HuanxinResponseException e) {
            return null;
        }
    }


}
