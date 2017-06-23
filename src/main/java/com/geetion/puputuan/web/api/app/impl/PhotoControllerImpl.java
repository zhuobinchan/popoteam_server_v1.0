package com.geetion.puputuan.web.api.app.impl;

import com.geetion.generic.permission.service.ShiroService;
import com.geetion.generic.serverfile.model.File;
import com.geetion.generic.userbase.service.UserBaseService;
import com.geetion.puputuan.common.constant.LikeType;
import com.geetion.puputuan.common.constant.PhotoType;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.common.utils.TransactionHelper;
import com.geetion.puputuan.model.*;
import com.geetion.puputuan.model.jsonModel.AlbumPhoto;
import com.geetion.puputuan.model.jsonModel.UserFavouritePic;
import com.geetion.puputuan.service.*;
import com.geetion.puputuan.utils.OssFileUtils;
import com.geetion.puputuan.web.api.app.PhotoController;
import com.geetion.puputuan.web.api.base.BaseController;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by jian on 25/3/16.
 */
@Controller
public class PhotoControllerImpl extends BaseController implements PhotoController {

    @Resource(name = "geetionShiroService")
    private ShiroService shiroService;
    @Resource(name = "geetionUserBaseService")
    private UserBaseService userBaseService;
    @Resource
    private UserService userService;
    @Resource
    private PhotoService photoService;
    @Resource
    private PhotoLikeService photoLikeService;
    @Resource
    private FriendRelationshipService friendRelationshipService;
    @Resource
    private ComplainService complainService;
    @Resource
    private UserFavouritePicService userFavouritePicService;

    @Resource
    private OssFileUtils ossFileUtils;

    @Resource
    private TransactionHelper transactionHelper;

