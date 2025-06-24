package study.data_jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/*  (Spring Data JPA 방식)
 * 작동 방식 -> Spring Data JPA의 Auditing 기능을 활용
 * 어노테이션 -> `@CreatedDate`, `@LastModifiedDate` 사용
 * 필요 설정 -> `@EnableJpaAuditing` 필수
 * 장점    -> 스프링이 직접 감지해서 정확한 시점 기록 (트랜잭션 반영 후)
 * 단점    -> 스프링 환경에 의존함
 * */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;
}
