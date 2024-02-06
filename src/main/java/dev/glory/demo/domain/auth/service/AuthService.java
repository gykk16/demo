package dev.glory.demo.domain.auth.service;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

import dev.glory.demo.common.exception.BizRuntimeException;
import dev.glory.demo.domain.auth.AuthErrorCode;
import dev.glory.demo.domain.auth.RefreshToken;
import dev.glory.demo.domain.auth.repository.RefreshTokenRepository;
import dev.glory.demo.domain.auth.service.dto.AuthServiceRequest;
import dev.glory.demo.domain.auth.service.dto.AuthServiceResponse;
import dev.glory.demo.domain.auth.service.dto.RegisterServiceRequest;
import dev.glory.demo.domain.user.User;
import dev.glory.demo.domain.user.repository.UserRepository;
import dev.glory.demo.system.config.security.jwt.JwtService;
import dev.glory.demo.system.config.security.jwt.exception.CustomJwtException;
import dev.glory.demo.system.config.security.jwt.exception.TokenCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager  authenticationManager;
    private final JwtService             jwtService;
    private final UserRepository         userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder        passwordEncoder;


    @Transactional
    public AuthServiceResponse register(RegisterServiceRequest registerRequest) {

        boolean exists = userRepository.existsByUsername(registerRequest.username());
        if (exists) {
            throw new BizRuntimeException(AuthErrorCode.EXISTING_USER);
        }

        User user = User.builder()
                .username(registerRequest.username())
                .password(passwordEncoder.encode(registerRequest.password()))
                .name(registerRequest.name())
                .email(registerRequest.email())
                .enabled(true)
                .locked(false)
                .role(registerRequest.role())
                .build();
        userRepository.save(user);

        return getAuthenticationResponse(user);
    }

    @Transactional
    public AuthServiceResponse authenticate(AuthServiceRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = userRepository.findByUsername(request.username()).orElseThrow();

        return getAuthenticationResponse(user);
    }

    @Transactional
    public AuthServiceResponse validateRefreshToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomJwtException(TokenCode.REFRESH_TOKEN_NOT_FOUND));

        // 만료된 토큰이면 삭제
        if (token.getExpireDt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.deleteByUser(token.getUser());
            throw new CustomJwtException(TokenCode.REFRESH_TOKEN_EXPIRED);
        }

        User user = userRepository.findByUsername(token.getUser().getUsername()).orElseThrow();

        return getAuthenticationResponse(user);
    }

    @Transactional
    public void deleteRefreshToken(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        refreshTokenRepository.deleteByUser(user);
    }

    private AuthServiceResponse getAuthenticationResponse(User user) {

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        return new AuthServiceResponse(accessToken, refreshToken);
    }

}
