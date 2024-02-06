package dev.glory.demo.api.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 토크 재발급 api spec
 *
 * @param refreshToken refreshToken
 */
public record RefreshTokenRequest(
        @NotBlank String refreshToken
) {

}
