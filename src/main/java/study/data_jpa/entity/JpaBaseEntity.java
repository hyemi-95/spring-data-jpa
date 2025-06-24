package study.data_jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;
/**(JPA 순수 방식)

 *  작동 방식 --> JPA의 **Lifecycle 콜백** 기능 사용 |
 *  어노테이션 --> `@PrePersist`, `@PreUpdate` |
 *  필요 설정 --> 별도 설정 없음                    |
 *  장점    --> JPA만 써도 됨 (Spring 없이도 가능)   |
 *  단점    --> 정확한 트랜잭션 타이밍과는 약간 차이날 수 있음  |
 *
 * */



@MappedSuperclass // 자식 테이블 필드로 포함되서 나옴
@Getter
public class JpaBaseEntity {
    @Column(updatable = false) //업데이트 못하도록
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist //엔티티가 처음 저장되기 전 (INSERT 직전)
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate //엔티티가 수정되기 전 (UPDATE 직전)
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}