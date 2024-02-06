package dev.glory.demo.domain.auth.service.dto;

public record AuthServiceRequest(
        String username,
        String password
) {

}
