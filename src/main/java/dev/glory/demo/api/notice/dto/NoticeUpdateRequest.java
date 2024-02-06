package dev.glory.demo.api.notice.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Size;

import dev.glory.demo.domain.notice.service.dto.NoticeServiceRequest;

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
