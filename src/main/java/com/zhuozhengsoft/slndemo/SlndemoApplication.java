package com.zhuozhengsoft.slndemo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@MapperScan("com.zhuozhengsoft.slndemo.mapper")
@SpringBootApplication
public class SlndemoApplication {
    @Value("${moblicpath}")
    private String moblicpath;

    @Value("${policpath}")
    private String policpath;

    @Value("${popassword}")
    private String poPassWord;


    public static void main(String[] args) {
        SpringApplication.run(SlndemoApplication.class, args);
    }

    /**
     * moboffice的这个注册bean必须放pageoffice的后面，否则会找不到pageoffice.js
     * @return
     */
    @Bean
    public ServletRegistrationBean mobServletRegistrationBean() {
        com.zhuozhengsoft.moboffice.Server moserver = new com.zhuozhengsoft.moboffice.Server();
        moserver.setSysPath(moblicpath);
        ServletRegistrationBean srb2 = new ServletRegistrationBean(moserver);
        srb2.setName("mobserver");
        srb2.addUrlMappings("/mobserver.zz");
        srb2.addUrlMappings("/moboffice.js");
        srb2.addUrlMappings("/jquery.min.js");
        return srb2;
    }


    @Bean
    public ServletRegistrationBean pageServletRegistrationBean() {
        com.zhuozhengsoft.pageoffice.poserver.Server poserver = new com.zhuozhengsoft.pageoffice.poserver.Server();
        poserver.setSysPath(policpath);//设置PageOffice注册成功后,license.lic文件存放的目录
        ServletRegistrationBean srb = new ServletRegistrationBean(poserver);
        srb.addUrlMappings("/poserver.zz");
        srb.addUrlMappings("/posetup.exe");
        srb.addUrlMappings("/pageoffice.js");
       // srb.addUrlMappings("/jquery.min.js");
        srb.addUrlMappings("/pobstyle.css");
        srb.addUrlMappings("/sealsetup.exe");
        return srb;//
    }


    /**
     * 添加印章管理程序Servlet（可选）
     * @return
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean2() {
        com.zhuozhengsoft.pageoffice.poserver.AdminSeal adminSeal = new com.zhuozhengsoft.pageoffice.poserver.AdminSeal();
        adminSeal.setAdminPassword(poPassWord);//设置印章管理员admin的登录密码
        adminSeal.setSysPath(policpath);//设置印章数据库文件poseal.db存放的目录
        ServletRegistrationBean srb3 = new ServletRegistrationBean(adminSeal);
        srb3.addUrlMappings("/adminseal.zz");
        srb3.addUrlMappings("/sealimage.zz");
        srb3.addUrlMappings("/loginseal.zz");
        return srb3;
    }

}
