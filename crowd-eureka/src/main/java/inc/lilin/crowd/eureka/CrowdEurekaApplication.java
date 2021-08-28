package inc.lilin.crowd.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableEurekaServer
@SpringBootApplication
public class CrowdEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrowdEurekaApplication.class, args);
    }

}
