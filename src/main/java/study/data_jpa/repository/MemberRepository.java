package study.data_jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.data_jpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //1.
    // 스프링 데이터 JPA가 제공하는 '쿼리 메소드'기능
    // 엔티티의 필드명이 변경되면 인터페이스에 정의한 메서드 이름도 꼭 함께 변경
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
    List<Member> findTop3HelloBy();
    //쿼리 메소드 끝

    //2.
    // NamedQuery는 실무에서 거의 사용 안함
    //@Query(name = "Member.findByUsername") //-->주석 걸어도 쓸 수 있음 왜냐 ? 아래의 findByUsername에대해 Member에 NamedQuery가 있는지 확인 먼저 하게됨.
    //Named 쿼리가 없으면 메서드 이름으로 쿼리 생성 전략을 사용
    List<Member> findByUsername(@Param("username") String username);
    //NamedQuery 끝

    //3.
    // 리포지토리 메소드에 쿼리 정의하기
    //JPQL을 바로 삽입해서 사용
    @Query("select m from Member m where m.username =:username and m.age =:age")//오타가 있어도 애플리케이션 로딩 시 오류가 발생
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

}
