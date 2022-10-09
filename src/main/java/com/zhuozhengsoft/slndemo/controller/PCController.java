package com.zhuozhengsoft.slndemo.controller;

import com.zhuozhengsoft.slndemo.entity.Doc;
import com.zhuozhengsoft.slndemo.service.DocService;
import com.zhuozhengsoft.slndemo.utils.CopyFileUtil;
import com.zhuozhengsoft.slndemo.utils.ConvertStringDateUtil;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;
import  java.util.Arrays;
import java.util.Date;
/**
 * @Author: dong
 * @Date: 2022/7/23 11:56
 * @Version 1.0
 */
@Controller
@RequestMapping("/pc")
public class PCController {
    @Value("${docpath}")
    private String docPath;

    @Value("${dbpath}")
    private String dbPath;

    @Autowired DocService m_docService;

    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpSession  session,String userName)throws  Exception {
        request.setAttribute("userName",userName);
        return "/pc/login";
    }

    @RequestMapping("/logout")
    public void logout(HttpServletRequest request,HttpServletResponse response,HttpSession  session)throws  Exception {
        //清除session
        session.setAttribute("userName",null);
        response.sendRedirect(request.getContextPath() + "/pc/login");
    }

    @RequestMapping("/loginAction")
    public void loginAction(HttpServletRequest request, HttpServletResponse response,HttpSession  session,String userName,String password)throws  Exception {
        String  msg="";
        if(userName==""|| userName==null||password==""|| password==null){
            msg="用户名或者密码为空!";
            request.setAttribute("msg",msg);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
        if(("张三".equals(userName)&&"123456".equals(password))||("王总".equals(userName)&&"123456".equals(password))||("赵六".equals(userName)&&"123456".equals(password))||("李总".equals(userName)&&"123456".equals(password))){
            //如果登录用户是张三，则说明是普通文员，有起草文件的权限，如果是王总或者李总，则具有审批文档和盖章等功能的权限，如果是赵六，则是核稿人员
            session.setAttribute("userName",userName);
            request.getRequestDispatcher("/pc/index").forward(request, response);

        }else{
            msg="用户名或者密码错误";
            request.setAttribute("msg",msg);
            request.getRequestDispatcher("/pc/login").forward(request, response);
        }

    }

    @RequestMapping("/index")
    public String index(HttpServletRequest request, HttpServletResponse response,HttpSession  session)throws  Exception {
        //查询数据库，获取当前doc表中的status给index页面加上待办图标
        String  open=request.getParameter("open");//判断是不是起草页面跳转过来的
        request.setAttribute("open",open);

        Doc doc=m_docService.getDocById(1);
        request.setAttribute("doc",doc);

        return "/pc/index";
    }

    @RequestMapping("/info")
    public String info(HttpServletRequest request)throws  Exception {
        Doc doc=m_docService.getDocById(1);
        //info页面需要的step
        String status=doc.getStatus();
        if(status==""){
            status="起草";
        }
        List<String> statusList = Arrays.asList("起草","批阅","核稿","盖章","发文");
        Integer step = statusList.indexOf(status)+1;
        request.setAttribute("step",step);
        return "/pc/info";
    }

    /**
     * 修改密码
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/pass")
    public String pass(HttpServletRequest request)throws  Exception {
        return "/pc/pass";
    }

    @RequestMapping("/showAddUi")
    public String showAddUi(HttpServletRequest request)throws  Exception {
        boolean hasDoc = false;//标识数据库是否已经生成的记录，如果生成了则不能再起草文件
        Doc doc=m_docService.getDocById(1);
        if(doc.getId()!=0){
               hasDoc=true;
        }
        Date date = new Date();
        String nowDate = (new SimpleDateFormat("yyyy-MM-dd")).format(date);
        request.setAttribute("nowDate",nowDate);
        request.setAttribute("hasDoc",hasDoc);
        return "/pc/add";
    }

    @RequestMapping(value = "/addDoc",produces = "text/html;charset=utf-8")
    @ResponseBody
    public  String  addDoc(HttpServletRequest request,HttpServletResponse response,Doc  doc)throws  Exception {
        String  strFlg="";
        doc.setId(1);
        doc.setStatus("起草");
        int id=m_docService.addDoc(doc);//向数据库中插入一条数据
        if(id<=0){
            new Exception("插入数据失败！");
            strFlg= "<script type=\"text/javascript\">alert('起草失败，插入数据失败！')</script>";
            return  strFlg;
        }
        //拷贝文件
        //将此模板复制一份，重命名成doc001.docx
        String sepa = java.io.File.separator;
        String templatePath = docPath  + sepa + doc.getTemplateName();
        String filePath = docPath  + sepa+ "doc001.docx";
        boolean result = CopyFileUtil.copyFile(templatePath, filePath);
        if(!result){
            new Exception("拷贝文件失败！");
            strFlg="<script type=\"text/javascript\">alert('起草失败，拷贝文件失败！')</script>";
            return  strFlg;
        }
        //起草成功
        strFlg="<script type=\"text/javascript\"> window.parent.reloadDocList(); </script>";
        return  strFlg;
    }

    @RequestMapping("/showDocListUi")
    public String showDocListUi(HttpServletRequest request,String flg)throws  Exception {
        //根据不同的flg标识打开不同的list页面
        String listUrl="";
        if(flg.equals("edit")){
            listUrl="editDocList";
        }
        if(flg.equals("piyue")){
            listUrl="piyueDocList";
        }
        if(flg.equals("hegao")){
            listUrl="hegaoDocList";
        }
        if(flg.equals("insertSeal")){
            listUrl="insertSealDocList";
        }
        if(flg.equals("fawen")){
            listUrl="fawenDocList";
        }
        Doc doc=m_docService.getDocById(1);//查询数据库记录
        if(doc.getId()==1){
        String nowDate =ConvertStringDateUtil.convert(doc.getIssueDate());
        request.setAttribute("nowDate",nowDate);
        }
        request.setAttribute("doc",doc);
        return "pc/"+listUrl;
    }

    /**
     * 获取数据库记录中文档的名称，从而判断是否要更新数据库status
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/getDocInfo")
    @ResponseBody
    public String getDocInfo(HttpServletRequest request,HttpServletResponse response)throws  Exception {
        String pdfName="";
        String docName="";
        Doc doc=m_docService.getDocById(1);
        if(doc.getId()!=0){
            pdfName =doc.getPdfName();
            docName=doc.getDocName();
        }
       return "docName:"+docName+";pdfName:"+pdfName;
    }

    @RequestMapping("/setDocStatus")
    @ResponseBody
    public boolean setDocStatus(HttpServletRequest request,HttpServletResponse response,String status)throws  Exception {
        boolean result=false;//标志是否更新成功
        Doc doc=new Doc();
        doc.setStatus(status);
        doc.setId(1);
        int id=m_docService.updateStatusForDocById(doc);
        if(id!=0){
            //更新成功
            result=true;
        }
        return result;

    }

    /**
     * 复位demo，清除相关文件和重新拷贝数据库文件
     */
    @RequestMapping("/restart")
    @ResponseBody
    public boolean restart(HttpServletRequest request,HttpServletResponse response)throws Exception{
        boolean isSuccess=false;
        try{
            //复位功能：拷贝备份的slndata.db覆盖现有的文件，删除已经生成doc001.docx和doc001.pdf
            String sepa = java.io.File.separator;
            String dbfileBakPath = dbPath  +"slndata_bak.db";
            String dbfilePath =dbPath+"slndata.db";
            CopyFileUtil.copyFile(dbfileBakPath, dbfilePath);

            File docfile = new File(docPath +"doc001.docx");
            File pdffile = new File(docPath +"doc001.pdf");
            if(docfile.exists()){
                docfile.delete();
            }
            if(pdffile.exists()){
                pdffile.delete();
            }
            isSuccess=true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return  isSuccess;
    }

}
