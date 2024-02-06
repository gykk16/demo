package dev.glory.demo.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.glory.demo.domain.auth.service.dto.AuthServiceResponse;

/**
 * 인증 api 응답 spec
 *
 * @param accessToken  accessToken
 * @param refreshToken refreshToken
 */
public record AuthResponse(
        @JsonProperty("accessToken")
        String accessToken,
        @JsonProperty("refreshToken")
        String refreshToken
) {

    public static AuthResponse from(AuthServiceResponse serviceResponse) {
        return new AuthResponse(serviceResponse.accessToken(), serviceResponse.refreshToken());
    }

}
