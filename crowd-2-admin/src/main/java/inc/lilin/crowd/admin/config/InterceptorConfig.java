package inc.lilin.crowd.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    //
    // 改用Spring Security
    //
    // @Autowired
    // LoginInterceptor loginInterceptor;
    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    //     // 須用autowired方式才能取得動態代理的指標
    //     registry.addInterceptor(loginInterceptor)
    //             .addPathPatterns("/**")
    //             // 以下是免登入就能訪問的網址
    //             .excludePathPatterns("/")  //首頁
    //             .excludePathPatterns("/guestLogin")    //登入controller
    //             .excludePathPatterns("/admin/logout")  //登出controller
    //             .excludePathPatterns("/static/**")
    //             .excludePathPatterns("/frontend/**")
    //             .excludePathPatterns("/favicon.ico")
    //             .order(Ordered.HIGHEST_PRECEDENCE);
    //     // HiddenHttpMethodFilter(boot自動配置已配好)
    // }
}
