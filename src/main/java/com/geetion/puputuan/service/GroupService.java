package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.GroupDetailWithCount;
import com.geetion.puputuan.pojo.GroupStatisData;
import com.geetion.puputuan.pojo.GroupWithNumberList;

import java.util.List;
import java.util.Map;

public interface GroupService {

    Group getGroupById(Long id);

    Group getGroup(Map param);

    List<Group> getGroupList(Map param);

    PagingResult<Group> getGroupPage(PageEntity pageEntity);

    boolean addGroup(Group object);

    boolean updateGroup(Group object);

    boolean removeGroup(Long id);

    /**
     * 查询某个状态下，某个用户的数量
     * 例如：用户id在group表的status = 0的状态下有几条
     * 或者：用户id在group表的status != 0的状态下有几条
     * 或者：用户id在groupMember表的status = 1,2的状态下有几条
     * 或者：用户id在groupMember表的status != 2,3的状态下有几条
     *
     * @param param 包含的参数有:inStatus,notInStatus,inMemberStatus,notInMemberStatus,userId,type
     * @return
     */
    public List<User> getUserByStatusList(Map param);


    /**
     * 查询某个状态下，某个群聊的数量
     * 例如：用户id在group表的status = 0的状态下有几条
     * 或者：用户id在group表的status != 0的状态下有几条
     * 或者：用户id在groupMember表的status = 1,2的状态下有几条
     * 或者：用户id在groupMember表的status != 2,3的状态下有几条
     *
     * @param param 包含的参数有:inStatus,notInStatus,inMemberStatus,notInMemberStatus,userId,type
     * @return
     */
    public List<Group> getGroupByStatusList(Map param);

    /**
     * 查询某个状态下，某个群聊的数量
     * 例如：用户id在group表的status = 0的状态下有几条
     * 或者：用户id在group表的status != 0的状态下有几条
     * 或者：用户id在groupMember表的status = 1,2的状态下有几条
     * 或者：用户id在groupMember表的status != 2,3的状态下有几条
     *
     * @param pageEntity 里的param 包含的参数有:inStatus,notInStatus,inMemberStatus,notInMemberStatus,userId,type
     * @return
     */
    public PagingResult<Group> getGroupByStatusPage(PageEntity pageEntity);


    /**
     * 查询群组信息，带有当前人数和历史人数
     *
     * @return
     */
    public List<GroupWithNumberList> getGroupWithNumberList(Map param);


    /**
     * 查询群组信息，带有当前人数和历史人数 -- 分页
     *
     * @return
     */
    public PagingResult<GroupWithNumberList> getGroupWithNumberPage(PageEntity pageEntity);


    /**
     * 查询群组信息，带有当前人数和历史人数，还有当前用户头像
     *
     * @return
     */
    public GroupWithNumberList getGroupWithSignNumber(Map param);

    /**
     * 查询出投票给本队的异性队伍Ｒ
     * @param param
     * @return
     */
    List<Group> selectVoteGroup(Map param);

    /**
     * 通过area_id、city_id、bar_id、type进行查询
     * @param param
     * @return
     */
    List<Group> selectMatchGroupByParam(Map param);

    public Group getRunningGroup(Long userId);

    List<GroupDetailWithCount> getGroupDetailCount(Map param);

    PagingResult<GroupDetailWithCount> getGroupDetailCountPage(PageEntity pageEntity);

    List<GroupStatisData> getGroupStatisData(Map param);

    PagingResult<GroupStatisData> getGroupStatisDataPage(PageEntity pageEntity);

    List<GroupStatisData> getGroupBarStatisData(Map param);

    PagingResult<GroupStatisData> getGroupBarStatisDataPage(PageEntity pageEntity);

    List<GroupStatisData> getGroupRegionStatisData(Map param);

    GroupStatisData getGroupSumStatisData();

    GroupStatisData getGroupSumForCharts(Map param);

    List<GroupStatisData> getGroupSumForBar(Map param);

    void updateGroupDailyLiving(Long groupId);

    /**
     * 通过id集合进行查询
     * @param param
     * @return
     */
    List<GroupDetailWithCount> getGroupDetailCountByIds(Map param);

    List<Long> getGroupIdByParam(Map param);

    boolean updateGroupBarId(List<Long> groupIds,Long otherBarId);
}
