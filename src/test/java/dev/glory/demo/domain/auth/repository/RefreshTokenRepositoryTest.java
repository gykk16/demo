package dev.glory.demo.domain.auth.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.UUID;

import dev.glory.demo.IntegratedTestSupport;
import dev.glory.demo.domain.auth.RefreshToken;
import dev.glory.demo.domain.user.User;
import dev.glory.demo.domain.user.repository.UserRepository;
import dev.glory.demo.domain.user.role.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RefreshTokenRepositoryTest extends IntegratedTestSupport {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository         userRepository;

    private void createRefreshToken(User user, String refreshToken) {
        RefreshToken token = RefreshToken.builder()
                .user(user)
                .refreshToken(refreshToken)
                .expireDt(LocalDateTime.now().plusMinutes(30))
                .build();
        refreshTokenRepository.save(token);
    }

    @AfterEach
    void tearDown() {
        refreshTokenRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("User 으로 RefreshToken 조회 한다")
    @Test
    void when_findByUser_expect_token() throws Exception {
        // given
        String username = setUp();
        User user = userRepository.findByUsername(username).orElseThrow();
        String refreshToken = UUID.randomUUID().toString();

        createRefreshToken(user, refreshToken);

        // when
        RefreshToken findToken = refreshTokenRepository.findByUser(user).orElseThrow();

        // then
        assertThat(findToken.getRefreshToken()).isEqualTo(refreshToken);
        assertThat(findToken.getUser().getUsername()).isEqualTo(user.getUsername());
    }

    @DisplayName("refresh token 으로 RefreshToken 조회 한다")
    @Test
    void when_findByRefreshToken_expect_token() throws Exception {
        // given
        String username = setUp();
        User user = userRepository.findByUsername(username).orElseThrow();
        String refreshToken = UUID.randomUUID().toString();

        createRefreshToken(user, refreshToken);

        // when
        RefreshToken findToken = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow();

        // then
        assertThat(findToken.getRefreshToken()).isEqualTo(refreshToken);
        assertThat(findToken.getUser().getUsername()).isEqualTo(user.getUsername());
    }

    @DisplayName("User 의 RefreshToken 을 삭제 한다")
    @Test
    void when_deleteByUser_expect_deleteToken() throws Exception {
        // given
        String username = setUp();
        User user = userRepository.findByUsername(username).orElseThrow();
        String refreshToken = UUID.randomUUID().toString();

        createRefreshToken(user, refreshToken);

        // when
        long deleted = refreshTokenRepository.deleteByUser(user);

        // then
        assertThat(deleted).isOne();
        assertThat(refreshTokenRepository.findByRefreshToken(refreshToken)).isEmpty();
    }

    private String setUp() {
        String username = UUID.randomUUID().toString().substring(0, 8);
        User user = User.builder()
                .username(username)
                .password("mypassword123!@#")
                .name("test")
                .email("test@abc.com")
                .enabled(true)
                .locked(false)
                .role(Role.SUPER)
                .build();
        userRepository.save(user);
        return username;
    }

}