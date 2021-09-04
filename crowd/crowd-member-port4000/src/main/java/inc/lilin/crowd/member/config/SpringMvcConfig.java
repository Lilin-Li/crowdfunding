package inc.lilin.crowd.member.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/auth/member/to/reg/page").setViewName("member-reg");;
		registry.addViewController("/auth/member/to/login/page").setViewName("member-login");
		registry.addViewController("/auth/member/to/center/page").setViewName("member-center");
		registry.addViewController("/member/my/crowd").setViewName("member-crowd");
	}

}
