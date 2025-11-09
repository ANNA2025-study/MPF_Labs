package sumdu.edu.ua.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sumdu.edu.ua.core.port.CatalogRepositoryPort;
import sumdu.edu.ua.core.port.CommentRepositoryPort;
import sumdu.edu.ua.infrastructure.ApplicationInitializer;

public class JavalinBookApp {

    private static final Logger log = LoggerFactory.getLogger(JavalinBookApp.class);

    public static void main(String[] args) {

        CatalogRepositoryPort bookRepo = ApplicationInitializer.createCatalogRepository();
        CommentRepositoryPort commentRepo = ApplicationInitializer.createCommentRepository();

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        var jsonMapper = new JavalinJackson(mapper, false);

        var app = Javalin.create(cfg -> {
            cfg.staticFiles.add("/public");
            cfg.jsonMapper(jsonMapper);
        });

        new BooksController(bookRepo).registerRoutes(app);
        new CommentsController(commentRepo, bookRepo).registerRoutes(app);
        new BooksApiController(bookRepo).registerRoutes(app);

        app.start(8080);
        log.info("Server started at http://localhost:8080/books");
    }
}
