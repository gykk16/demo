package dev.glory.demo.domain.notice;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import dev.glory.demo.domain.base.BaseTimeEntity;
import org.hibernate.annotations.Comment;
import org.hibernate.type.YesNoConverter;

/**
 * 공지 조회수
 */
@Entity
@Table(name = "notice_hit",
        uniqueConstraints = {@UniqueConstraint(name = "uk01_notice_hit", columnNames = {"notice_id", "username"})}
)
@SequenceGenerator(
        name = "seq_notice_hit_id_generator",
        sequenceName = "seq_notice_hit_id",
        initialValue = 1,
        allocationSize = 50
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoticeHit extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_notice_hit_id_generator")
    private Long id;

    @Comment("공지 noticeId")
    private long noticeId;

    @Comment("사용자 noticeId")
    private String username;

    @Convert(converter = YesNoConverter.class)
    @Comment("적용 여부 - notice table update 여부")
    private boolean applied;

    public NoticeHit(long noticeId, String username) {
        this.noticeId = noticeId;
        this.username = username;
        this.applied = false;
    }

}
