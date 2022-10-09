package com.zhuozhengsoft.slndemo.service;
import com.zhuozhengsoft.slndemo.entity.Doc;

/**
 * @Author: dong
 * @Date: 2022/7/23 15:15
 * @Version 1.0
 */
public interface DocService  {
    //根据id查询doc表
    public Doc getDocById(int id)throws  Exception;
    //起草文件，插入一条记录
    public Integer addDoc(Doc doc)throws  Exception;
    //更新数据库doc表的status
    public Integer updateStatusForDocById(Doc doc)throws  Exception;
    //更新数据库doc表的docName
    public Integer updateDocNameForDocById(Doc doc)throws  Exception;
    //更新数据库doc表的pdfName
    Integer updatePdfNameForDocById(Doc doc)throws  Exception;
}
