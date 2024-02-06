package dev.glory.demo.domain.notice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import dev.glory.demo.domain.notice.Notice;
import dev.glory.demo.domain.notice.NoticeHit;
import dev.glory.demo.domain.notice.query.NoticeHitQueryRepository;
import dev.glory.demo.domain.notice.query.NoticeHitStatDto;
import dev.glory.demo.domain.notice.repository.NoticeHitRepository;
import dev.glory.demo.domain.notice.repository.NoticeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeHitService {

    private final NoticeHitRepository      noticeHitRepository;
    private final NoticeHitQueryRepository noticeHitQueryRepository;
    private final NoticeRepository         noticeRepository;

    /**
     * 공지 조회 내역 저장
     * <ul>
     *     <li>중복 조회는 저장하지 않는다</li>
     * </ul>
     *
     * @param noticeId 공지 id
     * @param username 사용자 id
     */
    @Transactional
    public void saveHit(long noticeId, String username) {
        NoticeHit noticeHit = new NoticeHit(noticeId, username);
        try {
            noticeHitRepository.saveAndFlush(noticeHit);
        } catch (DataIntegrityViolationException e) {
            log.warn("==> DataIntegrityViolationException :: {}", e.getMessage());
        }
    }

    /**
     * 공지 조회 수 동기화
     */
    @Scheduled(fixedDelay = 1_000 * 10, timeUnit = TimeUnit.MILLISECONDS)
    @Transactional
    public void applyHit() {
        final int HIT_REFRESH_INTERVAL_MIN = 5;
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusMinutes(HIT_REFRESH_INTERVAL_MIN);

        List<NoticeHitStatDto> noticeHitStatDtos = noticeHitQueryRepository.fetchNoticeHitStat(start, end);

        for (NoticeHitStatDto hitStat : noticeHitStatDtos) {
            Optional<Notice> noticeOptional = noticeRepository.findById(hitStat.getNoticeId());
            if (noticeOptional.isEmpty()) {
                continue;
            }

            Notice notice = noticeOptional.get();
            notice.updateHit(hitStat.getHits());
            noticeHitQueryRepository.updateAppliedTrue(hitStat.getNoticeId(), start, end);
        }
    }

}
