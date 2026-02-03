package com.devsenior.nmanja.bibliokeep.mapper;

import com.devsenior.nmanja.bibliokeep.model.dto.BookRequestDTO;
import com.devsenior.nmanja.bibliokeep.model.dto.BookResponseDTO;
import com.devsenior.nmanja.bibliokeep.model.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.UUID;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {

    default Book toEntity(BookRequestDTO dto, UUID ownerId) {
        if (dto == null) {
            return null;
        }
        var book = toEntity(dto);
        book.setOwnerId(ownerId);
        return book;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "isLent", ignore = true)
    Book toEntity(BookRequestDTO dto);

    BookResponseDTO toResponseDTO(Book entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "isLent", ignore = true)
    void updateEntityFromDTO(BookRequestDTO dto, @MappingTarget Book entity);
}
