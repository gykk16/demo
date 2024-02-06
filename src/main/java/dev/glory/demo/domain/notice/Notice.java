package dev.glory.demo.domain.notice;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import dev.glory.demo.domain.base.BaseEntity;
import io.micrometer.common.util.StringUtils;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.type.YesNoConverter;
import org.springframework.lang.NonNull;

/**
 * 공지
 */
@Entity
@Table(name = "notice")
@SoftDelete(columnName = "deleted", strategy = SoftDeleteType.DELETED, converter = YesNoConverter.class)
@SequenceGenerator(
        name = "seq_notice_id_generator",
        sequenceName = "seq_notice_id",
        initialValue = 1,
        allocationSize = 50
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_notice_id_generator")
    @Column(name = "noticeId", updatable = false)
    private Long id;

    @Comment("공지 제목")
    private String title;

    @Comment("개시 시작일시")
    private LocalDateTime beginDt;

    @Comment("개시 종료일시")
    private LocalDateTime endDt;

    @Comment("조회 수")
    private long hit;

    @Convert(converter = YesNoConverter.class)
    @Comment("사용 여부")
    private boolean valid;

    public Notice(@NonNull String title) {
        this(title, LocalDateTime.now(), LocalDate.of(9999, 12, 31).atStartOfDay());
    }

    public Notice(@NonNull String title, @NonNull LocalDateTime beginDt, @NonNull LocalDateTime endDt) {
        this.title = title;
        this.beginDt = beginDt;
        this.endDt = endDt;
        this.hit = 0;
        this.valid = true;
    }

    public void updateHit(long hits) {
        this.hit += hits;
    }

    public void patchNotice(String titleN, LocalDateTime beginDtN, LocalDateTime endDtN) {
        this.title = StringUtils.isBlank(titleN) ? title : titleN;
        this.beginDt = beginDtN == null ? beginDt : beginDtN;
        this.endDt = endDtN == null ? endDt : endDtN;
    }

}
