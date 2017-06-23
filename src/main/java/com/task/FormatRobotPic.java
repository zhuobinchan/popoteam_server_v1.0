package com.task;

import org.apache.poi.hssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于生成机器人数据
 * Created by guodikai on 2016/10/18.
 */
public class FormatRobotPic {
    public static void main(String[] args){
        String path="C:\\robot";
        File file=new File(path);
        File[] tempList = file.listFiles();
        Map<String, String> map = new HashMap<>();

        for(File picFile : tempList){
            File[] listFiles = new File(picFile.getAbsolutePath()).listFiles();
            StringBuffer sb = new StringBuffer();
//            System.out.println(picFile.getName());
//            System.out.println(listFiles);
            if(null != picFile && picFile.isDirectory()){
                for(File file1 : listFiles){
                    sb.append(file1.getName()).append(",");
                }
                System.out.println(picFile.getName());
                System.out.println(sb.substring(0, sb.length() - 1).toString());
                map.put(picFile.getName(), sb.substring(0, sb.length() - 1));
            }
        }
        try {
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File("C:\\robot\\ppt_robot.xls")));
            HSSFSheet sheet = wb.getSheetAt(0);
            System.out.println(sheet.getLastRowNum());
            for(int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++){
                HSSFRow row = sheet.getRow(i);
                HSSFCell cell0 = row.createCell(9);

                if(null != row.getCell(8)){
                    String picUrl = row.getCell(8).getStringCellValue();
                    if(null != map.get(picUrl)){
                        cell0.setCellValue(new HSSFRichTextString(map.get(picUrl)));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(map.toString());
    }
}
