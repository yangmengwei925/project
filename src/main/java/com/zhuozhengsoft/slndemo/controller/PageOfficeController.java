package com.zhuozhengsoft.slndemo.controller;

import com.zhuozhengsoft.pageoffice.*;
import com.zhuozhengsoft.pageoffice.wordwriter.*;
import com.zhuozhengsoft.slndemo.entity.Doc;
import com.zhuozhengsoft.slndemo.service.DocService;
import com.zhuozhengsoft.slndemo.utils.ConvertStringDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: dong
 * @Date: 2022/7/27 15:51
 * @Version 1.0
 */
@Controller
@RequestMapping("/pc")
public class PageOfficeController {
    @Autowired
    DocService m_docService;

    @Value("${docpath}")
    private  String docPath;

    /**
     * 添加PageOffice的服务器端授权程序Servlet（必须）
     * @return
     */

    /**
     * 文件起草，编辑文件
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/wordEdit")
    public String wordEdit(HttpServletRequest request)throws  Exception{
        Doc doc= m_docService.getDocById(1);
        WordDocument wordDocument = new WordDocument();
        //打开数据区域,并赋值
        DataRegion fileNum = wordDocument.openDataRegion("PO_DocNo");//发文号
        fileNum.setValue(doc.getDocNo());

        DataRegion dept = wordDocument.openDataRegion("PO_IssueDept");//部门
        dept.setValue(doc.getIssueDept());

        DataRegion date = wordDocument.openDataRegion("PO_IssueDate");//日期
        date.setValue(ConvertStringDateUtil.convert(doc.getIssueDate()));

        DataRegion theme = wordDocument.openDataRegion("PO_TopicWords");//主题词
        theme.setValue(doc.getTopicWords());

        DataRegion year =wordDocument.openDataRegion("PO_Year");//年
        year.setValue(ConvertStringDateUtil.convert(doc.getIssueDate()).substring(0, 4));

        DataRegion title = wordDocument.openDataRegion("PO_Title");//标题
        title.setValue(doc.getTitle());

        DataRegion copies = wordDocument.openDataRegion("PO_Copies");//份数
        copies.setValue(String.valueOf(doc.getCopies()));
        String  id=request.getParameter("id");
        String name=request.getParameter("name");
        PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
        poCtrl1.setCaption("XX办公系统");
        //添加保存文件按钮
        poCtrl1.addCustomToolButton("保存","Save()",1);
        poCtrl1.setServerPage(request.getContextPath() + "/poserver.zz"); //此行必须
        poCtrl1.setWriter(wordDocument);
        poCtrl1.setSaveFilePage("/pc/saveFile");
        //打开Word文件
        poCtrl1.webOpen("file://"+docPath+"doc001.docx", OpenModeType.docNormalEdit, "张三");
        request.setAttribute("pageoffice",poCtrl1.getHtmlCode("PageOfficeCtrl1"));
        return  "/pc/wordEdit";
    }


    /**
     * 领导审阅文件，文件留痕
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/wordRevision")
    public String wordRevision(HttpServletRequest request)throws  Exception{
        String userName = request.getSession().getAttribute("userName").toString();
        //审批环节，以强制留痕模式打开文件
        PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
        poCtrl1.setCaption("XX办公系统");
        //添加保存文件按钮
        poCtrl1.addCustomToolButton("保存","Save()",1);
        poCtrl1.setServerPage(request.getContextPath() + "/poserver.zz"); //此行必须
        poCtrl1.setSaveFilePage("/pc/saveFile");
        //打开Word文件
        poCtrl1.webOpen("file://"+docPath+"doc001.docx", OpenModeType.docRevisionOnly,userName);
        request.setAttribute("pageoffice",poCtrl1.getHtmlCode("PageOfficeCtrl1"));
        return  "/pc/wordRevision";
    }

    /**
     * 核稿文件
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/wordAcceptRevision")
    public String wordAcceptRevision(HttpServletRequest request)throws  Exception{
        String userName = request.getSession().getAttribute("userName").toString();
       //核稿，以核稿模式打开文件
        PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
        poCtrl1.setCaption("XX办公系统");
        poCtrl1.setJsFunction_AfterDocumentOpened("AfterDocumentOpened()");
        //添加保存文件按钮
        poCtrl1.addCustomToolButton("保存","Save()",1);
        poCtrl1.addCustomToolButton("接受所有修订","accept()",5);
        poCtrl1.setServerPage(request.getContextPath() + "/poserver.zz"); //此行必须
        poCtrl1.setSaveFilePage("/pc/saveFile");
        //打开Word文件
        poCtrl1.webOpen("file://"+docPath+"doc001.docx", OpenModeType.docAdmin, userName);
        request.setAttribute("pageoffice",poCtrl1.getHtmlCode("PageOfficeCtrl1"));
        return  "/pc/wordAcceptRevision";
    }

    /**
     * 领导盖章
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/pdfAddSeal")
    public String pdfAddSeal(HttpServletRequest request)throws  Exception{
        String userName = request.getSession().getAttribute("userName").toString();
        if("李总".equals(userName)){
            userName = "李志";
        }
        if("王总".equals(userName)){
            userName = "王恒杰";
        }
        request.setAttribute("userName", userName);

        PDFCtrl pdfCtrl1 = new PDFCtrl(request);
        pdfCtrl1.setServerPage(request.getContextPath() + "/poserver.zz"); //此行必须
        pdfCtrl1.setCaption("XX办公系统");
        pdfCtrl1.setJsFunction_AfterDocumentOpened("afterDocumentOpened()");
        //设置保存页面
        pdfCtrl1.setSaveFilePage("/pc/saveFile");
        // Create custom toolbar
        pdfCtrl1.addCustomToolButton("保存", "Save()", 1);
        pdfCtrl1.addCustomToolButton("加盖印章", "InsertSeal()", 2);
        pdfCtrl1.addCustomToolButton("隐藏/显示书签", "SetBookmarks()", 0);
        pdfCtrl1.addCustomToolButton("-", "", 0);
        pdfCtrl1.addCustomToolButton("实际大小", "SetPageReal()", 16);
        pdfCtrl1.addCustomToolButton("适合页面", "SetPageFit()", 17);
        pdfCtrl1.addCustomToolButton("适合宽度", "SetPageWidth()", 18);
        pdfCtrl1.addCustomToolButton("-", "", 0);
        pdfCtrl1.addCustomToolButton("首页", "FirstPage()", 8);
        pdfCtrl1.addCustomToolButton("上一页", "PreviousPage()", 9);
        pdfCtrl1.addCustomToolButton("下一页", "NextPage()", 10);
        pdfCtrl1.addCustomToolButton("尾页", "LastPage()", 11);
        pdfCtrl1.addCustomToolButton("-", "", 0);
        pdfCtrl1.addCustomToolButton("向左旋转90度", "SetRotateLeft()", 12);
        pdfCtrl1.addCustomToolButton("向右旋转90度", "SetRotateRight()", 13);
        pdfCtrl1.webOpen("file://"+docPath+"doc001.pdf");
        request.setAttribute("pageoffice",pdfCtrl1.getHtmlCode("PDFCtrl1"));
        return  "/pc/pdfAddSeal";
    }
    /**
     * 发文，只读打开pdf文件
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/pdfReadOnly")
    public String pdfReadOnly(HttpServletRequest request)throws  Exception{
        PDFCtrl pdfCtrl1 = new PDFCtrl(request);
        pdfCtrl1.setJsFunction_AfterDocumentOpened("AfterDocumentOpened()");
        pdfCtrl1 .setCaption("XX办公系统");
        pdfCtrl1.setServerPage(request.getContextPath() + "/poserver.zz"); //此行必须
        pdfCtrl1.addCustomToolButton("打印", "PrintFile()", 6);
        pdfCtrl1.addCustomToolButton("隐藏/显示书签", "SetBookmarks()", 0);
        pdfCtrl1.addCustomToolButton("-", "", 0);
        pdfCtrl1.addCustomToolButton("实际大小", "SetPageReal()", 16);
        pdfCtrl1.addCustomToolButton("适合页面", "SetPageFit()", 17);
        pdfCtrl1.addCustomToolButton("适合宽度", "SetPageWidth()", 18);
        pdfCtrl1.addCustomToolButton("-", "", 0);
        pdfCtrl1.addCustomToolButton("首页", "FirstPage()", 8);
        pdfCtrl1.addCustomToolButton("上一页", "PreviousPage()", 9);
        pdfCtrl1.addCustomToolButton("下一页", "NextPage()", 10);
        pdfCtrl1.addCustomToolButton("尾页", "LastPage()", 11);
        pdfCtrl1.addCustomToolButton("-", "", 0);
        pdfCtrl1.addCustomToolButton("向左旋转90度", "SetRotateLeft()", 12);
        pdfCtrl1.addCustomToolButton("向右旋转90度", "SetRotateRight()", 13);
        pdfCtrl1.webOpen("file://"+docPath+"doc001.pdf");
        request.setAttribute("pageoffice",pdfCtrl1.getHtmlCode("PDFCtrl1"));
        return  "/pc/pdfReadOnly";
    }

    @RequestMapping("/saveFile")
      public  void  saveFile(HttpServletRequest request, HttpServletResponse response)throws  Exception{
          String sepa = java.io.File.separator;
          Doc doc=new  Doc();
          FileSaver fs = new FileSaver(request,response);
          if (".pdf".equals(fs.getFileExtName())) {
              //保存pdf文件并更新数据库信息
              fs.saveToFile(docPath  + sepa+ "doc001.pdf");
              //链接数据库，更新pdf名称。下面的代码只在核稿环节第一次点保存必须执行，有待优化
              doc.setId(1);
              doc.setPdfName("doc001.pdf");
              int id=m_docService.updatePdfNameForDocById(doc);
              if (id <= 0) {
                  System.out.println("更新数据库pdf文件名称失败！");
              }
          }
          if (".docx".equals(fs.getFileExtName())) {
              //起草完的文件保存成doc001.docx
              fs.saveToFile(docPath + sepa+ "doc001.docx");
              //链接数据库，更新doc名称。下面的代码只在起草环节第一次点保存必须执行，有待优化
              doc.setId(1);
              doc.setDocName("doc001.docx");
              int id=m_docService.updateDocNameForDocById(doc);
              if (id <= 0) {
                  System.out.println("更新数据库doc文件名称失败！");
              }
          }
          fs.close();
      }
}

