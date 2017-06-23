package com.geetion.puputuan.utils;

import com.geetion.puputuan.common.constant.GroupMemberTypeAndStatus;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.GroupMember;
import com.geetion.puputuan.model.Photo;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.model.jsonModel.*;
import com.geetion.puputuan.service.RecommendService;
import com.geetion.puputuan.service.UserService;
import com.geetion.puputuan.supervene.recommend.utils.SpringLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * java bean 转换工具类
 * Created by gdk on 2016/9/26.
 */
public class ConvertBeanUtils {

    public static JSONUserBase convertDBModelToJSONModel(User user) {
        JSONUserBase jsonUserBase = new JSONUserBase();
        jsonUserBase.setUserId(user.getUserId());
        jsonUserBase.setIdentify(user.getIdentify());
        jsonUserBase.setAvatarId(user.getHeadId());
        if(null == user.getHead()){
            jsonUserBase.setAvatarUrl(null);
        }else{
            jsonUserBase.setAvatarUrl(user.getHead().getUrl());
        }

        jsonUserBase.setNickName(EmojiCharacterUtil.emojiRecovery2(user.getNickName()));
        jsonUserBase.setNickNameChar(user.getNickNameChr());
        jsonUserBase.setSex(user.getSex());

        ImUser imUser = new ImUser();
        imUser.setUserName(user.getUserBase().getAccount());
        jsonUserBase.setImUser(imUser);

        return jsonUserBase;
    }

    public static JSONUser convertUserToJSONUser(User user){

        List<JSONUser> jsonUserList = new ArrayList<>();
        JSONUser jsonUser = new JSONUser();
        ImUser imUser = new ImUser();
        imUser.setUserName(user.getUserBase().getAccount());

        jsonUser.setUserId(user.getUserId());
        jsonUser.setIdentify(user.getIdentify());
//        OssFileUtils ossFileUtils = SpringContextUtil.getBean("ossFileUtils");
//        ossFileUtils.getPhotoList(user.getAlbum(), null);
//        List<Photo> photoList = user.getAlbum();
//
//        for(Photo photo : photoList){
//
//            if (photo.getIsAvatar()) {
//                jsonUser.setAvatarId(photo.getId());
//                jsonUser.setAvatarUrl(photo.getImage().getUrl());
//            }
//        }
        jsonUser.setAvatarId(user.getHeadId());
        jsonUser.setAvatarUrl(user.getHead().getUrl());
        jsonUser.setNickName(user.getNickName());
        jsonUser.setNickNameChar(user.getNickNameChr());
        jsonUser.setSex(user.getSex());
        jsonUser.setImUser(imUser);
        jsonUserList.add(jsonUser);

        return jsonUser;
    }
    public static List<JSONUserBase> convertDBListToJSONList(List<User> dbList) {
        List<JSONUserBase> jsonUserBases = new ArrayList<>();

        if (dbList != null && dbList.size() != 0) {
            for (User user : dbList) {
                JSONUserBase jsonModel = ConvertBeanUtils.convertDBModelToJSONModel(user);
                jsonUserBases.add(jsonModel);
            }
            return jsonUserBases;
        }
        return null;
    }

    public static JSONGroup convertToJSONGroup(Group group){
        JSONGroup jgroup = new JSONGroup();
        jgroup.setId(group.getId());

        if (null != group.getAreaId()){
            jgroup.setAreaId(group.getAreaId());
        }

        jgroup.setArea(group.getArea());

        if (null != group.getCityId()){
            jgroup.setCityId(group.getCityId());
        }
        jgroup.setCity(group.getCity());

        if (null != group.getProvinceId()){
            jgroup.setProvinceId(group.getProvinceId());
        }
        jgroup.setProvince(group.getProvince());
        jgroup.setBarId(group.getBarId());
        jgroup.setType(group.getType());
        jgroup.setRoomId(group.getRoomId());
        jgroup.setName(EmojiCharacterUtil.emojiRecovery2(group.getName()));
        jgroup.setStatus(group.getStatus());
        jgroup.setRecommendSex(group.getRecommendSex());
        return jgroup;
    }

    public static List<JSONGroupMember> converToJSONGroupMember(List<GroupMember> groupMemberList){
        List<JSONGroupMember> jsonGroupMemberList = new ArrayList<>();

        for(GroupMember groupMember : groupMemberList){
            JSONGroupMember jsonGroupMember = new JSONGroupMember();
            JSONUser jsonUser = new JSONUser();
            UserService userService = SpringLoader.getInstance().getBean(UserService.class);
            User user = userService.getByUserId(groupMember.getUserId());
            ImUser imUser = new ImUser();
            imUser.setUserName(user.getUserBase().getAccount());

            jsonUser.setUserId(groupMember.getUserId());
            jsonUser.setIdentify(user.getIdentify());

            OssFileUtils ossFileUtils = SpringLoader.getInstance().getBean(OssFileUtils.class);
            ossFileUtils.getPhotoList(user.getAlbum(), null);
            List<Photo> photoList = user.getAlbum();

            for(Photo photo : photoList){

                if (photo.getIsAvatar()) {
                    jsonUser.setAvatarId(photo.getId());
                    jsonUser.setAvatarUrl(photo.getImage().getUrl());
                }
            }
            jsonUser.setNickName(EmojiCharacterUtil.emojiRecovery2(groupMember.getUser().getNickName()));
            jsonUser.setNickNameChar(groupMember.getUser().getNickNameChr());
            jsonUser.setSex(groupMember.getUser().getSex());
            jsonUser.setImUser(imUser);
            jsonGroupMember.setUser(jsonUser);
            jsonGroupMember.setStatus(groupMember.getStatus());
            jsonGroupMember.setIsLeader(groupMember.getType() == GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_LEADER ? true : false);

            jsonGroupMemberList.add(jsonGroupMember);
        }

        return jsonGroupMemberList;
    }
}
