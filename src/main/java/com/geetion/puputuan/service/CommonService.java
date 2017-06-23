package com.geetion.puputuan.service;

import com.geetion.generic.permission.service.ShiroService;
import com.geetion.puputuan.common.constant.ActivityTypeAndStatus;
import com.geetion.puputuan.common.constant.GroupMemberTypeAndStatus;
import com.geetion.puputuan.common.constant.GroupTypeAndStatus;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CommonService {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource
    private GroupService groupService;
    @Resource
    private UserService userService;
    @Resource
    private GroupMemberService groupMemberService;
    @Resource
    private ActivityService activityService;
    @Resource
    private ActivityMemberService activityMemberService;

    /**
     * 用户的账号
     */
    public static int USER_ACCOUNT = 0;
    /**
     * 用户的token
     */
    public static int USER_TOKEN = 1;


    //------------------------- Group ---------------------------------------//

    /**
     * 用户是否已经在其他进行中的队伍中
     * 群状态是非结束（创建，匹配，组队）
     * 群成员状态是加入（非邀请，踢出）
     *
     * @param userId 用户userId
     * @return 进行中队伍的用户列表
     */
    public List<User> userHaveInRunningGroup(Long userId) {
        List<Integer> inStatus = new ArrayList<>();
        //查询在创建中状态的群
        inStatus.add(GroupTypeAndStatus.GROUP_CREATE);
        //查询在匹配中状态的群
        inStatus.add(GroupTypeAndStatus.GROUP_MATCHING);
        //查询在组队中状态的群
        inStatus.add(GroupTypeAndStatus.GROUP_TEAM);

        List<Integer> inMemberStatus = new ArrayList<>();
        //查询加入状态的用户
        inMemberStatus.add(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS);
        inMemberStatus.add(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_VOTE);
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("inStatus", inStatus);
        param.put("inMemberStatus", inMemberStatus);

        //如果用户已经在其他正在进行的队中，则list不为空
        return groupService.getUserByStatusList(param);
    }

    /**
     * 查询用户当前进行中的群聊
     * 群状态是非结束（创建，匹配，组队）
     * 群成员状态是加入（非邀请，踢出）
     *
     * @return 进行中的群聊信息
     */
    public Group getRunningGroup() {
        List<Integer> inStatus = new ArrayList<>();
        //查询在创建中状态的群
        inStatus.add(GroupTypeAndStatus.GROUP_CREATE);
        //查询在匹配中状态的群
        inStatus.add(GroupTypeAndStatus.GROUP_MATCHING);
        //查询在组队中状态的群
        inStatus.add(GroupTypeAndStatus.GROUP_TEAM);

        List<Integer> inMemberStatus = new ArrayList<>();
        //查询加入状态的用户
        inMemberStatus.add(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS);
        inMemberStatus.add(GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_VOTE);

        Map<String, Object> param = new HashMap<>();
        param.put("userId", shiroService.getLoginUserBase().getId());
        param.put("inStatus", inStatus);
        param.put("inMemberStatus", inMemberStatus);

        //如果用户已经在正在进行的队中，则list不为空
        List<Group> list = groupService.getGroupByStatusList(param);
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }


    /**
     * 获取group的队长
     *
     * @param groupId 群组id
     * @return 返回群 队长信息
     */
    public User getGroupLeader(Long groupId) {

        Map<String, Object> param = new HashMap<>();
        param.put("type", GroupMemberTypeAndStatus.GROUP_MEMBER_TYPE_LEADER);
        param.put("groupId", groupId);

        GroupMember groupMember = groupMemberService.getGroupMember(param);
        if (groupMember != null) {
            return groupMember.getUser();
        }
        return null;
    }

    /**
     * 获取group的所有成员
     *
     * @param groupId 群组id
     * @return 返回群 群所有成员信息
     */
    public List<GroupMember> getGroupMember(Long groupId) {
        Map<String, Object> param = new HashMap<>();
        param.put("groupId", groupId);
        // 已邀请、已加入、投票中
        param.put("statusList", new int[]{GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_VOTE});
        //获得群成员
        return groupMemberService.getGroupMemberList(param);
    }

    public List<GroupMember> getInviteAndInGroupMember(Long groupId){
        Map<String, Object> param = new HashMap<>();
        param.put("groupId", groupId);
        // 已邀请、已加入、投票中
        param.put("statusList", new int[]{GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_INVITE,
                GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_VOTE});
        //获得群成员
        return groupMemberService.getGroupMemberList(param);
    }

    /**
     * 查询主队和匹配队两队的成员的账户名或是token（token是推送用）
     *
     * @param recommend      推荐对象
     * @param accountOrToken 获取账户或是token
     * @return 用户的账户名account
     */
    public String[] getBothGroupMemberAccountOrToken(Recommend recommend, int accountOrToken) {
        Map<String, Object> param = new HashMap<>();
        //查询主队成员
        param.put("groupId", recommend.getMainGroupId());
        //param.put("status", GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS);
        param.put("statusList", new int[]{GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_VOTE});
        List<GroupMember> maleGroupMemberList = groupMemberService.getGroupMemberList(param);

        //查询匹配队成员
        param.put("groupId", recommend.getMatchGroupId());
        //param.put("status", GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS);
        param.put("statusList", new int[]{GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_VOTE});
        List<GroupMember> femaleGroupMemberList = groupMemberService.getGroupMemberList(param);

        List<String> members = new ArrayList<>();
        for (GroupMember groupMember : maleGroupMemberList) {
            members.add(accountOrToken == USER_ACCOUNT ? groupMember.getUser().getUserBase().getAccount() :
                    groupMember.getUser().getUserBase().getToken());
        }

        for (GroupMember groupMember : femaleGroupMemberList) {
            members.add(accountOrToken == USER_ACCOUNT ? groupMember.getUser().getUserBase().getAccount() :
                    groupMember.getUser().getUserBase().getToken());
        }

        return members.toArray(new String[members.size()]);
    }

    /**
     * 获取group的所有成员的账户名或是token（token是推送用）
     *
     * @param groupId        群组id
     * @param includeSelf    是否包括自己
     * @param accountOrToken 获取账户或是token
     * @return 返回群 群所有成员信息
     */
    public String[] getGroupMemberAccountOrToken(Long groupId, boolean includeSelf, int accountOrToken) {
        Map<String, Object> param = new HashMap<>();
        param.put("groupId", groupId);
        //param.put("status", GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS);
        param.put("statusList", new int[]{GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS, GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_VOTE});
        List<GroupMember> maleGroupMemberList = groupMemberService.getGroupMemberList(param);

        List<String> members = new ArrayList<>();
        for (GroupMember groupMember : maleGroupMemberList) {
            if (!includeSelf) {
                //如果不包括自己，则排除自己的账户
                if (groupMember.getUserId().longValue() == shiroService.getLoginUserBase().getId().longValue()) {
                    continue;
                }
            }
            members.add(accountOrToken == USER_ACCOUNT ? groupMember.getUser().getUserBase().getAccount() :
                    groupMember.getUser().getUserBase().getToken());
        }
        //获得群成员
        return members.toArray(new String[members.size()]);
    }


    /**
     * 查询主队和匹配队两队的成员的账户名或是token（token是推送用）
     *
     * @param accountOrToken 获取账户或是token
     * @param userIds        用户id
     * @return 用户的账户名account
     */
    public String[] getUserAccountOrToken(int accountOrToken, Long... userIds) {

        List<String> members = new ArrayList<>();
        for (Long userId : userIds) {
            //查询每一个用户
            User user = userService.getByUserId(userId);
            //获得对应用户的account或是token
            members.add(accountOrToken == USER_ACCOUNT ? user.getUserBase().getAccount() :
                    user.getUserBase().getToken());
        }
        return members.toArray(new String[members.size()]);
    }

    //------------------------- Activity ---------------------------------------//

    /**
     * 查询用户当前正在进行中的约会
     *
     * @return
     */
    public Activity getRunningActivity() {



        Map<String, Object> param = new HashMap<>();
        //查询该用户的约会，群聊是组队中状态，约会状态是正在进行中
        param.put("userId", shiroService.getLoginUserBase().getId());
        List<Integer> types = new ArrayList<>();
        types.add(ActivityTypeAndStatus.BEGIN);
        types.add(ActivityTypeAndStatus.SUCCESS);
//        param.put("type", ActivityTypeAndStatus.BEGIN);
        param.put("types", types);
        param.put("status", GroupTypeAndStatus.GROUP_TEAM);
        return activityService.getActivityByUser(param);
    }

    /**
     * 查询当前正在进行中的约会的用户
     *
     * @return
     */
    public List<GroupMember> getActivityMember(Long activityId) {
        if (activityId != null) {
            //查询约会
            Activity activity = activityService.getActivityById(activityId);
            if (activity != null) {
                //获得男队成员
                List<GroupMember> groupAMemberList = getGroupMember(activity.getGroupAId());
                //获得女队成员
                List<GroupMember> groupBMemberList = getGroupMember(activity.getGroupBId());
                //返回男女队成员
                if (groupAMemberList.addAll(groupBMemberList)) {
                    return groupAMemberList;
                }
            }
        }
        return null;
    }

    /**
     * 获取Activity的所有成员的账户名或是token（token是推送用）
     *
     * @param activityId     约会id
     * @param includeSelf    是否包括自己
     * @param accountOrToken 获取账户或是token
     * @return
     */
    public String[] getActivityMemberAccountOrToken(Long activityId, boolean includeSelf, int accountOrToken) {

        Map<String, Object> param = new HashMap<>();
        //查询约会的用户成员信息，群聊成员是进入中状态，约会状态是正在进行中
        param.put("id", activityId);
//        param.put("type", ActivityTypeAndStatus.BEGIN);
        param.put("status", GroupMemberTypeAndStatus.GROUP_MEMBER_STATUS_ACCESS);
        //查询成员
        List<User> list = activityService.getActivityMember(param);

        List<String> members = new ArrayList<>();
        for (User user : list) {
            if (!includeSelf) {
                //如果不包括自己，则排除自己的账户
                if (user.getUserId().longValue() == shiroService.getLoginUserBase().getId().longValue()) {
                    continue;
                }
            }
            members.add(accountOrToken == USER_ACCOUNT ? user.getUserBase().getAccount() : user.getUserBase().getToken());
        }
        //获得约会成员
        return members.toArray(new String[members.size()]);
    }


}
