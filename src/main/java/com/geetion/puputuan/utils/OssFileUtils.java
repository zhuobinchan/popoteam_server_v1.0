package com.geetion.puputuan.utils;

import com.geetion.generic.serverfile.model.File;
import com.geetion.generic.serverfile.oss.FileManager;
import com.geetion.generic.serverfile.oss.OssOption;
import com.geetion.generic.serverfile.oss.OssPermissionManager;
import com.geetion.generic.serverfile.service.FileService;
import com.geetion.puputuan.application.Application;
import com.geetion.puputuan.common.enums.ResultCode;
import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.GroupMember;
import com.geetion.puputuan.model.Photo;
import com.geetion.puputuan.model.User;
import com.geetion.puputuan.pojo.ConfigInfo;
import com.geetion.puputuan.pojo.GroupWithNumberList;
import com.geetion.puputuan.pojo.PhotoWithLikeCount;
import com.geetion.puputuan.pojo.UserWithCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jian on 2016/4/26.
 */
@Component
public class OssFileUtils {


    @Resource(name = "geetionFileService")
    private FileService fileService;
    @Resource(name = "puputuanApplication")
    private Application application;
    @Resource
    private OssPermissionManager ossPermissionManager;

    @Resource
    private FileManager fileManager;

    @Autowired
    private ConfigInfo configInfo;

    /**
     * 根据文件的id从阿里云获得图片
     *
     * @param fileId 文件id
     * @param style  图片服务的参数
     * @return 返回file文件类型，里面的url是经过阿里云解析后的图片的url
     */
    public File getPictures(Long fileId, String style) {
        if (fileId != null) {

            //原始域名
            String originalUrl = "";
            //图片服务的域名
            String imageUrl = "";
            if (!OssOption.DEFAULT_BUCKET.equals("") && !OssOption.ENDPOINT.equals("") && !OssOption.IMGENDPOINT.equals("")) {
                //原始域名
                originalUrl = "http://" + OssOption.DEFAULT_BUCKET + "." + OssOption.ENDPOINT + "/";
                //图片服务的域名
                imageUrl = "http://" + OssOption.DEFAULT_BUCKET + "." + OssOption.IMGENDPOINT + "/";
            } else {
                //如果无法获取图片服务的域名，则不添加任何的图片服务参数
                style = null;
            }
            //获取file信息
            File file = fileService.getFileById(fileId);

            String fileUrl = file.getUrl();
            //要使用图片服务，则把域名更换为图片服务的域名
            fileUrl = imageUrl + fileUrl.substring(originalUrl.length(), fileUrl.length());

            String fileName = fileUrl.substring(fileUrl.lastIndexOf("-") + 1, fileUrl.length());
            String suffix = fileUrl.substring(fileUrl.lastIndexOf(".") + 1, fileUrl.length());
//            String cutUrl = fileUrl.substring(52);
//            String url = ossPermissionManager.cipherUrl(application.getOssAccessToken().getAccessKeyId(),
//                    application.getOssAccessToken().getAccessKeySecret(),
//                    application.getOssAccessToken().getSecurityToken(),
//                    OssOption.DEFAULT_BUCKET,
//                    cutUrl + (style == null ? "" : "@" + style),
//                    true);
            String url = fileUrl + (style == null ? "" : "@" + style);
            File pojoFile = new File();
            pojoFile.setId(file.getId());
            pojoFile.setUrl(url);
            pojoFile.setSuffix(suffix);
            pojoFile.setName(fileName);
            pojoFile.setSize(file.getSize());
            pojoFile.setCreateTime(file.getCreateTime());

            return pojoFile;
        }
        return null;
    }


    /**
     * 根据文件的id从阿里云获得图片的路径
     *
     * @param fileId 文件id
     * @param style  图片服务的参数
     * @return 返回url是经过阿里云解析后的图片的url
     */
    public String getPicturesUrl(Long fileId, String style) {
        if (fileId != null) {
            //原始域名
            String originalUrl = "";
            //图片服务的域名
            String imageUrl = "";
            if (!OssOption.DEFAULT_BUCKET.equals("") && !OssOption.ENDPOINT.equals("") && !OssOption.IMGENDPOINT.equals("")) {
                //原始域名
                originalUrl = "http://" + OssOption.DEFAULT_BUCKET + "." + OssOption.ENDPOINT + "/";
                //图片服务的域名
                imageUrl = "http://" + OssOption.DEFAULT_BUCKET + "." + OssOption.IMGENDPOINT + "/";
            } else {
                //如果无法获取图片服务的域名，则不添加任何的图片服务参数
                style = null;
            }
            //获取file信息
            File file = fileService.getFileById(fileId);
            String fileUrl = file.getUrl();
            //要使用图片服务，则把域名更换为图片服务的域名
            fileUrl = imageUrl + fileUrl.substring(originalUrl.length(), fileUrl.length());
//            String cutUrl = fileUrl.substring(52);
//            return ossPermissionManager.cipherUrl(application.getOssAccessToken().getAccessKeyId(),
//                    application.getOssAccessToken().getAccessKeySecret(),
//                    application.getOssAccessToken().getSecurityToken(),
//                    OssOption.DEFAULT_BUCKET,
//                    cutUrl + (style == null ? "" : "@" + style),
//                    true);
            return fileUrl + (style == null ? "" : "@" + style);
        }
        return null;
    }


