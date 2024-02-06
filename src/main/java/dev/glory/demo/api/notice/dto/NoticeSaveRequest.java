package dev.glory.demo.api.notice.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import dev.glory.demo.domain.notice.service.dto.NoticeServiceRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 * 공지 등록 api spec
 *
 * @param title   제목
 * @param content 내용
 * @param beginDt 개시 시작일시
 * @param endDt   개시 종료일시
 * @param file    등록 파일
 */
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
