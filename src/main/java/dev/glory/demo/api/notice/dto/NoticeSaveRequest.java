package dev.glory.demo.api.notice.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import dev.glory.demo.domain.notice.service.dto.NoticeServiceRequest;
import org.springframework.web.multipart.MultipartFile;

public record NoticeSaveRequest(
        @NotBlank @Size(max = 250) String title,
        @NotBlank String content,
        LocalDateTime beginDt,
        LocalDateTime endDt,
        @Size(max = 5) MultipartFile[] file
) {

    public NoticeServiceRequest toServiceRequest() {
        return new NoticeServiceRequest(title.strip(),
                content.strip(),
                beginDt != null ? beginDt : LocalDateTime.now(),
                endDt != null ? endDt : LocalDateTime.now().plusDays(30));
    }

}
