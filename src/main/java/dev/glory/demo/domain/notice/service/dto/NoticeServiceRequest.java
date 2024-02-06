package dev.glory.demo.domain.notice.service.dto;

import java.time.LocalDateTime;

public record NoticeServiceRequest(
        String title,
        String content,
        LocalDateTime beginDt,
        LocalDateTime endDt
) {

}
