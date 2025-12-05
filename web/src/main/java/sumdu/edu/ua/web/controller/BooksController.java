package sumdu.edu.ua.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sumdu.edu.ua.core.domain.Book;
import sumdu.edu.ua.core.domain.PageRequest;
import sumdu.edu.ua.core.port.CatalogRepositoryPort;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final CatalogRepositoryPort bookRepo;

    public BooksController(CatalogRepositoryPort bookRepo) {
        this.bookRepo = bookRepo;
    }

    @GetMapping
    public String getAllBooks(Model model) {
        List<Book> books = bookRepo.search(null, new PageRequest(0, 20)).getItems();
        model.addAttribute("books", books);
        return "books";   // Thymeleaf template
    }


    @GetMapping("/{id}")
    public String getBookById(@PathVariable long id) {
        return "redirect:/comments?bookId=" + id;
    }
}
