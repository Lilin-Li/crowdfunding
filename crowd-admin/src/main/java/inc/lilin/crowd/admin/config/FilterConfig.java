package inc.lilin.crowd.admin.config;

import inc.lilin.crowd.admin.Interceptors.CharacterEncodingInterceptors;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilterConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CharacterEncodingInterceptors()).addPathPatterns("/**");
//      HiddenHttpMethodFilter (boot之自動配置已配好)
    }
}
