package com.zhuozhengsoft.slndemo.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: dong
 * @Date: 2022/7/29 17:37
 * @Version 1.0
 */

@Configuration

public class LoginConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new LoginInterceptor());
        registration.addPathPatterns("/**").excludePathPatterns(//添加不拦截路径
                "/",
                "/index",                   //首页
                "/pc/login",                //pc登录
                "/pc/loginAction",         //pc登录后台
                "/pc/restart",               //首页的复位，这时候还没有登录呢，没有userName
          //      "/mobile/**",               //moboffice从5.2.0.16开始支持session和token了，所以这块可以统一去拦截判断用户是否登录
                "/assets/**",               //css,js静态资源
                "/apk/**",                  //moboffice客户端apk程序
                "/pc/*.html",
                "/posetup.exe",             //pageoffice相关
                "/sealsetup.exe",
                "/jquery.min.js",
                "/pageoffice.js",
                "/moboffice.js",
                "/pobstyle.css",
                "/poserver.zz",
                "/mobserver.zz",
                "/loginseal.zz",
                "/adminseal.zz",
                "/sealimage.zz"

        );
    }
}

