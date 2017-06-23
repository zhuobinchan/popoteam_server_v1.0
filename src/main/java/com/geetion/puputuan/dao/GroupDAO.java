package com.geetion.puputuan.dao;

import com.geetion.puputuan.dao.base.BaseDAO;
import com.geetion.puputuan.model.Group;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.GroupDetailWithCount;
import com.geetion.puputuan.pojo.GroupStatisData;
import com.geetion.puputuan.pojo.GroupWithNumberList;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GroupDAO extends BaseDAO<Group, Long> {

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
    public List<User> selectUserByStatus(Map param);

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
    public List<Group> selectGroupByStatus(Map param);

    /**
     * 查询群组信息，带有当前人数和历史人数
     *
     * @return
     */
    public List<GroupWithNumberList> selectWithNumberList(Map param);

    /**
     * 查询群组信息，带有签到人数
     *
     * @return
     */
    public GroupWithNumberList selectWithSignNumber(Map param);

    /**
     * 通过area_id、city_id、bar_id、type进行查询
     * @param param
     * @return
     */
    List<Group> selectMatchGroupByParam(Map param);

    /**
     * 查询出投票给本队的异性队伍Ｒ
     * @param param
     * @return
     */
    List<Group> selectVoteGroup(Map param);

    List<GroupDetailWithCount> selectGroupDetailCount(Map param);

    List<GroupStatisData> selectGroupStatisData(Map param);

    List<GroupStatisData> selectGroupBarStatisData(Map param);

    List<GroupStatisData> selectGroupRegionStatisData(Map param);

    GroupStatisData selectGroupSumStatisData();

    GroupStatisData selectGroupSumForCharts(Map param);

    List<GroupStatisData> selectGroupSumForBar(Map param);

    List<GroupDetailWithCount> selectGroupDetailCountByIds(Map param);
    /**
     * 通过存储过程进行队伍日活情况更新
     * @param param
     */
    void callGroupDailyLiving(Map param);


    List<Long> selectGroupIdByParam(Map param);

    Integer updateGroupBarId(Map param);
}