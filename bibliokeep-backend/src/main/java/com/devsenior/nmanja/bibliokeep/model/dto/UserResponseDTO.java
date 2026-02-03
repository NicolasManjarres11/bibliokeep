package com.devsenior.nmanja.bibliokeep.model.dto;

import java.util.Set;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String email,
        Set<String> preferences,
        Integer annualGoal
) {
}
