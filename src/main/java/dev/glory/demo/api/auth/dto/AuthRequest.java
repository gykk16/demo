package dev.glory.demo.api.auth.dto;

import jakarta.validation.constraints.NotBlank;

import dev.glory.demo.domain.auth.service.dto.AuthServiceRequest;

/**
 * 사용자 인증 api spec
 *
 * @param username 사용자 id
 * @param password 비밀번호
 */
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
