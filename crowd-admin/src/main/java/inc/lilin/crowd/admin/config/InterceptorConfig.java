package inc.lilin.crowd.admin.config;

import inc.lilin.crowd.admin.web.springmvc.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 須用autowired方式才能取得動態代理的指標
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/")    //首頁
                .excludePathPatterns("/guestLogin")    //登入controller
                .excludePathPatterns("/admin/logout")  //登出controller
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/frontend/**")
                .excludePathPatterns("/favicon.ico")
                .order(Ordered.HIGHEST_PRECEDENCE);

        // HiddenHttpMethodFilter(boot自動配置已配好)
    }
}
