package inc.lilin.crowd.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login.html").setViewName("admin-login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/frontend/bootstrap/**").addResourceLocations("classpath:/frontend/bootstrap/");
        registry.addResourceHandler("/frontend/css/**").addResourceLocations("classpath:/frontend/css/");
        registry.addResourceHandler("/frontend/font/**").addResourceLocations("classpath:/frontend/font/");
        registry.addResourceHandler("/frontend/img/**").addResourceLocations("classpath:/frontend/img/");
        registry.addResourceHandler("/frontend/jquery/**").addResourceLocations("classpath:/frontend/jquery/");
        registry.addResourceHandler("/frontend/script/**").addResourceLocations("classpath:/frontend/script/");
        registry.addResourceHandler("/frontend/ztree/**").addResourceLocations("classpath:/frontend/ztree/");
    }
}


