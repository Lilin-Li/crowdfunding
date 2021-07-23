package inc.lilin.crowd.common.config;

import inc.lilin.crowd.common.api.CurrentTimeMillisClock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@Configuration
public class MainConfig {

    @Bean
    public CurrentTimeMillisClock currentTimeMillisClock() {
        return CurrentTimeMillisClock.getInstance();
    }
}

