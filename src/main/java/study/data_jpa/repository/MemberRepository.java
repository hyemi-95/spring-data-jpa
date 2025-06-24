package study.data_jpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> , MemberRepositoryCustom{

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

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //dto조회하기
    @Query("select new study.data_jpa.dto.MemberDto(m.id, m.username, t.name) from Member m join  m.team t")
    List<MemberDto> findMemberDto();

    //컬렉션 파라미터 바인딩
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    //반환타입 start
    List<Member> findListByUsername(String username);//컬렉션
    Member findMemberByUsername(String username);//단건
    Optional<Member> findOpionalByUsername(String username);//단건 옵셔널
    //반환타입 end

    //페이징 및 정렬
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m")//count 쿼리를 분리할 수 있음(복잡한 sql에서 사용)
    Page<Member> findByAge(int name, Pageable pageable); //totalCount쿼리까지 함께해줌
//    Slice<Member> findByAge(int name, Pageable pageable); //totalCount쿼리는 없음
//    List<Member> findByAge(int name, Pageable pageable); //List만 불러와짐

    //벌크성 수정
    @Modifying (clearAutomatically = true)// executeUpdate를 시켜줌 / 쿼리 실행 후 영속성 컨텍스트를 자동으로 초기화해서 동기화 문제 방지를 위함
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    //EntityGraph 패치조인 없이 한번에 가지고 오는 방법
    //사실상 페치 조인(FETCH JOIN)의 간편 버전
    //LEFT OUTER JOIN 사용
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //JPQL + 엔티티 그래프
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //메서드 이름으로 쿼리에서 특히 편리하다.
    @EntityGraph(attributePaths = {"team"})
//    @EntityGraph("Member.all") //-> 엔티티에 NamedEntityGraph를 적용했을 때
    List<Member> findEntityGraphByUsername(String username);

    //EntityGraph end

    //jpa 힌트
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true")) //조회 성능이 향상되긴 하나 엄처나지 않기때문에 성능 테스트 후 필요 시 사용하는걸로
    Member findReadOnlyByUsername(String username);

    @QueryHints(value = {@QueryHint(name = "org.hibernate.readOnly", value = "true")}, forCounting = true)
    Page<Member> findByUsername(String name, Pageable pageable);


    //Lock
//    @Lock(LockModeType.PESSIMISTIC_WRITE) //	동시에 수정할 수 있는 위험이 있는 민감한 데이터 처리 시
//    List<Member> findByUsername(String name);

}
