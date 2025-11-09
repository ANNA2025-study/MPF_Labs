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

        // --- список коментарів до книги ---
        app.get("/comments", this::listComments);

        // --- додати коментар ---
        app.post("/comments", this::addComment);

        // --- видалити коментар ---
        app.post("/comments/delete", this::deleteComment);
    }

    private void listComments(Context ctx) {
        long bookId = ctx.queryParamAsClass("bookId", Long.class).get();
        var book = bookRepo.findById(bookId);
        var comments = commentRepo.list(bookId, null, null, new PageRequest(0, 20)).getItems();

        ctx.render("book-comments.mustache", Map.of(
                "book", book,
                "comments", comments
        ));
    }

    private void addComment(Context ctx) {
        long bookId = ctx.formParamAsClass("bookId", Long.class).get();
        String author = ctx.formParam("author");
        String text = ctx.formParam("text");

        if (author == null || text == null || author.isBlank() || text.isBlank()) {
            ctx.status(400).result("author and text required");
            return;
        }

        commentRepo.add(bookId, author.trim(), text.trim());
        ctx.redirect("/comments?bookId=" + bookId);
    }

    private void deleteComment(Context ctx) {
        long bookId = ctx.formParamAsClass("bookId", Long.class).get();
        long commentId = ctx.formParamAsClass("commentId", Long.class).get();
        commentRepo.delete(bookId, commentId);
        ctx.redirect("/comments?bookId=" + bookId);
    }
}
