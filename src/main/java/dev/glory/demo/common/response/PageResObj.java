package dev.glory.demo.common.response;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;

/**
 * 표준 Paging 응답 객체
 *
 * @param content  데이터 목록
 * @param pageInfo 페이징 정보
 * @param <T>      데이터 타입
 */
public record PageResObj<T>(
        @JsonProperty("content")
        Collection<T> content,
        @JsonProperty("pageInfo")
        PageInfo pageInfo
) {

    /**
     * 표준 Paging 응답 객체 생성
     *
     * @param page 페이징 객체
     */
    public PageResObj(Page<T> page) {
        this(page.getContent(), new PageInfo(page));
    }

}
