package dev.glory.demo.domain.notice.repository;

import java.util.Optional;

import dev.glory.demo.domain.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<Notice> findByIdAndRegId(Long id, String regId);

}