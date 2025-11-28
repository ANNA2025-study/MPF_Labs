package sumdu.edu.ua.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = "sumdu.edu.ua")
@EnableJpaRepositories(basePackages = "sumdu.edu.ua.persistence.jpa.repo")
@EntityScan(basePackages = "sumdu.edu.ua.core.domain")
@ComponentScan(basePackages = "sumdu.edu.ua")
public class AppInit {
    public static void main(String[] args) {
        SpringApplication.run(AppInit.class, args);
        System.out.println("Started at http://localhost:8080/books");

    }
}


