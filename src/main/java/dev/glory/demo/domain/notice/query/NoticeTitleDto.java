package dev.glory.demo.domain.notice.query;

import java.time.LocalDateTime;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.querydsl.core.annotations.QueryProjection;

@Getter
public class NoticeTitleDto {

    @JsonProperty("noticeId")
    private long noticeId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("hit")
    private long hit;

    @JsonProperty("regId")
    private String regId;

    @JsonProperty("regDt")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime regDt;

    @QueryProjection
    public NoticeTitleDto(long noticeId, String title, long hit, String regId, LocalDateTime regDt) {
        this.noticeId = noticeId;
        this.title = title;
        this.hit = hit;
        this.regId = regId;
        this.regDt = regDt;
    }

}
