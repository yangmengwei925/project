package com.zhuozhengsoft.slndemo.controller;

import com.zhuozhengsoft.slndemo.entity.Doc;
import com.zhuozhengsoft.slndemo.service.DocService;
import com.zhuozhengsoft.moboffice.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.net.URLDecoder;

/**
 * @Author: dong
 * @Date: 2022/7/10 17:16
 * @Version 1.0
 */
@Controller
@RequestMapping("/mobile")
public class MobileOfficeController {

    @Value("${docpath}")
    private  String docPath;

    @Value("${moblicpath}")
    private  String moblicpath;

    @Autowired
    DocService m_docService;

    /**
     * 添加MobOffice的服务器端授权程序Servlet（必须）
     *
     */
    @RequestMapping("/opendoc")
    public void opendoc(HttpServletRequest request, HttpServletResponse response, HttpSession session,String type,String userName)throws  Exception {
        String fileName = "";
        userName= URLDecoder.decode(userName,"utf-8");

        Doc doc=m_docService.getDocById(1);
        if(type.equals("word")){
            fileName = doc.getDocName();
        }else{
            fileName = doc.getPdfName();
        }
        OpenModeType openModeType = OpenModeType.docNormalEdit;

        if (fileName.endsWith(".doc")) {
            openModeType = OpenModeType.docNormalEdit;
        } else if (fileName.endsWith(".pdf")) {
            String mode = request.getParameter("mode");
            if (mode.equals("normal")) {
                openModeType = OpenModeType.pdfNormal;
            } else {
                openModeType = OpenModeType.pdfReadOnly;
            }
        }

        MobOfficeCtrl mobCtrl = new MobOfficeCtrl(request,response);
        mobCtrl.setSysPath(moblicpath);
        mobCtrl.setServerPage("/mobserver.zz");
        //mobCtrl.setZoomSealServer("http://xxx.xxx.xxx.xxx:8080/ZoomSealEnt/enserver.zz");
        mobCtrl.setSaveFilePage("/mobile/savedoc?testid="+Math.random());
        mobCtrl.webOpen("file://"+docPath+fileName,  openModeType , userName);
    }

    @RequestMapping("/savedoc")
    public  void  savedoc(HttpServletRequest request,  HttpServletResponse response){
        FileSaver fs = new FileSaver(request, response);
        fs.saveToFile(docPath+fs.getFileName());
        fs.close();
    }
}

