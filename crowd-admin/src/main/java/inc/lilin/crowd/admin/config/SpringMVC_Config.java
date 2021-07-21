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
        registry.addInterceptor(new CharacterEncodingInterceptors()).addPathPatterns("/**");
//      HiddenHttpMethodFilter (boot之自動配置已配好)
    }
}

