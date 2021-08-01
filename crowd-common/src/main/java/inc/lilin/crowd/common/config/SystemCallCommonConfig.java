package inc.lilin.crowd.common.config;

import inc.lilin.crowd.common.systemcall.CurrentTimeMillisClock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@Configuration
public class SystemCallCommonConfig {

    @Bean
    public CurrentTimeMillisClock currentTimeMillisClock() {
        return CurrentTimeMillisClock.getInstance();
    }
}

