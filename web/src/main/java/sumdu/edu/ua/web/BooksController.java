package sumdu.edu.ua.web;


import io.javalin.Javalin;
import io.javalin.http.Context;
import sumdu.edu.ua.core.domain.PageRequest;
import sumdu.edu.ua.core.port.CatalogRepositoryPort;

public class BooksController {

    private final CatalogRepositoryPort bookRepo;

    public BooksController(CatalogRepositoryPort bookRepo) {
        this.bookRepo = bookRepo;
    }

    public void registerRoutes(Javalin app) {

        app.get("/books", ctx -> ctx.redirect("/books.html"));
    }

    private void getBooks(Context ctx) {
        var books = bookRepo.search("", new PageRequest(0, 100)).getItems();
        ctx.json(books);
    }
}
