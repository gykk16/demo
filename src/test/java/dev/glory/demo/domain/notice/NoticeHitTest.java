package dev.glory.demo.domain.notice;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.glory.demo.IntegratedTestSupport;
import dev.glory.demo.domain.notice.repository.NoticeHitRepository;
import dev.glory.demo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

class NoticeHitTest extends IntegratedTestSupport {

    @Autowired
    private NoticeHitRepository noticeHitRepository;
    @Autowired
    private UserRepository      userRepository;

    @AfterEach
    void tearDown() {
        noticeHitRepository.deleteAllInBatch();
    }

    @DisplayName("사용자 공지 조회는 1회만 기록하고 중복 기록시 예외를 던진다")
    @Test
    void when_usernameAndNoticeIdIsDuplicate_throw_Exception() throws Exception {
        // given
        long noticeId = 1L;
        String username = "test_user";

        NoticeHit noticeHit = new NoticeHit(noticeId, username);
        noticeHitRepository.save(noticeHit);

        // when
        NoticeHit duplicateNoticeHit = new NoticeHit(noticeId, username);
        assertThatThrownBy(() -> noticeHitRepository.save(duplicateNoticeHit))
                // then
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}