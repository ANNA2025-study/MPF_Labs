package sumdu.edu.ua.persistence.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sumdu.edu.ua.core.domain.Book;
import sumdu.edu.ua.persistence.jpa.entity.BookEntity;

@Mapper(componentModel = "spring")
public interface BookEntityMapper {

    BookEntity toEntity(Book model);

    @Mapping(target = "pubYear", source = "pubYear")
    Book toModel(BookEntity entity);
}
