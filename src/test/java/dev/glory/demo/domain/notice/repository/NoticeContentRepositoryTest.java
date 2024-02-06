package dev.glory.demo.domain.notice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import dev.glory.demo.IntegratedTestSupport;
import dev.glory.demo.domain.notice.Notice;
import dev.glory.demo.domain.notice.NoticeContent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class NoticeContentRepositoryTest extends IntegratedTestSupport {

    @Autowired
    private NoticeContentRepository noticeContentRepository;
    @Autowired
    private NoticeRepository        noticeRepository;

    @AfterEach
    void tearDown() {
        noticeContentRepository.deleteAllInBatch();
        noticeRepository.deleteAllInBatch();
    }

    @DisplayName("findByNoticeId 조회 시 Notice 도 한번에 퀴리 한다")
    @Test
    void when_findByNoticeId_expect_FetchNotice() throws Exception {
        // given
        String title = "notice title";
        String content = "content -------";

        Notice notice = new Notice(title);
        NoticeContent noticeContent = new NoticeContent(content, notice);
        noticeRepository.save(notice);
        noticeContentRepository.save(noticeContent);

        // when
        NoticeContent fetchedNoticeContent = noticeContentRepository.findByNoticeId(notice.getId(), LocalDateTime.now())
                .orElseThrow();

        // then
        Notice fetchedNotice = fetchedNoticeContent.getNotice();
        assertThat(fetchedNotice)
                .extracting(Notice::getId, Notice::getTitle, Notice::getHit, Notice::getRegId)
                .contains(notice.getId(), notice.getTitle(), notice.getHit(), notice.getRegId());
    }

}