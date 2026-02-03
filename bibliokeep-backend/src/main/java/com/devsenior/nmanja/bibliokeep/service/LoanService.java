package com.devsenior.nmanja.bibliokeep.service;

import com.devsenior.nmanja.bibliokeep.model.dto.LoanRequestDTO;
import com.devsenior.nmanja.bibliokeep.model.dto.LoanResponseDTO;

import java.util.List;
import java.util.UUID;

public interface LoanService {

    LoanResponseDTO create(UUID ownerId, LoanRequestDTO request);

    List<LoanResponseDTO> findAllByOwnerId(UUID ownerId);

    LoanResponseDTO findByIdAndOwnerId(Long id, UUID ownerId);

    LoanResponseDTO update(Long id, UUID ownerId, LoanRequestDTO request);

    void delete(Long id, UUID ownerId);

    LoanResponseDTO returnLoan(Long id, UUID ownerId);
}
