package sumdu.edu.ua.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sumdu.edu.ua.core.domain.Comment;
import sumdu.edu.ua.core.domain.Page;
import sumdu.edu.ua.core.domain.PageRequest;
import sumdu.edu.ua.core.port.CommentRepositoryPort;
import sumdu.edu.ua.persistence.jpa.entity.CommentEntity;
import sumdu.edu.ua.persistence.jpa.mapper.CommentEntityMapper;
import sumdu.edu.ua.persistence.jpa.repo.CommentJpaRepository;

import java.time.Instant;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaCommentRepositoryAdapter implements CommentRepositoryPort {

    private final CommentJpaRepository repo;
    private final CommentEntityMapper mapper;

    @Override
    public void add(long bookId, String author, String text) {
        CommentEntity entity = new CommentEntity();
        entity.setBookId(bookId);
        entity.setAuthor(author);
        entity.setText(text);
        entity.setCreatedAt(Instant.now());
        repo.save(entity);
    }

    @Override
    public Page<Comment> list(long bookId, String author, Instant since, PageRequest request) {
        var all = repo.findByBookIdOrderByCreatedAtDesc(bookId);

        // TODO: можна фільтрувати за author/since, а потім робити пагінацію вручну
        List<Comment> content = all.stream()
                .map(mapper::toModel)
                .toList();

        // Поки що можна зробити примітивну пагінацію вручну
        int from = request.getPage() * request.getSize();
        int to = Math.min(from + request.getSize(), content.size());
        List<Comment> pageSlice = from >= content.size() ? List.of() : content.subList(from, to);

        int totalPages = (int) Math.ceil((double) content.size() / request.getSize());

        return new Page<>(pageSlice, request.getPage(), totalPages);
    }

    @Override
    public void delete(long bookId, long commentId) {
        repo.deleteById(commentId);
    }
}
