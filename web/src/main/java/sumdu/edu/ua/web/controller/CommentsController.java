package sumdu.edu.ua.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sumdu.edu.ua.core.domain.Comment;
import sumdu.edu.ua.core.domain.PageRequest;
import sumdu.edu.ua.core.port.CatalogRepositoryPort;
import sumdu.edu.ua.core.port.CommentRepositoryPort;
import sumdu.edu.ua.core.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/comments")
public class CommentsController {

    private final CommentRepositoryPort commentRepo;
    private final CatalogRepositoryPort bookRepo;
    private final UserService userService;

    public CommentsController(CommentRepositoryPort commentRepo,
                              CatalogRepositoryPort bookRepo,
                              UserService userService) {
        this.commentRepo = commentRepo;
        this.bookRepo = bookRepo;
        this.userService = userService;
    }

    // -------------------------
    // GET: /comments?bookId=1
    // -------------------------
    @GetMapping
    public String list(@RequestParam long bookId,
                       Model model,
                       Authentication auth) {

        var book = bookRepo.findById(bookId);
        if (book == null) {
            // Книга не знайдена — редірект назад
            return "redirect:/books";
        }

        List<Comment> comments = commentRepo
                .list(bookId, null, null, new PageRequest(0, 20))
                .getItems();

        model.addAttribute("book", book);
        model.addAttribute("comments", comments);

        // Підкидаємо в модель нік поточного користувача
        if (auth != null && auth.isAuthenticated()) {
            var principal = (org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal();
            String email = principal.getUsername(); // у нас email = username
            var user = userService.findByEmailOrThrow(email);
            model.addAttribute("currentUserNickname", user.getNickname());
        }

        return "book-comments"; // Thymeleaf-шаблон
    }

    // ---------------------
    // POST: add new comment
    // ---------------------
    @PostMapping
    public String add(@RequestParam long bookId,
                      @RequestParam String text,
                      Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            // На всякий випадок — захист від анонімів
            return "redirect:/login";
        }

        if (text == null || text.isBlank()) {
            return "redirect:/comments?bookId=" + bookId;
        }

        var principal = (org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal();
        String email = principal.getUsername();
        var user = userService.findByEmailOrThrow(email);

        String author = user.getNickname();

        commentRepo.add(bookId, author.trim(), text.trim());

        return "redirect:/comments?bookId=" + bookId;
    }

    // -----------------------
    // POST: delete comment
    // -----------------------
    @PostMapping("/delete")
    public String delete(@RequestParam long bookId,
                         @RequestParam long commentId) {

        commentRepo.delete(bookId, commentId);

        return "redirect:/comments?bookId=" + bookId;
    }
}
