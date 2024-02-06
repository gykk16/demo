package dev.glory.demo.domain.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import dev.glory.demo.IntegratedTestSupport;
import dev.glory.demo.domain.auth.repository.RefreshTokenRepository;
import dev.glory.demo.domain.user.User;
import dev.glory.demo.domain.user.role.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserRepositoryTest extends IntegratedTestSupport {

    @Autowired
    private UserRepository         userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @AfterEach
    void tearDown() {
        refreshTokenRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("username 으로 User 조회 한다")
    @Test
    void when_findByUsername_expect_User() throws Exception {
        // given
        String username = setUp();

        // when
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // then
        assertThat(user.getUsername()).isEqualTo(username);
    }

    @DisplayName("username 으로 User 존재 여부를 확인 한다")
    @Test
    void when_existsByUsername_expect_Boolean() throws Exception {
        // given
        String username = setUp();

        // when
        boolean exists = userRepository.existsByUsername(username);

        // then
        assertThat(exists).isTrue();
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