package inc.lilin.crowd.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/admin-main").setViewName("admin-main");
        registry.addViewController("/admin/add").setViewName("add");
        registry.addViewController("/role/page").setViewName("role");
        registry.addViewController("/menu/to/page.html").setViewName("menu-page");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/frontend/bootstrap/**").addResourceLocations("classpath:/frontend/bootstrap/");
        registry.addResourceHandler("/frontend/css/**").addResourceLocations("classpath:/frontend/css/");
        registry.addResourceHandler("/frontend/fonts/**").addResourceLocations("classpath:/frontend/fonts/");
        registry.addResourceHandler("/frontend/img/**").addResourceLocations("classpath:/frontend/img/");
        registry.addResourceHandler("/frontend/jquery/**").addResourceLocations("classpath:/frontend/jquery/");
        registry.addResourceHandler("/frontend/layer/**").addResourceLocations("classpath:/frontend/layer/");
        registry.addResourceHandler("/frontend/script/**").addResourceLocations("classpath:/frontend/script/");
        registry.addResourceHandler("/frontend/ztree/**").addResourceLocations("classpath:/frontend/ztree/");
    }
}


