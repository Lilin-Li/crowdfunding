package inc.lilin.crowd.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMVCConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // view-controller 是在 project-consumer 內部定義的，所以這裡是一個不經過 Zuul訪問的地址，所以這個路徑前面不加路由規則中定義的字首：「/project」
        registry.addViewController("/agree/protocol/page").setViewName("project-agree");
        registry.addViewController("/launch/project/page").setViewName("project-launch");

    }


}
