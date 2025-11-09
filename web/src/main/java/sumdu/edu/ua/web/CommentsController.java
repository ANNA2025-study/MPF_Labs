package sumdu.edu.ua.web;

import io.javalin.Javalin;
import io.javalin.http.Context;
import sumdu.edu.ua.core.domain.PageRequest;
import sumdu.edu.ua.core.port.CatalogRepositoryPort;
import sumdu.edu.ua.core.port.CommentRepositoryPort;

import java.util.Map;

public class CommentsController {

    private final CommentRepositoryPort commentRepo;
    private final CatalogRepositoryPort bookRepo;

    public CommentsController(CommentRepositoryPort commentRepo, CatalogRepositoryPort bookRepo) {
        this.commentRepo = commentRepo;
        this.bookRepo = bookRepo;
    }

    public void registerRoutes(Javalin app) {

        // --- REST API ---
        app.get("/api/comments", this::getComments);
        app.post("/api/comments", this::addComment);
        app.delete("/api/comments/{commentId}", this::deleteComment);

        // --- сторінка з фронтом ---
        app.get("/comments", ctx -> ctx.redirect("/book-comments.html"));
    }

    /**
     * Повертає JSON зі списком коментарів до книги
     */
    private void getComments(Context ctx) {
        Long bookId = ctx.queryParamAsClass("bookId", Long.class).getOrDefault(null);
        if (bookId == null) {
            ctx.status(400).result("Missing bookId");
            return;
        }

        var book = bookRepo.findById(bookId);
        var comments = commentRepo.list(bookId, null, null, new PageRequest(0, 20)).getItems();

        ctx.json(Map.of(
                "book", book,
                "comments", comments
        ));
    }

    /**
     * Додає коментар (через POST /api/comments)
     */
    private void addComment(Context ctx) {
        Long bookId = ctx.formParamAsClass("bookId", Long.class).getOrDefault(null);
        String author = ctx.formParam("author");
        String text = ctx.formParam("text");

        if (bookId == null || author == null || text == null ||
                author.isBlank() || text.isBlank()) {
            ctx.status(400).result("bookId, author and text required");
            return;
        }
        System.out.println("Adding comment to bookId = " + bookId + " by " + author);

        commentRepo.add(bookId, author.trim(), text.trim());
        ctx.status(201).json(Map.of("status", "ok"));
    }

    /**
     * Видаляє коментар (через DELETE /api/comments/:commentId)
     */
    private void deleteComment(Context ctx) {
        Long bookId = ctx.queryParamAsClass("bookId", Long.class).getOrDefault(null);
        Long commentId = ctx.pathParamAsClass("commentId", Long.class).getOrDefault(null);

        if (bookId == null || commentId == null) {
            ctx.status(400).result("bookId and commentId required");
            return;
        }

        commentRepo.delete(bookId, commentId);
        ctx.status(200).json(Map.of("status", "deleted"));
    }
}
