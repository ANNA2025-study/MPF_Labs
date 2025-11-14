package sumdu.edu.ua.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sumdu.edu.ua.core.domain.Book;
import sumdu.edu.ua.core.port.CatalogRepositoryPort;

@Controller
@RequestMapping("/books")
public class BookFormController {

    private final CatalogRepositoryPort bookRepo;

    public BookFormController(CatalogRepositoryPort bookRepo) {
        this.bookRepo = bookRepo;
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        return "book-form";
    }

    @PostMapping
    public String addBook(@ModelAttribute Book book) {

        if (book.getTitle() != null && !book.getTitle().isBlank()) {
            bookRepo.add(book.getTitle(), book.getAuthor(), book.getPubYear());
        }

        return "redirect:/books";
    }
}
