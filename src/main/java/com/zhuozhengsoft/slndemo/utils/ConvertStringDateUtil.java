package com.zhuozhengsoft.slndemo.utils;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @Author: dong
 * @Date: 2022/7/27 17:02
 * @Version 1.0
 */
public class ConvertStringDateUtil {
    public  static  String convert(Date date){
     return   (new SimpleDateFormat("yyyy-MM-dd")).format(date);
    }
}
