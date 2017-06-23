package com.geetion.puputuan.web.api.ctrl;



import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.model.Advertisement;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.Objects;

/**
 * 广告管理接口
 * Created by chenzhuobin on 2017/3/22.
 */
@RequestMapping("/ctrl/advertisement")
public interface AdvMengerController {

    /**
     *
     ** @param methodType -- 1，不分页查询所有数据；
     *                   -- 2，根据主键ID查询数据；
     *                   -- 3，分页查询，也可以根据关键字查询数据，只需要传对应的参数，不填则查询全部
     * @param id         配合methodType = 2使用
     * @param pageEntity
     * @param object     Interest 的字段;  //可选，不填则默认选择所有
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    Object searchAdvertisement(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                     @ModelAttribute Advertisement object);

    /**
     * 通过id数组删除广告
     * 并把图片删除
     * @param ids
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    Object deleteAdvertisement(Long[] ids);

    /**
     *更新广告，如新的图片，则把旧的图片删除
     * @param file 广告的图片
     * @param advertisement
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    Object updateAdvertisement(@RequestParam(value = "file",required = false) CommonsMultipartFile file,@ModelAttribute Advertisement advertisement);

    /**
     *添加广告
     * @param file 广告的图片
     * @param advertisement
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    Object addAdvertisement(@RequestParam(value = "file",required = false) CommonsMultipartFile file,@ModelAttribute Advertisement advertisement);


    /**
     * 修改广告的屏蔽状态
     * @param ids
     * @param status
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/setStatus", method = RequestMethod.POST)
    @ResponseBody
    Object setAdvertisementsStatus(Long[] ids,int status);

}
