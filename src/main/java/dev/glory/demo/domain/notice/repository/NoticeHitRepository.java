package dev.glory.demo.domain.notice.repository;

import dev.glory.demo.domain.notice.NoticeHit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeHitRepository extends JpaRepository<NoticeHit, Long> {

}