package com.geetion.puputuan.web.api.app;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.model.User;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 移动端 用户 接口
 * Created by jian on 25/3/16.
 */
@RequestMapping("/app/friend")
public interface FriendController {


    /**
     * 查询我的好友
     *
     * @param methodType -- 1，不分页查询所有数据；
     *                   -- 2，分页查询
     * @param pageEntity
     * @param object     //不填则查询自己的好友，如果填则查询好友的好友
     * @param orderByFirstLetter //true表示根据首字母排序，false表示按照添加时间排序
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    Object search(Integer methodType, @ModelAttribute PageEntity pageEntity, @ModelAttribute User object,
                  Boolean orderByFirstLetter);


    /**
     * 申请添加用户
     *
     * @param identify identify
     * @return
     */
    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ResponseBody
    Object applyFriend(String identify);


    /**
     * 查询申请添加我为好友的申请
     *
     * @return
     */
    @RequestMapping(value = "/searchApply", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    Object searchApply(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity);


    /**
     * 同意用户的添加申请
     *
     * @param messageId      消息列表的ID
     * @param friendApplyId      用户申请列表的ID
     * @param isAgree 是否同意添加
     * @return
     */
    @RequestMapping(value = "/agreeApplyOrNot", method = RequestMethod.POST)
    @ResponseBody
    Object agreeApplyOrNot(Long messageId,Long friendApplyId, Boolean isAgree);


    /**
     * 删除好友
     *
     * @param identify 好友的identify
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    Object deleteFriend(String identify);

    /**
     * 查询某个用户是否是我的好友
     *
     * @param userId 好友的userId
     * @return
     */
    @RequestMapping(value = "/isMyFriend", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    Object isMyFriend(Long userId);

}
