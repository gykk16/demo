package dev.glory.demo.domain.auth.service.dto;

public record AuthServiceResponse(
        String accessToken,
        String refreshToken
) {

}
