package com.devsenior.nmanja.bibliokeep.mapper;

import com.devsenior.nmanja.bibliokeep.model.dto.LoanRequestDTO;
import com.devsenior.nmanja.bibliokeep.model.dto.LoanResponseDTO;
import com.devsenior.nmanja.bibliokeep.model.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LoanMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "returned", ignore = true)
    Loan toEntity(LoanRequestDTO dto);

    LoanResponseDTO toResponseDTO(Loan entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookId", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "returned", ignore = true)
    void updateEntityFromDTO(LoanRequestDTO dto, @MappingTarget Loan entity);
}