    /**
     * 获取单个用户头像
     */
    public User getUserHead(User user, String style) {
        if (user != null) {
            user.setHead(getPictures(user.getHeadId(), style));
        }
        return user;
    }

    /**
     * 批量获取用户头像 -- 不分页
     */
    public List<User> getUserHeadList(List<User> list, String style) {
        if (list != null) {
            for (User user : list) {
                user.setHead(getPictures(user.getHeadId(), style));
            }
        }
        return list;
    }

    /**
     * 批量获取用户头像 -- 分页
     */
    public PagingResult<User> getUserHeadPage(PagingResult<User> pagingForKeyword, String style) {
        if (pagingForKeyword != null && pagingForKeyword.getResultList() != null) {
            for (int i = 0; i < pagingForKeyword.getResultList().size(); i++) {
                pagingForKeyword.getResultList().get(i).setHead(getPictures(pagingForKeyword.getResultList().get(i).getHeadId(), style));
            }
        }
        return pagingForKeyword;
    }

    /**
     * 批量获取群聊成员头像 -- GroupMember
     */
    public List<GroupMember> getUserHeadGroupMemberList(List<GroupMember> list, String style) {
        if (list != null) {
            for (GroupMember groupMember : list) {
                if (groupMember.getUser() != null) {
                    groupMember.getUser().setHead(getPictures(groupMember.getUser().getHeadId(), style));
                }
            }
        }
        return list;
    }


    /**
     * 获取相册图片
     */
    public Photo getPhoto(Photo photo, String style) {
        if (photo != null) {
            photo.setImage(getPictures(photo.getImageId(), style));
        }
        return photo;
    }

    /**
     * 批量获取相册图片 -- Photo -- 不分页
     */
    public List<Photo> getPhotoList(List<Photo> list, String style) {
        if (list != null) {
            for (Photo photo : list) {
                photo.setImage(getPictures(photo.getImageId(), style));
            }
        }
        return list;
    }

    /**
     * 批量获取用户头像 -- Photo -- 分页
     */
    public PagingResult<Photo> getPhotoPage(PagingResult<Photo> pagingForKeyword, String style) {
        if (pagingForKeyword != null && pagingForKeyword.getResultList() != null) {
            for (int i = 0; i < pagingForKeyword.getResultList().size(); i++) {
                pagingForKeyword.getResultList().get(i).setImage(getPictures(pagingForKeyword.getResultList().get(i).getImageId(), style));
            }
        }
        return pagingForKeyword;
    }


    /**
     * 获取单个用户头像
     */
    public UserWithCount getUserWithCountHead(UserWithCount user, String style) {
        if (user != null) {
            user.setHead(getPictures(user.getHeadId(), style));
        }
        return user;
    }

    /**
     * 批量获取用户头像 -- UserWithCount -- 不分页
     */
    public List<UserWithCount> getUserWithCountHeadList(List<UserWithCount> list, String style) {
        if (list != null) {
            for (User user : list) {
                user.setHead(getPictures(user.getHeadId(), style));
            }
        }
        return list;
    }

    /**
     * 批量获取用户头像 -- UserWithCount -- 分页
     */
    public PagingResult<UserWithCount> getUserWithCountHeadPage(PagingResult<UserWithCount> pagingForKeyword, String style) {
        if (pagingForKeyword != null && pagingForKeyword.getResultList() != null) {
            for (int i = 0; i < pagingForKeyword.getResultList().size(); i++) {
                pagingForKeyword.getResultList().get(i).setHead(getPictures(pagingForKeyword.getResultList().get(i).getHeadId(), style));
            }
        }
        return pagingForKeyword;
    }


    /**
     * 批量获取相册图片 -- PhotoWithLikeCount -- 不分页
     */
    public List<PhotoWithLikeCount> getPhotoWithLikeCountImageList(List<PhotoWithLikeCount> list, String style) {
        if (list != null) {
            for (PhotoWithLikeCount photoWithLikeCount : list) {
                photoWithLikeCount.setImage(getPictures(photoWithLikeCount.getImageId(), style));
            }
        }
        return list;
    }

