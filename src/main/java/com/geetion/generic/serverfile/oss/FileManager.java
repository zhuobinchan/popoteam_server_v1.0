package com.geetion.generic.serverfile.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.geetion.generic.serverfile.utils.FileUtil;
import com.geetion.generic.serverfile.utils.PathKit;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beary on 16/1/6.
 */
@Component
public class FileManager {

    /**
     * 列出对应名字bucket下所有的文件
     * 默认情况下，如果存储空间中的文件数量大于100，则只会返回100个文件， 且返回结果中 IsTruncated 为 true，
     * 并返回 NextMarker 作为下此读取的起点。
     * 若想增大返回文件数目，可以修改MaxKeys参数，或者使用Marker参数分次读取。
     *
     * @param bucket
     * @param prefix
     * @return
     */
    public List<OSSObjectSummary> getAllfilesInBucket(String bucket, String prefix) {
        OssManager manager = new OssManager();
        OSSClient client = manager.getOssClient();
        // 获取指定bucket下的所有Object信息
        ObjectListing listing = null;
        if (prefix != null && !prefix.isEmpty()) {
            listing = client.listObjects(bucket, prefix);
        } else {
            listing = client.listObjects(bucket);
        }
        List<OSSObjectSummary> result = new ArrayList<>();
        //不足或者达到100个,先取前100个加入结果集
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            result.add(objectSummary);
        }
        while (listing.isTruncated()) {
            String nextMark = listing.getNextMarker();
            listing.setMarker(nextMark);
            for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
                result.add(objectSummary);
            }
        }
        manager.closeClient();
        return result;
    }


    //====================================文件上下传================================================

    /**
     * 文件上传中转
     *
     * @param domain
     * @param folder
     * @param file
     * @param fileName
     * @return
     */
    public boolean uploadFile(String domain, String folder, MultipartFile file, String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("you must name the file");
        }
        //先缓存文件，然后上传，如果上传成功，则删除缓存
        File tempFile = FileUtil.saveFile(file, fileName);
        //   System.out.println("\n\nprefix:  " + domain + folder + fileName +"  "+OssOption.DEFAULT_BUCKET+"\n\n");
        try {
            uploadFile(OssOption.DEFAULT_BUCKET, domain + folder, fileName, tempFile);
            tempFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 文件上传
     *
     * @param bucket
     * @param prefix
     * @param fileName
     * @param file
     */
    public void uploadFile(String bucket, String prefix, String fileName, File file) {
        if (fileName == null) {
            throw new IllegalArgumentException("you must name the file");
        }
        OssManager manager = new OssManager();
        OSSClient client = manager.getOssClient();
        client.putObject(bucket, prefix + fileName, file);
        manager.closeClient();
    }

    /**
     * 文件上传,可以输入二进制数据
     *
     * @param bucket
     * @param prefix
     * @param fileName
     * @param inputbytes
     */
    public void uploadFile(String bucket, String prefix, String fileName, byte[] inputbytes) {
        if (fileName == null) {
            throw new IllegalArgumentException("you must name the file");
        }
        OssManager manager = new OssManager();
        OSSClient client = manager.getOssClient();
        client.putObject(bucket, prefix + fileName, new ByteArrayInputStream(inputbytes));
        manager.closeClient();
    }

    /**
     * 创建文件夹
     *
     * @param folderName
     */
    public void createFolder(String folderName) {
        if (folderName == null) {
            throw new IllegalArgumentException("you must name the folder");
        }
        OssManager manager = new OssManager();
        OSSClient client = manager.getOssClient();
        client.putObject(OssOption.DEFAULT_BUCKET, OssOption.DEFAULT_DOMAIN + folderName + "/", new ByteArrayInputStream(new byte[0]));
        System.out.println("创建文件夹: " + OssOption.DEFAULT_DOMAIN + folderName + "/");
        manager.closeClient();
    }


    /**
     * 文件下载部分提供应用服务器先下载文件到本地
     * 然后做二次处理,再通过接口返回到各端,文件管理系统不做接口处理
     */
    public void downloadFile(String bucket, String prefix, String fileName) {
        OssManager manager = new OssManager();
        OSSClient client = manager.getOssClient();
        // 获取Object，返回结果为OSSObject对象
        OSSObject object = client.getObject(bucket, prefix + fileName);
        // 获取Object的输入流
        InputStream objectContent = object.getObjectContent();
        File uploadFile = new File(PathKit.getWebRootPath() + "/upload");
        if (!uploadFile.exists()) {//检查upload文件夹是否存在
            uploadFile.mkdir();
        }

        File f2 = new File(uploadFile, fileName);
        try {
            OutputStream os = new FileOutputStream(f2);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = objectContent.read(buffer, 0, 8192)) != -1) {

                os.write(buffer, 0, bytesRead);

            }
            os.close();
            objectContent.close();
            manager.closeClient();
        } catch (IOException e) {
            manager.closeClient();
            e.printStackTrace();
        }
    }

    //====================================文件操作================================================

    /**
     * 更改文件权限
     * 设置Object ACL注意事项：
     * 如果没有设置Object的权限，即Object的ACL为default，Object的权限和Bucket权限一致。
     * 如果设置了Object的权限，Object的权限大于Bucket权限。举个例子，如果设置了Object的权限是public-read，
     * 无论Bucket是什么权限，该Object都可以被身份验证访问和匿名访问。
     *
     * @param bucket
     * @param prefix
     * @param fileName
     * @param chmod
     */
    public void chmodFile(String bucket, String prefix, String fileName, int chmod) {
        OssManager manager = new OssManager();
        OSSClient client = manager.getOssClient();
        switch (chmod) {
            case OssOption.CONTROL_PRIVATE:
                client.setObjectAcl(bucket, prefix + fileName, OssOption.ACLS[0]);
                break;
            case OssOption.CONTROL_PUBLICREAD:
                client.setObjectAcl(bucket, prefix + fileName, OssOption.ACLS[1]);
                break;
            case OssOption.CONTROL_PUBLICREAD_WRITE:
                client.setObjectAcl(bucket, prefix + fileName, OssOption.ACLS[2]);
                break;
            case OssOption.CONTROL_DEFAULT:
                client.setObjectAcl(bucket, prefix + fileName, OssOption.ACLS[3]);
                break;
        }
        manager.closeClient();
    }

    /**
     * 获取文件的当前权限
     *
     * @param bucket
     * @param prefix
     * @param fileName
     * @return
     */
    public String getFileMod(String bucket, String prefix, String fileName) {
        OssManager manager = new OssManager();
        OSSClient client = manager.getOssClient();
        ObjectAcl returnedAcl = client.getObjectAcl(bucket, prefix + fileName);
        manager.closeClient();
        return returnedAcl.getPermission().toString();
    }


    /**
     * 删除文件
     *
     * @param bucket
     * @param prefix
     * @param fileName
     */
    public void deleteFile(String bucket, String prefix, String fileName) {
        OssManager manager = new OssManager();
        OSSClient client = manager.getOssClient();
        client.deleteObject(bucket, prefix + fileName);
        manager.closeClient();
    }

    /**
     * 批量删除文件
     *
     * @param bucket
     * @param keys
     * @return
     */
    public List<String> deleteFiles(String bucket, List<String> keys) {
        OssManager manager = new OssManager();
        OSSClient client = manager.getOssClient();
        DeleteObjectsResult deleteObjectsResult = client.deleteObjects(
                new DeleteObjectsRequest(bucket).withKeys(keys));
        List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
        manager.closeClient();
        return deletedObjects;
    }

}
