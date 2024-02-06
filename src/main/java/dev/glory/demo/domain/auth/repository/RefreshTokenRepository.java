package dev.glory.demo.domain.auth.repository;

import java.util.Optional;

import dev.glory.demo.domain.auth.RefreshToken;
import dev.glory.demo.domain.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @EntityGraph(attributePaths = "user")
    Optional<RefreshToken> findByUser(User user);

    @EntityGraph(attributePaths = "user")
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    @Transactional
    long deleteByUser(User user);

}