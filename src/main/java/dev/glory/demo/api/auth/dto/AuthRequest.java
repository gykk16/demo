package dev.glory.demo.api.auth.dto;

import jakarta.validation.constraints.NotBlank;

import dev.glory.demo.domain.auth.service.dto.AuthServiceRequest;

public record AuthRequest(
        @NotBlank
        String username,
        @NotBlank
        String password
) {

    public AuthServiceRequest toServiceRequest() {
        return new AuthServiceRequest(
                username.strip(),
                password.strip()
        );
    }
}
