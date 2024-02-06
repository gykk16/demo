package dev.glory.demo.domain.notice.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import java.util.List;

import dev.glory.demo.IntegratedTestSupport;
import dev.glory.demo.domain.notice.NoticeHit;
import dev.glory.demo.domain.notice.repository.NoticeHitRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class NoticeHitQueryRepositoryTest extends IntegratedTestSupport {

    @Autowired
    private NoticeHitQueryRepository noticeHitQueryRepository;
    @Autowired
    private NoticeHitRepository      noticeHitRepository;

    @AfterEach
    void tearDown() {
        noticeHitRepository.deleteAllInBatch();
    }

    @DisplayName("fetchNoticeHitStat")
    @Test
    void fetchNoticeHitStat() throws Exception {
        // given
        NoticeHit noticeHit1 = new NoticeHit(1L, "user1");
        NoticeHit noticeHit2 = new NoticeHit(1L, "user2");
        NoticeHit noticeHit3 = new NoticeHit(1L, "user3");
        NoticeHit noticeHit4 = new NoticeHit(2L, "user1");
        noticeHitRepository.saveAll(List.of(noticeHit1, noticeHit2, noticeHit3, noticeHit4));

        LocalDateTime start = LocalDateTime.now().minusMinutes(10);
        LocalDateTime end = LocalDateTime.now();

        // when
        List<NoticeHitStatDto> noticeHitStatDtos = noticeHitQueryRepository.fetchNoticeHitStat(start, end);

        // then
        assertThat(noticeHitStatDtos).hasSize(2)
                .extracting("noticeId", "hits")
                .contains(tuple(1L, 3L),
                        tuple(2L, 1L)
                );
    }

    @DisplayName("updateAppliedTrue")
    @Test
    void updateAppliedTrue() throws Exception {
        // given
        NoticeHit noticeHit1 = new NoticeHit(1L, "user1");
        NoticeHit noticeHit2 = new NoticeHit(1L, "user2");
        NoticeHit noticeHit3 = new NoticeHit(1L, "user3");
        NoticeHit noticeHit4 = new NoticeHit(2L, "user1");
        noticeHitRepository.saveAll(List.of(noticeHit1, noticeHit2, noticeHit3, noticeHit4));

        LocalDateTime start = LocalDateTime.now().minusMinutes(10);
        LocalDateTime end = LocalDateTime.now();

        assertThat(noticeHitQueryRepository.fetchNoticeHitStat(start, end)).hasSize(2);

        // when
        long updated = noticeHitQueryRepository.updateAppliedTrue(1L, start, end);

        // then
        assertThat(updated).isEqualTo(3L);
        assertThat(noticeHitQueryRepository.fetchNoticeHitStat(start, end)).hasSize(1);
    }

}