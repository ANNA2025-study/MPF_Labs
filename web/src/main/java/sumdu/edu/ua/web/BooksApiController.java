package sumdu.edu.ua.web;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sumdu.edu.ua.core.domain.Book;
import sumdu.edu.ua.core.domain.PageRequest;
import sumdu.edu.ua.core.port.CatalogRepositoryPort;

public class BooksApiController {

    private static final Logger log = LoggerFactory.getLogger(BooksApiController.class);
    private final CatalogRepositoryPort bookRepo;

    public BooksApiController(CatalogRepositoryPort bookRepo) {
        this.bookRepo = bookRepo;
    }

    public void registerRoutes(Javalin app) {
        // REST API — JSON only
        app.get("/api/books", this::getBooks);
        app.post("/api/books", this::addBook);
    }

    private void getBooks(Context ctx) {
        try {
            String q = ctx.queryParam("q");
            if (q == null) q = "";

            int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(0);
            int size = ctx.queryParamAsClass("size", Integer.class).getOrDefault(10);

            var result = bookRepo.search(q, new PageRequest(page, size));
            ctx.json(result);

        } catch (Exception e) {
            log.error("DB error in GET /api/books", e);
            ctx.status(500).json(Map.of("error", "Database error"));
        }
    }

    private void addBook(Context ctx) {
        try {
            Book book = ctx.bodyAsClass(Book.class);

            if (book.getTitle() == null || book.getTitle().isBlank() ||
                book.getAuthor() == null || book.getAuthor().isBlank()) {
                ctx.status(400).json(Map.of("error", "title and author required"));
                return;
            }

            if (book.getPubYear() <= 0) {
                ctx.status(400).json(Map.of("error", "invalid pubYear"));
                return;
            }

            Book saved = bookRepo.add(
                book.getTitle().trim(),
                book.getAuthor().trim(),
                book.getPubYear()
            );
            ctx.status(201).json(saved);

        } catch (Exception e) {
            log.error("DB error in POST /api/books", e);
            ctx.status(500).json(Map.of("error", "Database error"));
        }
    }
}
