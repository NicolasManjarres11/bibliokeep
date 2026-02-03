package com.devsenior.nmanja.bibliokeep.service.impl;

import com.devsenior.nmanja.bibliokeep.mapper.UserMapper;
import com.devsenior.nmanja.bibliokeep.model.dto.UserRequestDTO;
import com.devsenior.nmanja.bibliokeep.model.dto.UserResponseDTO;
import com.devsenior.nmanja.bibliokeep.repository.UserRepository;
import com.devsenior.nmanja.bibliokeep.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO create(UserRequestDTO request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        
        if (user.getAnnualGoal() == null) {
            user.setAnnualGoal(12);
        }

        var savedUser = userRepository.save(user);
        return userMapper.toResponseDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        var users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findById(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findByEmail(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return userMapper.toResponseDTO(user);
    }

    @Override
    public UserResponseDTO update(UUID id, UserRequestDTO request) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!user.getEmail().equals(request.email())) {
            if (userRepository.findByEmail(request.email()).isPresent()) {
                throw new IllegalArgumentException("El correo electrónico ya está en uso");
            }
        }

        userMapper.updateEntityFromDTO(request, user);
        
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        var updatedUser = userRepository.save(user);
        return userMapper.toResponseDTO(updatedUser);
    }

    @Override
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UUID getFirstUserId() {
        return userRepository.findAll().stream()
                .findFirst()
                .map(user -> user.getId())
                .orElseThrow(() -> new IllegalArgumentException("No hay usuarios registrados"));
    }
}
