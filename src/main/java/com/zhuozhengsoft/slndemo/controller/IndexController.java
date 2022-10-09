package com.zhuozhengsoft.slndemo.controller;
import com.zhuozhengsoft.slndemo.utils.QRCodeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author: dong
 * Date: 2022/7/23 10:05
 * Version 1.0
 */
@Controller
public class IndexController {
    @RequestMapping(value = {"/","/index"})
    public String index(HttpServletRequest request, HttpServletResponse response)throws  Exception {

        String path = request.getContextPath();
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;

        if(!request.getServerName().equals("127.0.0.1") && !request.getServerName().toLowerCase().equals("localhost")){
            BASE64Encoder encoder = new BASE64Encoder();

            QRCodeUtil qrCode = new QRCodeUtil(basePath+"/mobile/login");
            byte[] imageBytes = qrCode.getQRCodeBytes();

            QRCodeUtil qrCode2 = new QRCodeUtil(basePath+"/apk/PoDroid_5.apk");
            byte[] imageBytes2 = qrCode2.getQRCodeBytes();

            String mobileQrCodeString = encoder.encode(imageBytes);
            String apkQrCodeString = encoder.encode(imageBytes2);
            request.setAttribute("mobImgPath", "data:image/png;base64,"+ apkQrCodeString );
            request.setAttribute("webImgPath","data:image/png;base64,"+ mobileQrCodeString );
        }else{
           request.setAttribute("msg","请用电脑IP访问，不要使用localhost或127.0.0.1");
        }
        return "index";
    }
}
