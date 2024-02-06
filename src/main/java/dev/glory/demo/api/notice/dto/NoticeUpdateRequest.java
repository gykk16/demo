package dev.glory.demo.api.notice.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Size;

import dev.glory.demo.domain.notice.service.dto.NoticeServiceRequest;

/**
 * 공지 수정 api spec
 *
 * @param title   제목
 * @param content 내용
 * @param beginDt 개시 시작일시
 * @param endDt   개시 종료일시
 */
public record NoticeUpdateRequest(
        @Size(max = 250) String title,
        String content,
        LocalDateTime beginDt,
        LocalDateTime endDt
) {

    public NoticeServiceRequest toServiceRequest() {
        return new NoticeServiceRequest(title.strip(),
                content.strip(),
                beginDt,
                endDt);
    }

}
