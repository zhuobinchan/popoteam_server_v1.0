package com.geetion.puputuan.web.api.ctrl;

import com.geetion.puputuan.common.mybatis.PageEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by chenzhuobin on 2017/5/3.
 */
@RequestMapping("/ctrl/simple")
public interface SimpleManageController {
    /**
     * * 通过daokey找出对应的dao并进行分页操作
     * @param daoKey
     * @param paramsJson 查询所带的参数，是一条json的字符串
     *                   将json字符串转成map
     *
     * @param methodType
     * methodType ：1   不分页查询
     * methodType ：2   通过id查询
     * methodType ：3   分页查询
     *
     * @param id
     * @param daoKey
     * @param paramsJson
     * 通过Json字符串 得到筛选信息
     *
     * @param pageEntity
     * @return
     */

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    Object search(Integer methodType, Long id, String daoKey, String paramsJson, @ModelAttribute PageEntity pageEntity);


    /**
     *  通过daokey找出对应的dao并进行分页操作
     * 可以进行复杂的查询
     * 可以再该dao下进行，调用一些特殊的方法名
     * @param methodType
     * methodType ：1   不分页查询
     * methodType ：2   通过id查询
     * methodType ：3   分页查询
     *
     * @param id
     * @param daoKey
     * @param methodKey
     * @param paramsJson
     * 通过Json字符串 得到筛选信息
     *
     * @param pageEntity
     * @return
     */
    @RequestMapping(value = "/complexSearch", method = RequestMethod.POST)
    @ResponseBody
    Object complexSearch(Integer methodType, Long id,String daoKey, String methodKey, String paramsJson, @ModelAttribute PageEntity pageEntity);

    /**
     * 通过daokey找出对应的dao并,并对该object进行status属性的更新操作
     * @param daoKey
     * @param ids
     * @param status
     * @return
     */

    @RequestMapping(value = "/setStatus", method = RequestMethod.POST)
    @ResponseBody
    Object setStatus(String daoKey,Long[] ids,Integer status);
}
