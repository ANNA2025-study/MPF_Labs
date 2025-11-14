package sumdu.edu.ua.persistence.jdbc;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
@Component
public class DbInit {

    @PostConstruct
    public void init() {
        try (Connection c = Db.get();
             Statement st = c.createStatement()) {

            // читаємо schema.sql з resources
            try (var in = getClass().getClassLoader().getResourceAsStream("schema.sql")) {
                if (in == null) {
                    throw new IllegalStateException("schema.sql not found in resources");
                }
                String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                for (String cmd : sql.split(";")) {
                    if (!cmd.isBlank()) {
                        st.execute(cmd);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("DB schema init failed", e);
        }
    }
}
