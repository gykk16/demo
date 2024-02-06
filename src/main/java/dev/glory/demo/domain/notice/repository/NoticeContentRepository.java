package dev.glory.demo.domain.notice.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import dev.glory.demo.domain.notice.NoticeContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeContentRepository extends JpaRepository<NoticeContent, Long> {

    @Query("""
            select nc
              from NoticeContent nc
              join fetch nc.notice n
             where n.id = :id
               and n.valid = true
            """)
    Optional<NoticeContent> findByNoticeId(@Param("id") Long id);

    @Query("""
            select nc
              from NoticeContent nc
              join fetch nc.notice n
             where n.id = :id
               and n.valid = true
               and n.beginDt <= :targetDt
               and n.endDt >= :targetDt
            """)
    Optional<NoticeContent> findByNoticeId(@Param("id") Long id, @Param("targetDt") LocalDateTime targetDt);

}