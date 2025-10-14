package sumdu.edu.ua.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sumdu.edu.ua.core.domain.Book;
import sumdu.edu.ua.core.domain.PageRequest;
import sumdu.edu.ua.core.port.CatalogRepositoryPort;

import java.util.List;


@RestController
@RequestMapping("/books")
public class BooksController {

    private final CatalogRepositoryPort bookRepo;

    public BooksController(CatalogRepositoryPort bookRepo) {
        this.bookRepo = bookRepo;
    }


        @GetMapping
        public List<Book> getAllBooks() {
            return bookRepo.search(null, new PageRequest(0, 20)).getItems();
        }

        @GetMapping("/{id}")
        public Book getBook(@PathVariable long id) {
            return bookRepo.findById(id);
        }
    }

