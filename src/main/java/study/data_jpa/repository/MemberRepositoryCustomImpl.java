package study.data_jpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import study.data_jpa.entity.Member;

import java.util.List;


//사용자 정의 구현 클래스**
//        규칙: 리포지토리 인터페이스 이름 + `Impl` 또는 (부트2.0이상) 사용자 정의 인터페이스 명 + `Impl`
//        스프링 데이터 JPA가 인식해서 스프링 빈으로 등록
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

    public  final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m").getResultList();
    }
}
