package sumdu.edu.ua.persistence.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sumdu.edu.ua.core.domain.Comment;
import sumdu.edu.ua.persistence.jpa.entity.CommentEntity;

@Mapper(componentModel = "spring")
public interface CommentEntityMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "bookId", source = "bookId")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "text", source = "text")
    @Mapping(target = "createdAt", source = "createdAt")
    Comment toModel(CommentEntity entity);

    CommentEntity toEntity(Comment model);
}
