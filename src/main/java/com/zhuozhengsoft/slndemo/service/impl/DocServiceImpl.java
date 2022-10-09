package com.zhuozhengsoft.slndemo.service.impl;

import com.zhuozhengsoft.slndemo.entity.Doc;
import com.zhuozhengsoft.slndemo.mapper.DocMapper;
import com.zhuozhengsoft.slndemo.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: dong
 * @Date: 2022/7/23 15:25
 * @Version 1.0
 */
@Service
public class DocServiceImpl implements DocService {
    @Autowired
    DocMapper docMapper;
    @Override
    public Doc getDocById(int id) throws Exception {
        Doc  doc=docMapper.getDocById(id);
        //如果doc为null的话，页面所有doc.属性都报错
        if(doc==null) {
            doc=new Doc();
        }
        return doc;
    }

    @Override
    public Integer addDoc(Doc doc) throws Exception {
       int id=docMapper.addDoc(doc);
        return id;
    }

    @Override
    public Integer updateStatusForDocById(Doc doc) throws Exception {
        int id=docMapper.updateStatusForDocById(doc);
        return id;
    }

    @Override
    public Integer updateDocNameForDocById(Doc doc) throws Exception {
        int id=docMapper.updateDocNameForDocById(doc);
        return id;
    }

    @Override
    public Integer updatePdfNameForDocById(Doc doc) throws Exception {
        int id=docMapper.updatePdfNameForDocById(doc);
        return id;
    }
}
