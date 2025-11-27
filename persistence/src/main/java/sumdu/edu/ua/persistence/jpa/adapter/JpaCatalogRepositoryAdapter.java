package sumdu.edu.ua.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest as SpringPageRequest;
import org.springframework.stereotype.Repository;
import sumdu.edu.ua.core.domain.Book;
import sumdu.edu.ua.core.domain.Page;
import sumdu.edu.ua.core.domain.PageRequest;
import sumdu.edu.ua.core.port.CatalogRepositoryPort;
import sumdu.edu.ua.persistence.jpa.mapper.BookEntityMapper;
import sumdu.edu.ua.persistence.jpa.repo.BookJpaRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaCatalogRepositoryAdapter implements CatalogRepositoryPort {

    private final BookJpaRepository repo;
    private final BookEntityMapper mapper;

    @Override
    public Page<Book> search(String query, PageRequest request) {
        // Поки що можна зробити простий варіант: завантажити всі і відфільтрувати
        // або додати кастомний метод у BookJpaRepository
        var pageable = org.springframework.data.domain.PageRequest.of(
                request.getPage(), request.getSize()
        );

        var page = repo.findAll(pageable); // TODO: замінити на пошук за query

        List<Book> content = page.getContent().stream()
                .map(mapper::toModel)
                .toList();

        return new Page<>(content, request.getPage(), page.getTotalPages());
    }

    @Override
    public Book findById(long id) {
        return repo.findById(id)
                .map(mapper::toModel)
                .orElse(null);
    }

    @Override
    public Book add(String title, String author, int pubYear) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPubYear(pubYear);

        var saved = repo.save(mapper.toEntity(book));
        return mapper.toModel(saved);
    }
}
