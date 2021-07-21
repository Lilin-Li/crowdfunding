package inc.lilin.crowd.admin.config;

import inc.lilin.crowd.admin.Interceptors.CharacterEncodingInterceptors;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class SpringMVC_Config implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }

    public void addInterceptors(InterceptorRegistry registry) {
//        sample code：
//        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns("/index.html","/","/user/login");
        registry.addInterceptor(new CharacterEncodingInterceptors()).addPathPatterns("/**");
    }
}

