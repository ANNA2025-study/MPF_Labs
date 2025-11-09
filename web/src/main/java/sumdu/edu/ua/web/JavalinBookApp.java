package sumdu.edu.ua.web;

import io.javalin.Javalin;
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

        var app = Javalin.create(config -> {
            config.staticFiles.add("src/main/resources/public");
        }).start(8081);

        new BooksController(bookRepo).registerRoutes(app);
        new CommentsController(commentRepo, bookRepo).registerRoutes(app);
        new BooksApiController(bookRepo).registerRoutes(app);

        log.info("Server started at http://localhost:8081");
    }
}
