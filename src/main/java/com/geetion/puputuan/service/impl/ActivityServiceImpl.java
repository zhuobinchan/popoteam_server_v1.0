package com.geetion.puputuan.service.impl;

import com.geetion.generic.permission.service.ShiroService;
import com.geetion.puputuan.common.constant.ActivityTypeAndStatus;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.dao.ActivityDAO;
import com.geetion.puputuan.dao.ActivityMemberDAO;
import com.geetion.puputuan.model.Activity;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.ActivityDetailWithCount;
import com.geetion.puputuan.pojo.ActivityStatisData;
import com.geetion.puputuan.service.ActivityMemberService;
import com.geetion.puputuan.service.ActivityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class ActivityServiceImpl implements ActivityService {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource
    private ActivityDAO activityDAO;
    @Resource
    private ActivityMemberDAO activityMemberDAO;
    @Override
    public Activity getActivityById(Long id) {
        return activityDAO.selectByPrimaryKey(id);
    }

    @Override
    public Activity getActivity(Map param) {
        return activityDAO.selectOne(param);
    }

    @Override
    public List<Activity> getActivityList(Map param) {
        return activityDAO.selectParam(param);
    }

    @Override
    public PagingResult<Activity> getActivityPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<Activity> list = getActivityList(pageEntity.getParam());
        PageInfo<Activity> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public boolean addActivity(Activity object) {
        return activityDAO.insertSelective(object) == 1;
    }

    @Override
    public boolean updateActivity(Activity object) {
        return activityDAO.updateByPrimaryKeySelective(object) == 1;
    }

    @Override
    public boolean updateActivityDissBatch(List<Activity> list) {
        return activityDAO.updateActivityDissBatch(list) == list.size();
    }

    @Override
    public boolean removeActivity(Long id) {
        return activityDAO.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public Activity getActivityByUser(Map param) {
        return activityDAO.selectByUser(param);
    }

    @Override
    public List<User> getActivityMember(Map param) {
        return activityDAO.selectMember(param);
    }

    @Override
    public List<User> getEvaluateUserInActivity(Map param) {
        return activityDAO.selectEvaluateUser(param);
    }

    @Override
    public List<Activity> getRunningActivityList() {
        Map<String, Object> param = new HashMap<>();

        //查询该用户的约会，群聊是组队中状态，约会状态是正在进行中
        param.put("userId", shiroService.getLoginUserBase().getId());
        param.put("type", ActivityTypeAndStatus.IN_ACTIVITY);

//        param.put("userId", shiroService.getLoginUserBase().getId());
        List<Activity> activityList = getActivityList(param);

        return activityList;
    }

    @Override
    public PagingResult<Activity> getRunningActivityPage(PageEntity pageEntity){

        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        Map<String, Object> param = new HashMap<>();
        param.put("userId", shiroService.getLoginUserBase().getId());
        param.put("type", ActivityTypeAndStatus.BEGIN);
        List<Activity> activityList = getActivityList(param);
        PageInfo<Activity> page = new PageInfo<>(activityList);

        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public List<ActivityDetailWithCount> getActivityDetailWithCount(Map param) {
        return activityDAO.selectActivityDetailCount(param);
    }

    @Override
    public PagingResult<ActivityDetailWithCount> getActivityDetailWithCountPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        if (null != pageEntity.getOrderColumn() && null != pageEntity.getOrderTurn()){
            String orderColumn = pageEntity.getOrderColumn();

            if ("maleGroupName".equals(orderColumn)){
                orderColumn = "male_group_name";
            }
            if ("femaleGroupName".equals(orderColumn)){
                orderColumn = "female_group_name";
            }
            if ("roomId".equals(orderColumn)){
                orderColumn = "room_id";
            }
            if ("expireType".equals(orderColumn)){
                orderColumn = "expire_type";
            }
            if ("superLike".equals(orderColumn)){
                orderColumn = "super_like";
            }
            if ("isExpire".equals(orderColumn)){
                orderColumn = "is_expire";
            }
            if ("activityMemberCount".equals(orderColumn)){
                orderColumn = "activity_member_count";
            }
            if ("createTime".equals(orderColumn)){
                orderColumn = "create_time";
            }
            pageEntity.getParam().put("orderBy", "pu_activity_" + orderColumn + " " + pageEntity.getOrderTurn());
//            PageHelper.orderBy("pu_activity_" + orderColumn + " " + pageEntity.getOrderTurn());
        }
        List<ActivityDetailWithCount> list = getActivityDetailWithCount(pageEntity.getParam());
        PageInfo<ActivityDetailWithCount> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public List<ActivityStatisData> getActivityStatisData(Map param) {
        return activityDAO.selectActivityStatisData(param);
    }

    @Override
    public PagingResult<ActivityStatisData> getActivityStatisDataPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<ActivityStatisData> list = getActivityStatisData(pageEntity.getParam());
        PageInfo<ActivityStatisData> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public List<ActivityStatisData> getActivityMatchStatisData(Map param) {
        return activityDAO.selectActivityMatchStatisData(param);
    }

    @Override
    public PagingResult<ActivityStatisData> getActivityMatchStatisDataPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<ActivityStatisData> list = getActivityMatchStatisData(pageEntity.getParam());
        PageInfo<ActivityStatisData> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public List<ActivityStatisData> getActivityAreaStatisData(Map param) {
        return activityDAO.selectActivityAreaStatisData(param);
    }

    @Override
    public PagingResult<ActivityStatisData> getActivityAreaStatisDataPage(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPage(), pageEntity.getSize());
        List<ActivityStatisData> list = getActivityAreaStatisData(pageEntity.getParam());
        PageInfo<ActivityStatisData> page = new PageInfo<>(list);
        return new PagingResult<>(page.getPageNum(), page.getTotal(), page.getPages(), page.getList());
    }

    @Override
    public ActivityStatisData getActivitySumStatisData() {
        return activityDAO.selectActivitySumStatisData();
    }

    @Override
    public List<ActivityStatisData> getActivitySumForBar(Map param) {
        return activityDAO.selectActivitySumForBar(param);
    }

    @Override
    public void deleteActivityAndMember(List<Activity> list) {
        activityDAO.deleteActivityBatch(list);
        activityMemberDAO.deleteActivityMemberBatch(list);
    }

    @Override
    public boolean updateActivityIsExpireByGroupIds(List<Long> groupIds, Integer isExpire) {
        Map<String,Object> param = new HashMap();
        param.put("groupAIds",groupIds);
        param.put("isExpire",isExpire);
        int a= activityDAO.updateActivityIsExpireByGroupIds(param);
        param.remove("groupAIds");
        param.put("groupBIds",groupIds);
        int b = activityDAO.updateActivityIsExpireByGroupIds(param);
        return a>0&&b>0;
    }
}
