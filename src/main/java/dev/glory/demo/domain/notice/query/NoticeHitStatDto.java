package dev.glory.demo.domain.notice.query;

import lombok.Getter;

import com.querydsl.core.annotations.QueryProjection;

@Getter
public class NoticeHitStatDto {

    private long noticeId;
    private long hits;

    @QueryProjection
    public NoticeHitStatDto(long noticeId, long hits) {
        this.noticeId = noticeId;
        this.hits = hits;
    }

}
