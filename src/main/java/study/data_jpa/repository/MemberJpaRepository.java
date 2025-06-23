package study.data_jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import study.data_jpa.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext //스프링이 EntityManager를 알아서 주입해주도록 만드는 어노테이션
    private EntityManager em;

    public Member save(Member member){ //저장
        em.persist(member);
        return member;
    }

    public void delete(Member member) { //삭제
        em.remove(member);
    }
    public List<Member> findAll() { //모두찾기
        return em.createQuery("select m from Member m", Member.class) 
                .getResultList();//list로 반환할 때
    }
    public Optional<Member> findById(Long id) { //Optional 타입으로 조회하기
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member); //null일 수도 있다
    }

    public long count() { // 갯수
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();//결과를 하나만 반환
    }

    public Member find(Long id){//조회
        return em.find(Member.class, id);
    }


    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }
    public List<Member> findByUsername(String username) {
        List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
                        .setParameter("username", username)
                        .getResultList();
        return resultList;
    }

}
