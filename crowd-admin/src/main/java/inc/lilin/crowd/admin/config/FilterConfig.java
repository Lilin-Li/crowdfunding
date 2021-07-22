package inc.lilin.crowd.admin.config;

import inc.lilin.crowd.admin.interceptors.CharacterEncodingInterceptors;
import inc.lilin.crowd.admin.interceptors.ResponseTimeLoggingInterceptors;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilterConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CharacterEncodingInterceptors()).addPathPatterns("/**").order(Ordered.HIGHEST_PRECEDENCE);
        registry.addInterceptor(new ResponseTimeLoggingInterceptors()).addPathPatterns("/**");
//      HiddenHttpMethodFilter (boot自動配置已配好)
    }
}
