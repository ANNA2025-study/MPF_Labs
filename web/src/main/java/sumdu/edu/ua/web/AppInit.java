package sumdu.edu.ua.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sumdu.edu.ua.core.domain.Book;
import sumdu.edu.ua.core.domain.PageRequest;
import sumdu.edu.ua.core.port.CatalogRepositoryPort;
import sumdu.edu.ua.core.port.CommentRepositoryPort;
import sumdu.edu.ua.persistence.jdbc.DbInit;
import sumdu.edu.ua.persistence.jdbc.JdbcBookRepository;
import sumdu.edu.ua.persistence.jdbc.JdbcCommentRepository;

public class JavalinBookApp {
    private static final Logger log = LoggerFactory.getLogger(JavalinBookApp.class);

    public static void main(String[] args) {
        // 1) init DB schema (та сама H2, що й у попередніх лабах)
        DbInit.init();

        // 2) простий "beans"
        CatalogRepositoryPort bookRepo = new JdbcBookRepository();
        CommentRepositoryPort commentRepo = new JdbcCommentRepository();
        ObjectMapper mapper = new ObjectMapper();

        // 3) app
        var app = Javalin.create(cfg -> {
            cfg.http.defaultContentType = "application/json";
            cfg.router.ignoreTrailingSlashes = true;
        });

        // 4) middleware (before) — простий access log
        app.before(ctx -> {
            log.info("{} {}", ctx.method(), ctx.path());
        });

        // 5) винятки → 500 JSON
        app.exception(Exception.class, (e, ctx) -> {
            log.error("Unhandled error", e);
            ctx.status(500).result("{\"error\":\"server error\"}");
        });

        // 6) маршрути REST
        app.get("/api/books", ctx -> getBooks(ctx, bookRepo));
        app.get("/api/books/{id}", ctx -> getBook(ctx, bookRepo));
        app.post("/api/books", ctx -> addBook(ctx, mapper, bookRepo));

        // (за бажанням) коментарі
        // app.get("/api/books/{id}/comments", ctx -> listComments(...));
        // app.post("/api/books/{id}/comments", ctx -> addComment(...));

        app.start(8081);
        log.info("Javalin started at http://localhost:8081/");
    }

    private static void getBooks(Context ctx, CatalogRepositoryPort repo) {
        String q = ctx.queryParam("q");
        int page = parseInt(ctx.queryParam("page"), 0);
        int size = parseInt(ctx.queryParam("size"), 10);

        var pr = new PageRequest(page, size);
        var result = repo.search(q, pr);
        ctx.json(result);
    }

    private static void getBook(Context ctx, CatalogRepositoryPort repo) {
        long id = Long.parseLong(ctx.pathParam("id"));
        var book = repo.findById(id);
        if (book == null) {
            ctx.status(404).result("{\"error\":\"book not found\"}");
            return;
        }
        ctx.json(book);
    }

    private static void addBook(Context ctx, ObjectMapper om, CatalogRepositoryPort repo) throws Exception {
        Book book = om.readValue(ctx.bodyInputStream(), Book.class);

        if (book.getTitle() == null || book.getTitle().isBlank()
                || book.getAuthor() == null || book.getAuthor().isBlank()) {
            ctx.status(400).result("{\"error\":\"title & author required\"}");
            return;
        }
        if (book.getPubYear() <= 0) {
            ctx.status(400).result("{\"error\":\"invalid pubYear\"}");
            return;
        }

        var saved = repo.add(book.getTitle().trim(), book.getAuthor().trim(), book.getPubYear());
        ctx.status(201).json(saved);
    }

    private static int parseInt(String s, int def) {
        try { return s == null ? def : Integer.parseInt(s); }
        catch (NumberFormatException e) { return def; }
    }
}
