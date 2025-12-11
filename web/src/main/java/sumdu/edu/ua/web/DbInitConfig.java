package sumdu.edu.ua.web;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import sumdu.edu.ua.persistence.jdbc.DbInit;

@Configuration
public class DbInitConfig {

    @PostConstruct
    public void initDb() {
        DbInit.init();
    }
}
