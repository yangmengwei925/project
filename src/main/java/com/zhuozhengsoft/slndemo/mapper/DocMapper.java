package com.zhuozhengsoft.slndemo.mapper;

import com.zhuozhengsoft.slndemo.entity.Doc;

/**
 * @Author: dong
 * @Date: 2022/7/23 15:27
 * @Version 1.0
 */
public interface DocMapper  {
     Doc getDocById(int id)throws  Exception;
     Integer addDoc(Doc doc)throws  Exception;
    //更新数据库doc表的status
    Integer updateStatusForDocById(Doc doc)throws  Exception;
    //更新数据库doc表的docName
    Integer updateDocNameForDocById(Doc doc)throws  Exception;
    //更新数据库doc表的pdfName
    Integer updatePdfNameForDocById(Doc doc)throws  Exception;

}
