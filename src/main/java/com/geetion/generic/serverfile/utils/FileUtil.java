package com.geetion.generic.serverfile.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by Beary on 16/1/6.
 */
@Component
public class FileUtil {

    /**
     * 保存文件到系统下upload目录内
     *
     * @param file
     * @param fileName
     */
    public static File saveFile(MultipartFile file, String fileName) {
        File uploadFile = new File(PathKit.getWebRootPath() + "/upload");
        if (!uploadFile.exists()) {//检查upload文件夹是否存在
            uploadFile.mkdir();
        }
//        File newFie = new File(PathKit.getWebRootPath() + "/upload/" + System.currentTimeMillis());
//        if (!newFie.exists()) {
//            newFie.mkdir();
//        }

        File tempFile = new File(uploadFile + File.separator + fileName);
        try {
            file.transferTo(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile.isFile() ? tempFile : null;
    }
}
