package com.geetion.puputuan.web.api.ctrl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.model.Admin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

/**
 * 后台 管理员 接口
 * Created by jian on 25/3/16.
 */
@RequestMapping("/ctrl/admin")
public interface AdminController {

    /**
     * 注册管理员
     *
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    Object register(String account, String password, String nickName, Long[] roles);


    /**
     * 更新管理员
     *
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    Object update(@ModelAttribute Admin admin);

    /**
     * 更新管理员
     *
     * @return
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    Object updatePassword(String account, String password);


    /**
     * 查询管理员登录历史
     *
     * @param methodType        -- 1，不分页查询所有数据；
     *                          -- 2，分页查询，也可以根据关键字查询数据，只需要传对应的参数，不填则查询全部
     * @param pageEntity
     * @param object            Admin 的字段;  //可选，不填则默认选择所有
     * @return
     */
    @RequestMapping(value = "/searchLoginHis", method = RequestMethod.GET)
    @ResponseBody
    Object searchLoginHis(Integer methodType, @ModelAttribute PageEntity pageEntity,
                  @ModelAttribute Admin object, Date loginTimeBegin, Date loginTimeEnd);

    /**
     * 查询管理员
     *
     * @param methodType        -- 1，不分页查询所有数据；
     *                          -- 2，分页查询，也可以根据关键字查询数据，只需要传对应的参数，不填则查询全部
     * @param pageEntity
     * @param object            Admin 的字段;  //可选，不填则默认选择所有
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    Object search(Integer methodType, @ModelAttribute PageEntity pageEntity, @ModelAttribute Admin object);

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    Object delete(Long userId);

    @RequestMapping(value = "/updateRole", method = RequestMethod.POST)
    @ResponseBody
    Object updateRole(Long userId, Long[] roles);

    @RequestMapping(value = "/updatePsw", method = RequestMethod.POST)
    @ResponseBody
    Object updatePsw(String oldPsw, String newPsw);
}
