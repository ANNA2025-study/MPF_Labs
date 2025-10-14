package sumdu.edu.ua.web;

import org.springframework.web.bind.annotation.*;
import sumdu.edu.ua.core.port.CommentRepositoryPort;
import sumdu.edu.ua.core.domain.Comment;
import sumdu.edu.ua.core.domain.PageRequest;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentsController {
    private final CommentRepositoryPort commentRepo;

    public CommentsController(CommentRepositoryPort commentRepo) {
        this.commentRepo = commentRepo;
    }

    @GetMapping("/{bookId}")
    public List<Comment> list(@PathVariable long bookId) {
        return commentRepo.list(bookId, null, null, new PageRequest(0, 20)).getItems();
    }

    @PostMapping
    public void add(@RequestParam long bookId,
                    @RequestParam String author,
                    @RequestParam String text) {
        commentRepo.add(bookId, author.trim(), text.trim());
    }
}
