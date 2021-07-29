package inc.lilin.crowd.common.config;


import inc.lilin.crowd.common.web.springmvc.interceptors.CharacterEncodingInterceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    CharacterEncodingInterceptors characterEncodingInterceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 須用autowired方式才能取得動態代理的指標
        registry.addInterceptor(characterEncodingInterceptors).addPathPatterns("/**")
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/favicon.ico")
                .order(Ordered.HIGHEST_PRECEDENCE);

        // HiddenHttpMethodFilter(boot自動配置已配好)
    }
}
