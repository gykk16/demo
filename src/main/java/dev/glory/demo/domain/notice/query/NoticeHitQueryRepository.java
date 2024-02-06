package dev.glory.demo.domain.notice.query;

import static dev.glory.demo.domain.notice.QNoticeHit.noticeHit;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class NoticeHitQueryRepository {

    private final JPAQueryFactory factory;

    public List<NoticeHitStatDto> fetchNoticeHitStat(LocalDateTime start, LocalDateTime end) {

        return factory
                .select(new QNoticeHitStatDto(
                        noticeHit.noticeId,
                        noticeHit.username.count())
                )
                .from(noticeHit)
                .where(noticeHit.applied.isFalse(),
                        noticeHit.regDt.between(start, end)
                )
                .groupBy(noticeHit.noticeId)
                .fetch();
    }

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    public long updateAppliedTrue(long noticeId, LocalDateTime start, LocalDateTime end) {

        return factory
                .update(noticeHit)
                .set(noticeHit.applied, true)
                .where(noticeHit.noticeId.eq(noticeId),
                        noticeHit.applied.isFalse(),
                        noticeHit.regDt.between(start, end)
                )
                .execute();
    }

}
