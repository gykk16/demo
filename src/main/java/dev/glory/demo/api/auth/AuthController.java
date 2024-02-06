package dev.glory.demo.api.auth;

import static dev.glory.demo.common.code.SuccessCode.CREATED;
import static dev.glory.demo.common.code.SuccessCode.SUCCESS;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import dev.glory.demo.api.auth.dto.AuthRequest;
import dev.glory.demo.api.auth.dto.AuthResponse;
import dev.glory.demo.api.auth.dto.RefreshTokenRequest;
import dev.glory.demo.api.auth.dto.RegisterRequest;
import dev.glory.demo.common.response.ApiResponseEntity;
import dev.glory.demo.domain.auth.service.AuthService;
import dev.glory.demo.domain.auth.service.dto.AuthServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * 등록
     *
     * @param requestDTO 등록 요청
     * @return 인증 토큰 정보
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponseEntity<AuthResponse>> register(
            @Validated @RequestBody RegisterRequest requestDTO) {

        AuthServiceResponse serviceResponse = authService.register(requestDTO.toServiceRequest());
        return ApiResponseEntity.of(CREATED, "", AuthResponse.from(serviceResponse));
    }

    /**
     * 인증
     *
     * @param requestDTO 인증 요청
     * @return 인증 토큰 정보
     */
    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponseEntity<AuthResponse>> authenticate(
            @Validated @RequestBody AuthRequest requestDTO) {

        AuthServiceResponse serviceResponse = authService.authenticate(requestDTO.toServiceRequest());
        return ApiResponseEntity.of(SUCCESS, AuthResponse.from(serviceResponse));
    }

    /**
     * 토큰 재발급
     *
     * @param requestDTO 토큰 재발급 요청
     * @return 인증 토큰 정보
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponseEntity<AuthResponse>> refreshToken(
            @Validated @RequestBody RefreshTokenRequest requestDTO) {

        AuthServiceResponse serviceResponse = authService.validateRefreshToken(requestDTO.refreshToken());
        return ApiResponseEntity.of(SUCCESS, AuthResponse.from(serviceResponse));
    }

    /**
     * Refresh Token 삭제
     *
     * @param username 사용자 noticeId
     * @return 성공
     */
    @DeleteMapping("/refresh-token/{username}")
    @PreAuthorize("hasRole('SUPER')")
    public ResponseEntity<ApiResponseEntity<Void>> deleteRefreshToken(@PathVariable String username) {

        authService.deleteRefreshToken(username);
        return ApiResponseEntity.ofSuccess();
    }

}
