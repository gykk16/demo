package dev.glory.demo.domain.notice.service;

import static dev.glory.demo.common.constant.WebAppConst.getMdcAccessId;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

import dev.glory.demo.common.exception.BizRuntimeException;
import dev.glory.demo.domain.notice.Notice;
import dev.glory.demo.domain.notice.NoticeContent;
import dev.glory.demo.domain.notice.NoticeErrorCode;
import dev.glory.demo.domain.notice.query.NoticeQueryRepository;
import dev.glory.demo.domain.notice.query.NoticeTitleDto;
import dev.glory.demo.domain.notice.repository.NoticeContentRepository;
import dev.glory.demo.domain.notice.repository.NoticeRepository;
import dev.glory.demo.domain.notice.service.dto.NoticeDto;
import dev.glory.demo.domain.notice.service.dto.NoticeServiceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository        noticeRepository;
    private final NoticeContentRepository noticeContentRepository;
    private final NoticeQueryRepository   noticeQueryRepository;
    private final NoticeHitService        noticeHitService;

    public Page<NoticeTitleDto> findNoticeList(Pageable pageable) {
        return noticeQueryRepository.fetchNoticeList(pageable);
    }

    public NoticeDto findContent(long noticeId) {
        NoticeContent noticeContent = noticeContentRepository.findByNoticeId(noticeId, LocalDateTime.now())
                .orElseThrow(() -> new BizRuntimeException(NoticeErrorCode.NOT_FOUND));
        Notice notice = noticeContent.getNotice();

        try {
            noticeHitService.saveHit(noticeId, getMdcAccessId().orElse("unknown"));
        } catch (UnexpectedRollbackException e) {
        }

        return new NoticeDto(notice.getId(),
                notice.getTitle(),
                noticeContent.getContent(),
                notice.getHit(),
                notice.getRegId(),
                notice.getRegDt());
    }

    @Transactional
    public Long save(NoticeServiceRequest request) {

        Notice notice = new Notice(request.title(), request.beginDt(), request.endDt());
        NoticeContent noticeContent = new NoticeContent(request.content(), notice);

        noticeRepository.save(notice);
        noticeContentRepository.save(noticeContent);

        return notice.getId();
    }

    @Transactional
    public NoticeDto patch(long noticeId, NoticeServiceRequest request, String username) {
        NoticeContent noticeContent = noticeContentRepository.findByNoticeId(noticeId)
                .filter(nc -> nc.getNotice().getRegId().equals(username))
                .orElseThrow(() -> new BizRuntimeException(NoticeErrorCode.NOT_FOUND));
        Notice notice = noticeContent.getNotice();

        noticeContent.pathContent(request.content());
        notice.patchNotice(request.title(), request.beginDt(), request.endDt());

        return new NoticeDto(notice.getId(),
                notice.getTitle(),
                noticeContent.getContent(),
                notice.getHit(),
                notice.getRegId(),
                notice.getRegDt());
    }

    @Transactional
    public void delete(long noticeId, String username) {
        Notice notice = noticeRepository.findByIdAndRegId(noticeId, username)
                .orElseThrow(() -> new BizRuntimeException(NoticeErrorCode.NOT_FOUND));
        noticeRepository.delete(notice);
    }

}
