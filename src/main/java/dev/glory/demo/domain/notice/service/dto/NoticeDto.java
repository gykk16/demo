package dev.glory.demo.domain.notice.service.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public record NoticeDto(
        @JsonProperty("noticeId")
        long noticeId,
        @JsonProperty("title")
        String title,
        @JsonProperty("content")
        String content,
        @JsonProperty("hit")
        long hit,
        @JsonProperty("regId")
        String regId,
        @JsonProperty("regDt")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime regDt
) {

}
