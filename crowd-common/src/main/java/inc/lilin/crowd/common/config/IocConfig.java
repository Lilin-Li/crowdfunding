package inc.lilin.crowd.common.config;

import inc.lilin.crowd.common.api.CurrentTimeMillisClock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IocConfig {

    @Bean
    public CurrentTimeMillisClock currentTimeMillisClock() {
        return CurrentTimeMillisClock.getInstance();
    }
}

