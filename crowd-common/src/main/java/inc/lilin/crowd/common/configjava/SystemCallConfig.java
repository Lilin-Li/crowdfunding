package inc.lilin.crowd.common.configjava;

import inc.lilin.crowd.common.systemcall.CurrentTimeMillisClock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@Configuration
public class SystemCallConfig {

    @Bean
    public CurrentTimeMillisClock currentTimeMillisClock() {
        return CurrentTimeMillisClock.getInstance();
    }
}

