package com.geetion.puputuan.web.api.app;

import com.geetion.puputuan.common.mybatis.PageEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 移动端 用户 接口
 * Created by jian on 25/3/16.
 */
@RequestMapping("/app/message")
public interface MessageController {


    /**
     * 查询我的消息
     *
     * @param methodType -- 1，不分页查询所有数据；
     *                   -- 2，分页查询，也可以根据关键字查询数据，只需要传对应的参数，
     *                   例如根据姓名查询，则要传一个name参数，不填则查询全部
     * @param pageEntity
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    Object search(Integer methodType, @ModelAttribute PageEntity pageEntity);


    /**
     * 设置消息为已读
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/setRead", method = RequestMethod.POST)
    @ResponseBody
    Object setRead(Long id);


    /**
     * 查询有几条未读信息
     *
     * @return
     */
    @RequestMapping(value = "/unReadNum", method = RequestMethod.GET)
    @ResponseBody
    Object unReadNum();


    /**
     * 查询公告信息
     *
     * @return
     */
    @RequestMapping(value = "/searchAnnouncement", method = RequestMethod.GET)
    @ResponseBody
    Object searchAnnouncement(Long id);

    /**
     * 删除单条消息
     * @param id
     * @return
     */
    @RequestMapping(value = "/delMsg", method = RequestMethod.POST)
    @ResponseBody
    Object delMsg(Long id);

    /**
     * 删除多条消息
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delMulMsg", method = RequestMethod.POST)
    @ResponseBody
    Object delMulMsg(List<Long> ids);

    /**
     * 删除用户名下所有消息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/delAllMsg", method = RequestMethod.POST)
    @ResponseBody
    Object delAllMsg(Long userId);


}
