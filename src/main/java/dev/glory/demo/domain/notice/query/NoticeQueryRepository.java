package dev.glory.demo.domain.notice.query;

import static dev.glory.demo.domain.notice.QNotice.notice;

import java.time.LocalDateTime;

import dev.glory.demo.domain.notice.Notice;
import dev.glory.demo.system.config.jpa.Querydsl5RepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class NoticeQueryRepository extends Querydsl5RepositorySupport {

    protected NoticeQueryRepository() {
        super(Notice.class);
    }

    public Page<NoticeTitleDto> fetchNoticeList(Pageable pageable) {

        LocalDateTime targetDt = LocalDateTime.now();

        return applyPagination(pageable,
                // content
                contentQuery -> contentQuery
                        .select(new QNoticeTitleDto(
                                notice.id,
                                notice.title,
                                notice.hit,
                                notice.regId,
                                notice.regDt)
                        )
                        .from(notice)
                        .where(notice.valid.isTrue(),
                                notice.beginDt.before(targetDt),
                                notice.endDt.after(targetDt)
                        )
                        .orderBy(notice.regDt.desc())
                ,
                // count
                countQuery -> countQuery
                        .select(notice.count())
                        .from(notice)
                        .where(notice.valid.isTrue(),
                                notice.beginDt.before(targetDt),
                                notice.endDt.after(targetDt)
                        )
        );
    }

}
