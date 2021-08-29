package inc.lilin.crowd.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"inc.lilin.crowd"})
public class IMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(IMemberApplication.class, args);
    }
}
