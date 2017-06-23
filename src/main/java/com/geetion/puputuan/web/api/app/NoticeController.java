package com.geetion.puputuan.web.api.app;

import com.geetion.puputuan.common.mybatis.PageEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 移动端 用户 接口
 *
 */
@RequestMapping("/app/notice")
public interface NoticeController {


    /**
     * 查询我的公告消息
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
     * 删除单条公告消息
     * @param id
     * @return
     */
    @RequestMapping(value = "/delNotice", method = RequestMethod.POST)
    @ResponseBody
    Object delNotice(Long id);

    /**
     * 删除多条公告消息
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delMulNotice", method = RequestMethod.POST)
    @ResponseBody
    Object delMulNotice(List<Long> ids);

    /**
     * 删除用户名下所有公告消息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/delAllNotice", method = RequestMethod.POST)
    @ResponseBody
    Object delAllNotice(Long userId);


}
