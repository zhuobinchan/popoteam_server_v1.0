package com.geetion.puputuan.web.api.app;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.model.Consultation;
import com.geetion.puputuan.model.User;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 移动端 用户 接口
 * Created by jian on 25/3/16.
 */
@RequestMapping("/app/group")
public interface GroupController {


    /**
     * 查询可以创建队友群的同性好友
     *
     * @param methodType -- 1，不分页查询所有数据；
     *                   -- 2，根据主键ID查询数据；
     *                   -- 3，分页查询，也可以根据关键字查询数据，只需要传对应的参数，
     *                   例如根据姓名查询，则要传一个name参数，不填则查询全部
     * @param id         配合methodType = 2使用
     * @param pageEntity
     * @param object     User 的字段;  //可选，不填则默认选择所有
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    Object search(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity, @ModelAttribute User object, String friendSex,
                  Boolean orderByFirstLetter);


    /**
     * 查询推荐过来的其他队伍
     *
     * @param recommendId 推送的id
     * @return
     */
    @RequestMapping(value = "/searchGroupByRecommend", method = RequestMethod.GET)
    @ResponseBody
    Object searchGroupByRecommend(Long recommendId);


    /**
     * 查询正在进行中的团队
     *
     * @return
     */
    @RequestMapping(value = "/runningGroup", method = RequestMethod.GET)
    @ResponseBody
    Object searchRunningGroup();


    /**
     * 创建群聊
     * @param identifys
     * @param provinceId
     * @param province
     * @param cityId
     * @param city
     * @param areaId
     * @param area
     * @param barId
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    Object createGroup(String[] identifys, Long provinceId, String province, Long cityId, String city, Long areaId, String area, Long barId, Integer matchGroupType, Long[] disLikeGroups, Long likeGroupId);

    /**
     *
     * 更新团队信息，团队名或地点
     * @param groupName
     * @param barId
     * @param cityId
     * @param city
     * @param areaId
     * @param area
     * @return
     */
    @RequestMapping(value = "/updateGroup", method = RequestMethod.POST)
    @ResponseBody
    Object updateGroup(String groupName, Long barId, Long provinceId, String province, Long cityId, String city, Long areaId, String area, Integer matchGroupType);


    /**
     * 邀请好友进群
     * @param identifys
     * @return
     */
    @RequestMapping(value = "/invite", method = RequestMethod.POST)
    @ResponseBody
    Object invite(String[] identifys);


    /**
     * 查询群成员
     *
     * @return
     */
    @RequestMapping(value = "/member", method = RequestMethod.GET)
    @ResponseBody
    Object groupMember();


    /**
     * 查询邀请我进群的记录
     *
     * @param methodType -- 1，不分页查询所有数据；
     *                   -- 2，根据主键ID查询数据；
     *                   -- 3，分页查询；
     * @param id         配合methodType = 2使用
     * @param pageEntity
     * @return
     */
    @RequestMapping(value = "/searchInvite", method = RequestMethod.GET)
    @ResponseBody
    Object searchInvite(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity);


    /**
     * 同意加入群
     *
     * @return
     */
    @RequestMapping(value = "/agree", method = RequestMethod.POST)
    @ResponseBody
    @Deprecated
    Object agreeGroup(Long groupId);

    /**
     * 将队友踢出群
     *
     * @return
     */
    @RequestMapping(value = "/removeMember", method = RequestMethod.POST)
    @ResponseBody
    Object removeGroupMember(String[] identifys);



    /**
     * 退出群或解散
     *
     * @return
     */
    @RequestMapping(value = "/quit", method = RequestMethod.GET)
    @ResponseBody
    Object quitGroup();


    /**
     * 开始匹配
     *
     * @return
     */
    @RequestMapping(value = "/startMatch", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    Object startMatch();


    /**
     * 结束匹配
     *
     * @return
     */
    @RequestMapping(value = "/endMatch", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    Object endMatch();


    /**
     * 给对方队伍成员投票
     * 不管投票成功与否，直接返回成功
     *
     * @return
     */
    @RequestMapping(value = "/vote", method = RequestMethod.POST)
    @ResponseBody
    Object vote(Long recommendId, Boolean isLike);


    /**
     * 推送推荐的队伍给用户
     *
     * @return
     */
    @RequestMapping(value = "/pushRecommend", method = RequestMethod.GET)
    @ResponseBody
    Object pushRecommend(Long groupId);


    /**
     * 查询投票的状态
     *
     * @return
     */
    @RequestMapping(value = "/searchVoteStatus", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    Object searchVoteStatus();

    /**
     * 查询资讯
     *
     * @param methodType -- 1，不分页查询所有数据；
     *                   -- 2，根据主键ID查询数据；
     *                   -- 3，分页查询，也可以根据关键字查询数据，只需要传对应的参数，不填则查询全部
     * @param id         配合methodType = 2使用
     * @param pageEntity
     * @param object     Consultation 的字段;  //可选，不填则默认选择所有
     * @return
     */
    @RequestMapping(value = "/searchConsultation", method = RequestMethod.GET)
    @ResponseBody
    Object searchConsultation(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                              @ModelAttribute Consultation object);

    /**
     *
     * @param excludeUrId
     * @return
     */
    @RequestMapping(value = "/searchRecommendTeam", method = RequestMethod.GET)
    @ResponseBody
    Object searchRecommendTeam(String excludeUrId);

    @RequestMapping(value = "/dissolution", method = RequestMethod.GET)
    @ResponseBody
    Object dissolution();

    /**
     * 通过市、地区查询队伍
     * @param cityId
     * @param areaId
     * @return
     */
    @RequestMapping(value = "/searchLocalGroup", method = RequestMethod.GET)
    @ResponseBody
    Object searchLocalGroup(Long cityId, Long areaId);

    /**
     * 用户superlike某个推荐队伍，直接生成约会信息
     * @param userRecommendId
     * @return
     */
    @RequestMapping(value = "/superLike", method = RequestMethod.GET)
    @ResponseBody
    Object superLike(Long userRecommendId);
}
