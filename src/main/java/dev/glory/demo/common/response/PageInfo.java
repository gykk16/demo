package dev.glory.demo.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;

/**
 * 표준 Paging 정보 응답
 *
 * @param totalPages    전체 페이지 수
 * @param totalElements 전체 데이터 수
 * @param pageNumber    현재 페이지 번호 (0 부터 시작)
 * @param pageElements  현재 페이지의 데이터 수
 * @param first         첫 페이지 여부
 * @param last          마지막 페이지 여부
 * @param empty         데이터 존재 여부
 */
public record PageInfo(
        @JsonProperty("totalPages")
        int totalPages,
        @JsonProperty("totalElements")
        long totalElements,
        @JsonProperty("pageNumber")
        int pageNumber,
        @JsonProperty("pageElements")
        int pageElements,
        @JsonProperty("first")
        boolean first,
        @JsonProperty("last")
        boolean last,
        @JsonProperty("empty")
        boolean empty
) {

    public PageInfo(Page<?> page) {
        this(page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber(),
                page.getNumberOfElements(),
                page.isFirst(),
                page.isLast(),
                page.isEmpty());
    }
}
