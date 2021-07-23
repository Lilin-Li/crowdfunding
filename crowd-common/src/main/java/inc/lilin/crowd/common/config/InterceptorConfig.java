package inc.lilin.crowd.common.config;


import inc.lilin.crowd.common.interceptors.CharacterEncodingInterceptors;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public  class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CharacterEncodingInterceptors()).addPathPatterns("/**").order(Ordered.HIGHEST_PRECEDENCE);
//      HiddenHttpMethodFilter (boot自動配置已配好)
    }
}
