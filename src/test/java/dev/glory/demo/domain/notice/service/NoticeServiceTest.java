package dev.glory.demo.domain.notice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dev.glory.demo.IntegratedTestSupport;
import dev.glory.demo.domain.notice.Notice;
import dev.glory.demo.domain.notice.NoticeContent;
import dev.glory.demo.domain.notice.query.NoticeTitleDto;
import dev.glory.demo.domain.notice.repository.NoticeContentRepository;
import dev.glory.demo.domain.notice.repository.NoticeRepository;
import dev.glory.demo.domain.notice.service.dto.NoticeDto;
import dev.glory.demo.domain.notice.service.dto.NoticeServiceRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class NoticeServiceTest extends IntegratedTestSupport {

    @Autowired
    private NoticeService           noticeService;
    @Autowired
    private NoticeRepository        noticeRepository;
    @Autowired
    private NoticeContentRepository noticeContentRepository;

    @AfterEach
    void tearDown() {
        noticeContentRepository.deleteAllInBatch();
        noticeRepository.deleteAllInBatch();
    }

    @DisplayName("공지 목록을 페이징 하여 조회 한다")
    @Test
    void when_findNoticeList_expect_Page() throws Exception {
        // given
        final int TOTAL = 20;
        final int PAGE_SIZE = 4;

        setup(TOTAL);

        // when
        Page<NoticeTitleDto> noticePage = noticeService.findNoticeList(Pageable.ofSize(PAGE_SIZE));

        // then
        assertThat(noticePage).hasSize(PAGE_SIZE);
        assertThat(noticePage.getTotalElements()).isEqualTo(TOTAL);
        assertThat(noticePage.getTotalPages()).isEqualTo(TOTAL / PAGE_SIZE);
    }

    @DisplayName("id 로 공지를 조회 한다")
    @Test
    void when_findContent_expect_NoticeDto() throws Exception {
        // given
        Notice notice = new Notice("title_");
        NoticeContent noticeContent = new NoticeContent("content_", notice);
        noticeRepository.save(notice);
        noticeContentRepository.save(noticeContent);

        // when
        NoticeDto noticeDto = noticeService.findContent(notice.getId());

        // then
        assertThat(noticeDto)
                .extracting(NoticeDto::noticeId, NoticeDto::title, NoticeDto::content, NoticeDto::regId)
                .contains(notice.getId(), notice.getTitle(), noticeContent.getContent(), notice.getRegId());
    }

    @DisplayName("신규 공지를 등록 한다")
    @Test
    void when_save_expect_NewNotice() throws Exception {
        // given
        String title = "title";
        String content = "공지 내용";
        LocalDateTime beginDt = LocalDateTime.now();
        LocalDateTime endDt = beginDt.plusHours(5);
        NoticeServiceRequest serviceRequest = new NoticeServiceRequest(title, content, beginDt, endDt);

        // when
        Long saved = noticeService.save(serviceRequest);

        // then
        NoticeContent noticeContent = noticeContentRepository.findByNoticeId(saved).orElseThrow();
        Notice notice = noticeContent.getNotice();
        assertThat(noticeContent.getContent()).isEqualTo(content);
        assertThat(notice.getTitle()).isEqualTo(title);
    }

    @DisplayName("변경 사항에 대해 공지를 수정 한다")
    @Test
    void when_patch_expect_PatchNotice() throws Exception {
        // given
        Notice notice = new Notice("title_");
        NoticeContent noticeContent = new NoticeContent("content_", notice);
        noticeRepository.save(notice);
        noticeContentRepository.save(noticeContent);

        String titleN = "새로운 제목";
        NoticeServiceRequest serviceRequest = new NoticeServiceRequest(titleN, null, null, null);

        // when
        NoticeDto patched = noticeService.patch(notice.getId(), serviceRequest, "unknown");

        // then
        assertThat(patched.title()).isEqualTo(titleN);
    }

    @DisplayName("공지를 삭제 한다")
    @Test
    void when_delete_expect_DeleteNotice() throws Exception {
        // given
        Notice notice = new Notice("title_");
        NoticeContent noticeContent = new NoticeContent("content_", notice);
        noticeRepository.save(notice);
        noticeContentRepository.save(noticeContent);

        // when
        noticeService.delete(notice.getId(), "unknown");

        // then
        assertThatCode(() -> noticeRepository.findById(notice.getId())).isNull();
    }

    private void setup(int total) {
        List<Notice> notices = new ArrayList<>();
        List<NoticeContent> contents = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            Notice notice = new Notice("title_" + i);
            contents.add(new NoticeContent("content_" + i, notice));
            notices.add(notice);
        }
        noticeRepository.saveAll(notices);
        noticeContentRepository.saveAll(contents);
    }

}