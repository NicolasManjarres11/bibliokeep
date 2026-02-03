package com.devsenior.nmanja.bibliokeep.controller;

import com.devsenior.nmanja.bibliokeep.model.dto.UserRequestDTO;
import com.devsenior.nmanja.bibliokeep.model.dto.UserResponseDTO;
import com.devsenior.nmanja.bibliokeep.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO create(@Valid @RequestBody UserRequestDTO request) {
        return userService.create(request);
    }

    @GetMapping
    public List<UserResponseDTO> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserResponseDTO findById(@PathVariable UUID id) {
        return userService.findById(id);
    }

    @GetMapping("/email/{email}")
    public UserResponseDTO findByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    @PutMapping("/{id}")
    public UserResponseDTO update(
            @PathVariable UUID id,
            @Valid @RequestBody UserRequestDTO request) {
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }

    @GetMapping("/first")
    public UUID getFirstUserId() {
        return userService.getFirstUserId();
    }
}