    @Deprecated
    @Override
    public Object search(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity,
                         @ModelAttribute Photo object, String style) {
        logger.info("PhotoControllerImpl search begin...");
        if (checkParaNULL(methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            param.putAll(pojoToMap(object));
            //查看未删除的照片
//            param.put("isDelete", PhotoType.NOT_DELETE);
            //如果userId不为空，则查询好友的相册，如果为空，则查询自己的相册
            if (checkParaNULL(object, object.getUserId())) {
                param.put("userId", object.getUserId());
            } else {
                param.put("userId", shiroService.getLoginUserBase().getId());
            }

            switch (methodType) {
                case 1:
                    List<Photo> list = photoService.getPhotoList(param);
                    //获取图片
                    ossFileUtils.getPhotoList(list, style);
                    resultMap.put("list", list);
                    break;
                case 2:
                    if (checkParaNULL(id)) {
                        Photo obj = photoService.getPhotoById(id);
                        //获取图片
                        ossFileUtils.getPhoto(obj, style);
                        resultMap.put("object", obj);
                    } else {
                        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
                    }
                    break;
                case 3:
                    if (pageEntity != null) {
                        pageEntity.setParam(param);
                    }
                    PagingResult<Photo> pagingForKeyword = photoService.getPhotoPage(pageEntity);
                    //获取图片
                    ossFileUtils.getPhotoPage(pagingForKeyword, style);
                    resultMap.put("list", pagingForKeyword.getResultList());
                    resultMap.put("totalPage", pagingForKeyword.getTotalPage());
                    resultMap.put("totalSize", pagingForKeyword.getTotalSize());
                    resultMap.put("currentPage", pagingForKeyword.getCurrentPage());
                    break;

                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    public Object releasePhoto(@RequestParam("uploadfiles") CommonsMultipartFile[] files) {
        logger.info("PhotoControllerImpl releasePhoto begin...");
        if (files.length != 0) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            //查看未删除的照片
            param.put("userId", shiroService.getLoginUserBase().getId());
            List<Photo> photos = photoService.getPhotoList(param);

            //每个用户最多只能发布6张照片
            if ((photos.size() + files.length) > 6) {
                return sendResult(ResultCode.CODE_650.code, ResultCode.CODE_650.msg, null);
            }

            List<AlbumPhoto> albumPhotoList = new ArrayList<>();

            /** 开启事务 */
            TransactionStatus transactionStatus = transactionHelper.start();
            try {
                for (int i = 0; i < files.length; i++) {
                    File pojoFile = ossFileUtils.uploadFile(shiroService.getLoginUserBase().getId(), files[i], (long) 0);
                    if (null == pojoFile) {
                        /** 回滚事务 */
                        transactionHelper.rollback(transactionStatus);
                        return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
                    }

                    //插入相册表,设置为头像
                    Photo photo = new Photo();
                    photo.setImageId(pojoFile.getId());
                    photo.setUserId(shiroService.getLoginUserBase().getId());
//                    photo.setIsDelete(false);
                    photo.setIsAvatar(false);
                    photo.setImage(pojoFile);
                    photoService.addPhoto(photo);

                    //转换JSON model
                    AlbumPhoto albumPhoto = new AlbumPhoto();
                    albumPhoto.setAlbumPhotoId(photo.getId());
                    albumPhoto.setImageId(photo.getImageId());
                    albumPhoto.setImageUrl(pojoFile.getUrl());
                    albumPhoto.setIsAvatar(photo.getIsAvatar());
                    albumPhotoList.add(albumPhoto);
                }

                transactionHelper.commit(transactionStatus);

            } catch (Exception e) {
                logger.info("PhotoControllerImpl releasePhoto error " + e);
                /** 回滚事务 */
                transactionHelper.rollback(transactionStatus);
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }

            if (albumPhotoList.size() > 0) {
                resultMap.put("list", albumPhotoList);
            } else {
                resultMap.put("list", null);
            }

            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Override
    @Transactional(timeout = 5000)
    public Object deletePhoto(Long albumPhotoId) {  // TODO modify by simon at 2016/08/08
        logger.info("PhotoControllerImpl deletePhoto begin...");
        if (checkParaNULL(albumPhotoId)) {
            Map<String, Object> param = new HashMap<>();
//            param.put("isDelete", PhotoType.NOT_DELETE);
            param.put("userId", shiroService.getLoginUserBase().getId());
            List<Photo> list = photoService.getPhotoList(param);

            //如果是最后一张照片则不允许删除
            if (list.size() == 1 && list.get(0).getId() == albumPhotoId) {
                return sendResult(ResultCode.CODE_651.code, ResultCode.CODE_651.msg, null);
            }

            Photo photo = null;
            for (Photo pic : list) {
                //在用户的相册列表中找到当前待删除的photo
                if (albumPhotoId.compareTo(pic.getId()) == 0) {
                    photo = pic;
                    break;
                }
            }

            if (photo != null) {
                //如果相片被设置为头像,则需要修改头像为另一张相片
                if (photo.getIsAvatar()) {
                    //经过上面的合法性判断,这里必然能找到一张非头像照片
                    for (Photo toBeAvatar: list) {
                        if (!toBeAvatar.getIsAvatar()) {
                            toBeAvatar.setIsAvatar(true);
                            photoService.updatePhoto(toBeAvatar);

                            //修改user表中的headId
                            User user = new User();
                            user.setUserId(shiroService.getLoginUserBase().getId());
                            user.setHeadId(toBeAvatar.getImageId());
                            userService.updateUserByUserId(user);

                            break;
                        }
                    }
                }

                //获取File对象
                ossFileUtils.getPhoto(photo, null);

                //删除文件,photo表的数据通过外键约束,在删除file表数据时自动删除
                ossFileUtils.deleteFile(shiroService.getLoginUserBase().getId(), photo.getImage());
            }
            //如果albumPhotoId找不到对应的图片,作为容错处理,也返回200响应
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Deprecated
    @Override
    public Object likePhotoOrNot(Long id) {
        if (checkParaNULL(id)) {
            try {
                Map<String, Object> param = new HashMap<>();
                param.put("photoId", id);
                param.put("userId", shiroService.getLoginUserBase().getId());
                Photo photo = photoService.getPhotoById(id);

                boolean result = false;

                //先查询是否已点过赞
                PhotoLike photoLike = photoLikeService.getPhotoLike(param);
                if (photoLike != null) {
                    //如果点过，则取消
                    result = photoLikeService.removePhotoLike(photoLike.getId());
                } else {

                    param.clear();
                    param.put("userId", shiroService.getLoginUserBase().getId());
                    param.put("friendId", photo.getUserId());

                    //查询是否是朋友关系
                    FriendRelationship friendRelationship = friendRelationshipService.getFriendRelationship(param);

                    //没有这新增一条记录
                    photoLike = new PhotoLike();
                    photoLike.setPhotoId(id);
                    photoLike.setUserId(shiroService.getLoginUserBase().getId());
                    photoLike.setIsFriend(friendRelationship != null ? LikeType.IS_FRIEND : LikeType.NOT_FRIEND);
                    result = photoLikeService.addPhotoLike(photoLike);
                }
                if (result) {
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
                }
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);

            } catch (DuplicateKeyException e) {
                if (e.getMessage().contains("uq_like_photo_user")) {
                    System.out.println("\nDuplicateKeyException 已经点过赞了");
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
                }
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);

            } catch (Exception e) {
                e.printStackTrace();
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }

        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Deprecated
    @Override
    public Object searchLike(Integer methodType, Long id, @ModelAttribute PageEntity pageEntity) {
        if (checkParaNULL(methodType, id)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();


            switch (methodType) {
                case 1:
                    List<PhotoLike> list = photoLikeService.getPhotoLikeList(param);
                    //获得点赞的用户的头像
                    if (list != null && list.size() != 0) {
                        for (PhotoLike photoLike : list) {
                            photoLike.getUser().setHead(ossFileUtils.getPictures(photoLike.getUser().getHeadId(), null));
                        }
                    }
                    resultMap.put("list", list);
                    resultMap.put("size", list == null ? 0 : list.size());
                    break;
                case 2:
                    Long userId = shiroService.getLoginUserBase().getId();
                    param.put("id", id);
                    param.put("userId", userId);
                    Photo obj = photoService.getPhotoById(id);
                    int likeCount = photoLikeService.countPhotoLikeByPhotoId(id);
                    boolean isLike = photoLikeService.userIsLikePhoto(param);

                    User user = null;
                    if (obj != null) {
                        //获得点赞的用户的头像
                        obj.setImage(ossFileUtils.getPictures(obj.getImageId(), null));
                        user = userService.getByUserId(obj.getUserId());
                        ossFileUtils.getUserHead(user, null);
                    }
                    resultMap.put("user", user);
                    resultMap.put("object", obj);
                    resultMap.put("likeCount", likeCount);
                    resultMap.put("isLike", isLike);
                    break;
                case 3:
                    //查询相片的点赞情况
                    param.put("photoId", id);
                    if (pageEntity != null) {
                        pageEntity.setParam(param);
                    }

                    PagingResult<PhotoLike> pagingForKeyword = photoLikeService.getPhotoLikePage(pageEntity);
                    //获得点赞的用户的头像
                    if (pagingForKeyword != null && pagingForKeyword.getResultList() != null) {
                        for (PhotoLike photoLike : pagingForKeyword.getResultList()) {
                            photoLike.getUser().setHead(ossFileUtils.getPictures(photoLike.getUser().getHeadId(), null));
                        }
                    }
                    resultMap.put("list", pagingForKeyword.getResultList());
                    resultMap.put("totalPage", pagingForKeyword.getTotalPage());
                    resultMap.put("totalSize", pagingForKeyword.getTotalSize());
                    resultMap.put("currentPage", pagingForKeyword.getCurrentPage());
                    break;

                default:
                    break;
            }
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    // TODO add by simon at 2016/08/03 -- 之前获取点赞人列表是用上面那个接口，现在另外增加一个接口
    @Deprecated
    @Override
    public Object searchLikeUser(Long albumPhotoId, Integer methodType, @ModelAttribute PageEntity pageEntity) {
        if (checkParaNULL(albumPhotoId, methodType)) {
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();

            switch (methodType) {
                case 1:
                    List<PhotoLike> list = photoLikeService.getPhotoLikeList(param);
                    //获得点赞的用户的头像
                    if (list != null && list.size() != 0) {
                        for (PhotoLike photoLike : list) {
                            photoLike.getUser().setHead(ossFileUtils.getPictures(photoLike.getUser().getHeadId(), null));
                        }
                    }
                    resultMap.put("list", list);
                    resultMap.put("size", list == null ? 0 : list.size());
                    break;
                case 2:
                    //查询相片的点赞情况
                    param.put("photoId", albumPhotoId);
                    if (pageEntity != null) {
                        pageEntity.setParam(param);
                    }

                    PagingResult<PhotoLike> pagingForKeyword = photoLikeService.getPhotoLikePage(pageEntity);
                    //获得点赞的用户的头像
                    if (pagingForKeyword != null && pagingForKeyword.getResultList() != null) {
                        for (PhotoLike photoLike : pagingForKeyword.getResultList()) {
                            photoLike.getUser().setHead(ossFileUtils.getPictures(photoLike.getUser().getHeadId(), null));
                        }
                    }
                    resultMap.put("list", pagingForKeyword.getResultList());
                    resultMap.put("totalPage", pagingForKeyword.getTotalPage());
                    resultMap.put("totalSize", pagingForKeyword.getTotalSize());
                    resultMap.put("currentPage", pagingForKeyword.getCurrentPage());
                    break;
                default:
                    break;
            }

            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Deprecated
    @Override
    public Object complainPhoto(Long id, String content) {
        if (checkParaNULL(id)) {

            try {
                Map<String, Object> param = new HashMap<>();
                Complain complain = new Complain();
                complain.setPhotoId(id);
                complain.setUserId(shiroService.getLoginUserBase().getId());
                complain.setContent(content);
                boolean result = complainService.addComplain(complain);

                if (result) {
                    return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
                }
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);

            } catch (DuplicateKeyException e) {
                if (e.getMessage().contains("uq_complain_photo_user")) {
                    System.out.println("\nDuplicateKeyException 已经投诉过了");
                    return sendResult(ResultCode.CODE_713.code, ResultCode.CODE_713.msg, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
            }
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }

    @Deprecated
    @Override
    public Object getFavouritePic(Long userId) {
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();

        if (checkParaNULL(userId)) {
            param.put("userId", userId);
        } else {
            param.put("userId", shiroService.getLoginUserBase().getId());
        }
        List<UserFavouritePic> userFavouritePicList = userFavouritePicService.getFavouritePicList(param);

        if(userFavouritePicList != null && userFavouritePicList.size() > 0) {
            resultMap.put("list", userFavouritePicList);
            return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, resultMap);
        }
        return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
    }

    @Deprecated
    @Override
    public Object updateFavouritePic(Long imageId) {
        if (checkParaNULL(imageId)) {
            boolean result;

//            Map<String, Object> param = new HashMap<>();
//            param.put("imageId", imageId);
//            UserFavouritePic userFavouritePic = userFavouritePicService.getFavouritePic(param);
//            if (userFavouritePic != null) {
//                result = userFavouritePicService.removeFavouritePicById(userFavouritePic.getId());
//
//                Photo photo = photoService.getPhotoById(imageId);
//                photo.setFavourite(false);
//                result = photoService.updatePhoto(photo);
//            } else {
//                userFavouritePic = new UserFavouritePic();
//                userFavouritePic.setUserId(shiroService.getLoginUserBase().getId());
//                userFavouritePic.setAlbumPhotoId(imageId);
//                userFavouritePic.setImageId(imageId);
//                userFavouritePic.setImageUrl(ossFileUtils.getPicturesUrl(photoService.getPhotoById(imageId).getImageId(), null));
//                result = userFavouritePicService.addFavouritePic(userFavouritePic);
//
//                Photo photo = photoService.getPhotoById(imageId);
//                photo.setFavourite(true);
//                result = photoService.updatePhoto(photo);
//
//                // TODO add by simon at 2016/08/06 -- 修改头像
//                User user = userService.getByUserId(shiroService.getLoginUserBase().getId());
//                user.setHeadId(photoService.getPhotoById(imageId).getImageId());
//                userService.updateUserByUserId(user);
//
//            }

            UserFavouritePic userFavouritePic = new UserFavouritePic();
            userFavouritePic.setUserId(shiroService.getLoginUserBase().getId());
            userFavouritePic.setAlbumPhotoId(imageId);
            userFavouritePic.setImageId(imageId);
            userFavouritePic.setImageUrl(ossFileUtils.getPicturesUrl(photoService.getPhotoById(imageId).getImageId(), null));
            result = userFavouritePicService.addFavouritePic(userFavouritePic);

            Photo photo = photoService.getPhotoById(imageId);
            photo.setIsAvatar(true);
            result = photoService.updatePhoto(photo);

            // TODO add by simon at 2016/08/06 -- 修改头像
            User user = userService.getByUserId(shiroService.getLoginUserBase().getId());
            user.setHeadId(photoService.getPhotoById(imageId).getImageId());
            userService.updateUserByUserId(user);

            Map<String, Object> param = new HashMap<>();
            param.put("userId", shiroService.getLoginUserBase().getId());
            List<Photo> list = photoService.getPhotoList(param);

            for (Photo pic : list) {
                if (!pic.getId().equals(imageId)) {
                    if (pic.getIsAvatar()) {
                        pic.setIsAvatar(false);
                        photoService.updatePhoto(pic);

                        param.put("imageId", pic.getId());
                        UserFavouritePic oldPic = userFavouritePicService.getFavouritePic(param);
                        if (oldPic != null) {
                            userFavouritePicService.removeFavouritePicById(oldPic.getId());
                        }
                        break;
                    }
                }
            }

            if (result) {
                return sendResult(ResultCode.CODE_200.code, ResultCode.CODE_200.msg, null);
            }
            return sendResult(ResultCode.CODE_500.code, ResultCode.CODE_500.msg, null);
        }
        return sendResult(ResultCode.CODE_401.code, ResultCode.CODE_401.msg, null);
    }


}
