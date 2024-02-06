package dev.glory.demo.domain.notice.query;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import dev.glory.demo.IntegratedTestSupport;
import dev.glory.demo.domain.notice.Notice;
import dev.glory.demo.domain.notice.repository.NoticeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class NoticeQueryRepositoryTest extends IntegratedTestSupport {

    @Autowired
    private NoticeQueryRepository noticeQueryRepository;
    @Autowired
    private NoticeRepository      noticeRepository;

    @DisplayName("fetchNoticeList")
    @Test
    void fetchNoticeList() throws Exception {
        // given
        List<Notice> notices = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            notices.add(new Notice("title_" + i));
        }
        noticeRepository.saveAll(notices);

        // when
        Page<NoticeTitleDto> noticeTitleDtos = noticeQueryRepository.fetchNoticeList(Pageable.ofSize(5));

        // then
        assertThat(noticeTitleDtos).hasSize(5);
    }

}