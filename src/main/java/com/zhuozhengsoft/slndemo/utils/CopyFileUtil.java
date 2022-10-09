package com.zhuozhengsoft.slndemo.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import  java.io.*;

/**
 * @Author: dong
 * @Date: 2022/7/25 10:13
 * @Version 1.0
 */
public class CopyFileUtil {
        //拷贝文件
        public static boolean copyFile(String oldPath, String newPath) throws Exception {
            boolean copyStatus=false;

            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);

                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    //System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                fs.close();
                inStream.close();
                copyStatus=true;
            }else{
                copyStatus=false;
            }
            return copyStatus;
        }

}
