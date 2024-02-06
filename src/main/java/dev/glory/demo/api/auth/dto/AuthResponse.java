package dev.glory.demo.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.glory.demo.domain.auth.service.dto.AuthServiceResponse;

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
