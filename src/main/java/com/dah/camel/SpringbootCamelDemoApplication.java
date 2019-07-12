package com.dah.camel;

import org.apache.camel.spring.boot.CamelSpringBootApplicationController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackages = {"com.dah.camel"})
public class SpringbootCamelDemoApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringbootCamelDemoApplication.class, args);
        CamelSpringBootApplicationController controller = context.getBean(CamelSpringBootApplicationController.class);
        controller.run();
    }

}
