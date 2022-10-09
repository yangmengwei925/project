package com.zhuozhengsoft.slndemo.controller;

import com.zhuozhengsoft.slndemo.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zhuozhengsoft.slndemo.entity.Doc;
/**
 * @Author: dong
 * @Date: 2022/7/10 14:54
 * @Version 1.0
 */
@Controller
@RequestMapping("/mobile")
public class MobileController {
    @Autowired
    DocService m_docService;

    @RequestMapping("/login")
    public String login(HttpServletRequest request)throws  Exception {
        String  ip=request.getServerName();
        request.setAttribute("ip",ip);
        return "/mobile/login";
    }

    @RequestMapping("/loginAction")
    public void loginAction(HttpServletRequest request,HttpServletResponse response, String name, HttpSession session)throws  Exception {
        if(name!=""&& name!=null){
            session.setAttribute("userName", name);
            request.getRequestDispatcher("/mobile/index").forward(request, response);
        }else{
            request.getRequestDispatcher("/mobile/login").forward(request, response);
        }
    }

    @RequestMapping("/index")
    public String index(HttpServletRequest request)throws  Exception {
        return "/mobile/index";
    }

    @RequestMapping("/todolist")
    public String todolist(HttpServletRequest request, HttpServletResponse  response,HttpSession session)throws  Exception {
        String  returnUrl="";

        Object userName = session.getAttribute("userName");
        if(null == userName) {
            returnUrl="/mobile/login";
            response.sendRedirect(returnUrl);
        }else {
            userName = java.net.URLEncoder.encode(userName.toString(), "UTF-8");
            request.setAttribute("userName", userName);

            //查询数据库，获取当前doc表中的status给index页面加上待办图标
            Doc doc = m_docService.getDocById(1);
            String docStatus = doc.getStatus();

            if ("".equals(docStatus)) {
                docStatus = "起草";
            }
            request.setAttribute("docStatus", docStatus);
            request.setAttribute("doc", doc);
            returnUrl="/mobile/todolist";

        }
             return returnUrl;
    }

    @RequestMapping("/issuelist")
    public String issuelist(HttpServletRequest request, HttpServletResponse  response,HttpSession session)throws  Exception {
        String  returnUrl="";

        Object userName = session.getAttribute("userName");
        if(null == userName) {
            returnUrl="/mobile/login";
            response.sendRedirect(returnUrl);
        }else {
            userName = java.net.URLEncoder.encode(userName.toString(), "UTF-8");
            request.setAttribute("userName", userName);

            //查询数据库，获取当前doc表中的status给index页面加上待办图标
            Doc doc = m_docService.getDocById(1);
            String docStatus = doc.getStatus();

            request.setAttribute("docStatus", docStatus);
            request.setAttribute("doc", doc);
            returnUrl="/mobile/issuelist";
        }
        return  returnUrl;

    }
}

