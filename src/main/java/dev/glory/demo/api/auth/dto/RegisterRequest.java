package dev.glory.demo.api.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import dev.glory.demo.domain.auth.service.dto.RegisterServiceRequest;
import dev.glory.demo.domain.user.role.Role;

/**
 * 사용자 등록 api spec
 *
 * @param username 사용자 id
 * @param password 비밀번호
 * @param name     이름
 * @param email    이메일
 * @param role     권한
 */
public record RegisterRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String name,
        @NotBlank String email,
        @NotNull Role role
) {

    public RegisterServiceRequest toServiceRequest() {
        return new RegisterServiceRequest(
                username.strip(),
                password.strip(),
                name.strip(),
                email.strip(),
                role
        );
    }
}
