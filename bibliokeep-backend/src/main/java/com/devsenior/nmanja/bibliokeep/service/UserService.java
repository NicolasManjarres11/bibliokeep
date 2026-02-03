package com.devsenior.nmanja.bibliokeep.service;

import com.devsenior.nmanja.bibliokeep.model.dto.UserRequestDTO;
import com.devsenior.nmanja.bibliokeep.model.dto.UserResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponseDTO create(UserRequestDTO request);

    List<UserResponseDTO> findAll();

    UserResponseDTO findById(UUID id);

    UserResponseDTO findByEmail(String email);

    UserResponseDTO update(UUID id, UserRequestDTO request);

    void delete(UUID id);

    UUID getFirstUserId();
}
