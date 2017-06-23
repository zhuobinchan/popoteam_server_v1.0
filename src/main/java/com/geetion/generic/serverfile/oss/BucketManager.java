package com.geetion.generic.serverfile.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Created by Beary on 16/1/5.
 */
@Component
public class BucketManager {


    /**
     * 创建Bucket
     * 由于Bucket的名字是全局唯一的，所以必须保证您的Bucket名称不与别人重复。
     *
     * @param bucketName
     */
    public void createBucket(String bucketName) {
        OssManager manager = new OssManager();
        OSSClient client = manager.getOssClient();
        client.createBucket(bucketName);
        manager.closeClient();
    }

    /**
     * 列出所有bucket
     *
     * @return
     */
    public List<Bucket> listBucket() {
        OssManager manager = new OssManager();
        OSSClient client = manager.getOssClient();
        List<Bucket> result = client.listBuckets();
        manager.closeClient();
        return result;
    }

    /**
     * 判断bucket是否存在
     *
     * @param bucket
     * @return
     */
    public boolean isBucketExist(String bucket) {
        OssManager manager = new OssManager();
        OSSClient client = manager.getOssClient();
        boolean exists = client.doesBucketExist(bucket);
        manager.closeClient();
        return exists;
    }

    /**
     * 更改bucket权限
     *
     * @param bucket
     * @param chmod
     */
    public void chmodBucket(String bucket, int chmod) {
        OssManager manager = new OssManager();
        OSSClient client = manager.getOssClient();
        switch (chmod) {
            case OssOption.CONTROL_PRIVATE:
                client.setBucketAcl(bucket, OssOption.ACLS[0]);
                break;
            case OssOption.CONTROL_PUBLICREAD:
                client.setBucketAcl(bucket, OssOption.ACLS[1]);
                break;
            case OssOption.CONTROL_PUBLICREAD_WRITE:
                client.setBucketAcl(bucket, OssOption.ACLS[2]);
                break;
            case OssOption.CONTROL_DEFAULT:
                client.setBucketAcl(bucket, OssOption.ACLS[3]);
                break;
        }
        manager.closeClient();
    }



}
