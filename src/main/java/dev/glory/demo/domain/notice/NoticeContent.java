package dev.glory.demo.domain.notice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import dev.glory.demo.domain.base.BaseTimeEntity;
import io.micrometer.common.util.StringUtils;
import org.hibernate.annotations.Comment;

/**
 * 공지 내용
 */
@Entity
@Table(name = "notice_content")
@SequenceGenerator(
        name = "seq_notice_content_id_generator",
        sequenceName = "seq_notice_content_id",
        initialValue = 1,
        allocationSize = 50
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoticeContent extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_notice_content_id_generator")
    @Column(updatable = false)
    private Long id;

    @Lob
    @Column(name = "content", columnDefinition = "CLOB")
    @Comment("공지 내용")
    private String content;

    @ManyToOne
    @JoinColumn(name = "notice_id")
    @ToString.Exclude
    private Notice notice;

    public NoticeContent(String content, Notice notice) {
        this.content = content;
        this.notice = notice;
    }

    public void pathContent(String contentN) {
        this.content = StringUtils.isBlank(contentN) ? content : contentN;
    }

}