    /**
     * 批量获取相册图片 -- PhotoWithLikeCount -- 分页
     */
    public PagingResult<PhotoWithLikeCount> getPhotoWithLikeCountImagePage(PagingResult<PhotoWithLikeCount> pagingForKeyword, String style) {
        if (pagingForKeyword != null && pagingForKeyword.getResultList() != null) {
            for (int i = 0; i < pagingForKeyword.getResultList().size(); i++) {
                pagingForKeyword.getResultList().get(i).setImage(getPictures(pagingForKeyword.getResultList().get(i).getImageId(), style));
            }
        }
        return pagingForKeyword;
    }


    /**
     * 批量设置群成员头像 -- GroupWithMemberList -- 不分页
     */
    public GroupWithNumberList getGroupWithMemberImageList(GroupWithNumberList groupWithNumberList, List<GroupMember> list, String style) {
        if (list != null && groupWithNumberList != null) {
            List<File> fileList = new ArrayList<>();
            for (GroupMember groupMember : list) {
                fileList.add(getPictures(groupMember.getUser().getHeadId(), style));
            }
            groupWithNumberList.setHeadList(fileList);
        }
        return groupWithNumberList;
    }

    /**
     * 批量设置群成员头像 -- GroupWithMemberList -- 分页
     */
    public GroupWithNumberList getGroupWithMemberImagePage(GroupWithNumberList groupWithNumberList,
                                                           PagingResult<GroupMember> pagingForKeyword, String style) {
        if (pagingForKeyword != null && pagingForKeyword.getResultList() != null && groupWithNumberList != null) {
            List<File> fileList = new ArrayList<>();
            for (GroupMember groupMember :  pagingForKeyword.getResultList()) {
                fileList.add(getPictures(groupMember.getUser().getHeadId(), style));
            }
            groupWithNumberList.setHeadList(fileList);
        }
        return groupWithNumberList;
    }

    /**
     * 封装上传文件接口,返回对应的File对象
     */
    public File uploadFile (Long userId, CommonsMultipartFile file, Long size) {

        String prefixUrl = "";
        if(configInfo.isServer()){
            prefixUrl = "http://bucket-puputuan.oss-cn-shenzhen.aliyuncs.com/puputuan/" + "user/" + userId + "/";
        }else {
            prefixUrl = "http://bucket-puputuan.oss-cn-shenzhen.aliyuncs.com/puputuan/" + "test/" + userId + "/";
        }

        String prefixFile = "user-";
        String domain = OssOption.DEFAULT_DOMAIN;//项目文件夹
        String folder = "";
        if(configInfo.isServer()){
            folder = OssOption.DEFAULT_USER + userId + "/";//项目子文件夹
        }else {
            folder = OssOption.DEFAULT_TEST + userId + "/";//项目子文件夹
        }


        // 转发文件到oss,获取url
        // http://bucket-geetion.oss-cn-shenzhen.aliyuncs.com/testfile/ons_open-api.pdf
        String fileName = prefixFile + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        boolean isSuccess = fileManager.uploadFile(domain, folder, file, fileName);
        if (!isSuccess) {
            return null;
        }

        //上传OSS成功,塞进数据库
        String url = prefixUrl + fileName;
        File pojoFile = new File();
        pojoFile.setUrl(url);
        pojoFile.setSize(size);
        fileService.addFile(pojoFile);

        return pojoFile;
    }

    /**
     * 封装上传文件接口,返回对应的File对象
     */
    public File uploadAnnounceImage (CommonsMultipartFile file, Long size) {

        String prefixUrl = "";
        if(configInfo.isServer()){
            prefixUrl = "http://bucket-puputuan.oss-cn-shenzhen.aliyuncs.com/puputuan/" + "announce/";
        }else {
            prefixUrl = "http://bucket-puputuan.oss-cn-shenzhen.aliyuncs.com/puputuan/" + "announce/test/";
        }

        String prefixFile = "image-";
        String domain = OssOption.DEFAULT_DOMAIN;//项目文件夹
        String folder = "";
        if(configInfo.isServer()){
            folder = "announce" + "/";//项目子文件夹
        }else {
            folder =  "announce/test" + "/";//项目子文件夹
        }


        // 转发文件到oss,获取url
        // http://bucket-geetion.oss-cn-shenzhen.aliyuncs.com/testfile/ons_open-api.pdf
        String fileName = prefixFile + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        boolean isSuccess = fileManager.uploadFile(domain, folder, file, fileName);
        if (!isSuccess) {
            return null;
        }

        //上传OSS成功,塞进数据库
        String url = prefixUrl + fileName;
        File pojoFile = new File();
        pojoFile.setUrl(url);
        pojoFile.setSize(size);
        fileService.addFile(pojoFile);

        return pojoFile;
    }

