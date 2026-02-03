package com.devsenior.nmanja.bibliokeep.mapper;

import com.devsenior.nmanja.bibliokeep.model.dto.UserRequestDTO;
import com.devsenior.nmanja.bibliokeep.model.dto.UserResponseDTO;
import com.devsenior.nmanja.bibliokeep.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequestDTO dto);

    UserResponseDTO toResponseDTO(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateEntityFromDTO(UserRequestDTO dto, @MappingTarget User entity);
}
