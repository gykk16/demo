package dev.glory.demo.domain.auth.service.dto;

import dev.glory.demo.domain.user.role.Role;

public record RegisterServiceRequest(
        String username,
        String password,
        String name,
        String email,
        Role role
) {

}
