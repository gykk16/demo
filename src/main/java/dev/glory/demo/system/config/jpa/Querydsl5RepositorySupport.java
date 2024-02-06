package dev.glory.demo.system.config.jpa;

import java.util.List;
import java.util.function.Function;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * Querydsl5RepositorySupport
 * <p>
 * Querydsl 5.x 버전을 사용하기 위한 RepositorySupport
 */
@Repository
public abstract class Querydsl5RepositorySupport {

    private final Class<?>        domainClass;
    private       Querydsl        querydsl;
    private       EntityManager   entityManager;
    private       JPAQueryFactory queryFactory;

    protected Querydsl5RepositorySupport(Class<?> domainClass) {
        Assert.notNull(domainClass, "Domain class must not be null!");
        this.domainClass = domainClass;
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager, JPAQueryFactory jpaQueryFactory) {
        Assert.notNull(entityManager, "EntityManager must not be null!");

        var entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager);
        var resolver = SimpleEntityPathResolver.INSTANCE;
        var path = resolver.createPath(entityInformation.getJavaType());

        this.entityManager = entityManager;
        this.queryFactory = jpaQueryFactory;
        this.querydsl = new Querydsl(entityManager, new PathBuilder<>(path.getType(), path.getMetadata()));
    }

    @PostConstruct
    public void validate() {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        Assert.notNull(querydsl, "Querydsl must not be null!");
        Assert.notNull(queryFactory, "QueryFactory must not be null!");
    }

    protected JPAQueryFactory getQueryFactory() {
        return queryFactory;
    }

    protected Querydsl getQuerydsl() {
        return querydsl;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * 조회
     *
     * @param expr 조회 표현식
     * @param <T>  조회 타입
     * @return JPAQuery
     */
    protected <T> JPAQuery<T> select(Expression<T> expr) {
        return getQueryFactory().select(expr);
    }

    /**
     * 조회
     *
     * @param from 조회 대상
     * @param <T>  조회 타입
     * @return JPAQuery
     */
    protected <T> JPAQuery<T> selectFrom(EntityPath<T> from) {
        return getQueryFactory().selectFrom(from);
    }

    /**
     * 페이징 조회
     *
     * @param pageable     pageable
     * @param contentQuery 조회 쿼리
     * @param countQuery   count 쿼리
     * @param <T>          조회 타입
     * @return 페이징 처리 결과
     */
    protected <T> Page<T> applyPagination(Pageable pageable,
            Function<JPAQueryFactory, JPAQuery<T>> contentQuery,
            Function<JPAQueryFactory, JPAQuery<Long>> countQuery) {

        JPAQuery<T> jpaContentQuery = contentQuery.apply(getQueryFactory());
        List<T> content = getQuerydsl().applyPagination(pageable, jpaContentQuery).fetch();

        JPAQuery<Long> jpaCountQuery = countQuery.apply(getQueryFactory());

        return PageableExecutionUtils.getPage(content, pageable, jpaCountQuery::fetchOne);
    }

}
