package inc.lilin.crowd.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"inc.lilin.crowd"})
public class MyOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyOrderApplication.class, args);
    }

}