    /**
     * chenzhuobin by 2017/3/24
     * 封装后台管理系统的文件上传文件接口
     * @param file
     * @param size
     * @param folderName 这个是上传的阿里云的文件夹名
     * @return 返回file对象
     */
    public File uploadMengerFile(CommonsMultipartFile file, Long size,String folderName){
        if (folderName==null||folderName.equals("")){
            return null;
        }
        String prefixUrl = "";
        if(configInfo.isServer()){
            prefixUrl = "http://bucket-puputuan.oss-cn-shenzhen.aliyuncs.com/puputuan/" + folderName +"/";
        }else {
            prefixUrl = "http://bucket-puputuan.oss-cn-shenzhen.aliyuncs.com/puputuan/" + folderName  +"/test/";
        }

        String prefixFile = "image-";
        String domain = OssOption.DEFAULT_DOMAIN;//项目文件夹

        String folder = "";
        if(configInfo.isServer()){
            folder = folderName + "/";//项目子文件夹
        }else {
            folder =  folderName+"/test" + "/";//项目子文件夹
        }


        // 转发文件到oss,获取url
        // http://bucket-geetion.oss-cn-shenzhen.aliyuncs.com/testfile/ons_open-api.pdf
        String fileName = prefixFile + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        boolean isSuccess = fileManager.uploadFile(domain, folder, file, fileName);
        if (!isSuccess) {
            return null;
        }

        //上传OSS成功,塞进数据库
        String url = prefixUrl + fileName;
        File pojoFile = new File();
        pojoFile.setUrl(url);
        pojoFile.setSize(size);
        fileService.addFile(pojoFile);

        return pojoFile;
    }

    /**
     * 重载该函数，可以修改文件的名字的名字
     * @param file
     * @param size
     * @param folderName
     * @param fileName
     * @return
     */

    public File uploadMengerFile(CommonsMultipartFile file, Long size,String folderName,String fileName){
        if (folderName==null||folderName.equals("")||fileName==null||fileName.equals("")){
            return null;
        }
        String prefixUrl = "";
        if(configInfo.isServer()){
            prefixUrl = "http://bucket-puputuan.oss-cn-shenzhen.aliyuncs.com/puputuan/" + folderName +"/";
        }else {
            prefixUrl = "http://bucket-puputuan.oss-cn-shenzhen.aliyuncs.com/puputuan/" + folderName  +"/test/";
        }

        String domain = OssOption.DEFAULT_DOMAIN;//项目文件夹

        String folder = "";
        if(configInfo.isServer()){
            folder = folderName + "/";//项目子文件夹
        }else {
            folder =  folderName+"/test" + "/";//项目子文件夹
        }
        // 转发文件到oss,获取url
        // http://bucket-geetion.oss-cn-shenzhen.aliyuncs.com/testfile/ons_open-api.pdf
        boolean isSuccess = fileManager.uploadFile(domain, folder, file, fileName);
        if (!isSuccess) {
            return null;
        }

        //上传OSS成功,塞进数据库
        String url = prefixUrl + fileName;
        File pojoFile = new File();
        pojoFile.setUrl(url);
        pojoFile.setSize(size);
        fileService.addFile(pojoFile);

        return pojoFile;
    }



    /**
     * 删除上传到OSS中的文件,同时删除file表中的记录
     */
    public void deleteFile (Long userId, File file) {
        if (null == userId || null == file) {
            return;
        }

        String domain = OssOption.DEFAULT_DOMAIN;//项目文件夹
        String folder = OssOption.DEFAULT_USER + userId + "/";//项目子文件夹
        String fileUrl = file.getUrl();
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.length());
        fileManager.deleteFile(OssOption.DEFAULT_BUCKET, domain + folder, fileName);

        fileService.removeFile(file.getId());
    }

    /**
     *
     * @param fileName 对应的文件名
     * @param folderName 所存放的文件夹的名字
     */
    public void deleteFile(String folderName,String fileName){
        if (null == fileName||null==folderName) return;
        String domain = OssOption.DEFAULT_DOMAIN;//项目文件夹
        String folder = "";
        if(configInfo.isServer()){
            folder = folderName + "/";//项目子文件夹
        }else {
            folder =  folderName+"/test" + "/";//项目子文件夹
        }
        fileManager.deleteFile(OssOption.DEFAULT_BUCKET, domain + folder, fileName);
    }

}
