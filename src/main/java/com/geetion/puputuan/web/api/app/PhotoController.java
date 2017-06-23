package com.geetion.puputuan.web.api.app;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.model.Photo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * 移动端 用户 接口
 * Created by jian on 25/3/16.
 */
@RequestMapping("/app/photo")
public interface PhotoController {


    /**
     * 查询 相册
     *
     * @param methodType -- 1，不分页查询所有数据；
     *                   -- 2，根据主键ID查询数据；
     *                   -- 3，分页查询，也可以根据关键字查询数据，只需要传对应的参数，
     *                   例如根据姓名查询，则要传一个name参数，不填则查询全部
     * @param id         配合methodType = 2使用，相片详情
     * @param pageEntity
     * @param object     如果object里的userId不为空，则查询好友的相册，如果为空，则查询自己的相册
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    Object search(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                  @ModelAttribute Photo object, String style);


    /**
     * 发布相片
     *
     * @param files 用户上传的图片数组
     * @return
     */
    @RequestMapping(value = "/release", method = RequestMethod.POST)
    @ResponseBody
    Object releasePhoto(@RequestParam("uploadfiles") CommonsMultipartFile[] files);


    /**
     * 删除相片
     *
     * @param albumPhotoId photoId
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    Object deletePhoto(Long albumPhotoId);


    /**
     * 给相片点赞 或取消
     *
     * @param id photoId
     * @return
     */
    @RequestMapping(value = "/likePhotoOrNot", method = RequestMethod.POST)
    @ResponseBody
    @Deprecated
    Object likePhotoOrNot(Long id);


    /**
     * 查询 点赞详情
     *
     * @param methodType -- 1，不分页查询所有数据；
     *                   -- 2，根据主键ID查询数据；
     *                   -- 3，分页查询，也可以根据关键字查询数据，只需要传对应的参数，
     *                   例如根据姓名查询，则要传一个name参数，不填则查询全部
     * @param id         配合methodType = 2使用，相片详情
     * @param pageEntity
     * @return
     */
    @RequestMapping(value = "/searchLike", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    Object searchLike(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity);

    // TODO add by simon at 2016/08/03 -- 之前获取点赞人列表是用上面那个接口，现在另外增加一个接口
    /*
    * 获取 点赞人列表
    *
    * @param albumPhotoId -- 必填，要查询的相片albumPhotoId
    * @param methodType -- 方法参数
    *                   -- 1，不分页查询所有数据；
    *                   -- 2，分页查询
    * @param page  methodType = 2时必填，分页的页数
    * @param size  methodType = 2时必填，每一页的数量
    * @return
    */
    @RequestMapping(value = "/searchLikeUser", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    Object searchLikeUser(Long albumPhotoId, Integer methodType, @ModelAttribute PageEntity pageEntity);

    /**
     * 投诉相片
     *
     * @param id      photoId
     * @param content 投诉内容
     * @return
     */
    @RequestMapping(value = "/complain", method = RequestMethod.POST)
    @ResponseBody
    @Deprecated
    Object complainPhoto(Long id, String content);

    // TODO add by simon at 2016/08/04 -- 增加精选照片的接口
    @RequestMapping(value = "/getFavouritePic", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    Object getFavouritePic(Long userId);

    @RequestMapping(value = "/updateFavouritePic", method = RequestMethod.POST)
    @ResponseBody
    @Deprecated
    Object updateFavouritePic(Long imageId);
}
