package study.data_jpa.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;
import study.data_jpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.ARRAY;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository; //JPA리포지토리 인터페이스
    @Autowired TeamRepository teamRepository;

    @Autowired EntityManager em;

    @Test
    public void testMember() {
        // given
        Member member = new Member("MemberA");
        Member savedMember = memberRepository.save(member);
//        Optional<Member> findById = memberRepository.findById(savedMember.getId());
        Member findById = memberRepository.findById(savedMember.getId()).get();
        // when
        assertThat(findById.getId()).isEqualTo(member.getId());
        assertThat(findById.getUsername()).isEqualTo(member.getUsername());
        assertThat(findById).isEqualTo(member);//JPA 엔티티 동일성 보장
    }


    @Test
    public void basicCRUD() {

        Member member1 = new Member("MemberA");
        Member member2 = new Member("MemberB");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1.getId()).isEqualTo(member1.getId());
        assertThat(findMember2.getId()).isEqualTo(member2.getId());

        findMember1.setUsername("member!!!!!!");//더티체킹(update나감)

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deleteCount = memberRepository.count();
        assertThat(deleteCount).isEqualTo(0);

    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void findTop3HelloBy(){
        List<Member> findTop3by = memberRepository.findTop3HelloBy();
    }

    @Test
    public void testNamedQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> result = memberRepository.findByUsername("AAA");
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> result = memberRepository.findUser("AAA",10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUserNameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<String> result = memberRepository.findUsernameList();
        for (String member : result) {
            System.out.println("member :"+ member);
        }
    }

    @Test
    public void findMemberDto() { //dto조회
        Team team =new Team("teamA");
        teamRepository.save(team);


        Member m1 = new Member("AAA", 10, team);
//        m1.setTeam(team);
        memberRepository.save(m1);


        List<MemberDto> result = memberRepository.findMemberDto();
        for (MemberDto memberDto : result) {
            System.out.println("member :"+ memberDto);
        }
    }

    @Test
    public void findByNames() { //컬렉션 파라미터 바인딩
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA","BBB"));
        for (Member member : result) {
            System.out.println("member :"+ member);
        }
    }

    @Test
    public void returnType() { //컬렉션 파라미터 바인딩
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findListByUsername("AAA");//컬렉션 -> 없으면 empty컬렉션 반환 []
        Member aaa1 = memberRepository.findMemberByUsername("AAA");//단건 -> 없으면 null
        Optional<Member> aaa2 = memberRepository.findOpionalByUsername("AAA");//단건 옵셔널 -> 없으면 Optional.empty

        System.out.println("aaa ="+aaa);
        System.out.println("aaa1 ="+aaa1);
        System.out.println("aaa2 ="+aaa2);

    }

    @Test
    public void paging() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age=10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
//        Slice<Member> page = memberRepository.findByAge(age, pageRequest);//내가 요청한 페이지사이즈보다 하나 더 가져옴 3개 요청 -> 4개로 요청함
//        List<Member> list = memberRepository.findByAge(age, pageRequest);

        //dto변환 -> API에서 반환할때는 꼭 DTO로 변환해서 반환하자
        Page<MemberDto> map = page.map(m -> new MemberDto(m.getId(), m.getUsername(),null));

        //then
        //List타입은 아래 모두 주석

        List<Member> content = page.getContent();
//
        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);//Slice에는 없음
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);//Slice에는 없음
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void bulkUpdate() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));
        //when
        int resultCount = memberRepository.bulkAgePlus(20);

        //영속성 확인
        List<Member> result = memberRepository.findListByUsername("member5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);
        
        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() throws Exception {
        //given
        //member1 -> teamA
        //member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));
        em.flush();
        em.clear();
        //when
        List<Member> members = memberRepository.findAll();
//        List<Member> members = memberRepository.findMemberFetchJoin();
        //then
        for (Member member : members) {
            member.getTeam().getName();
        }
    }

    @Test
    public void queryHint() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();
        //when
        Member member = memberRepository.findReadOnlyByUsername("member1");
        member.setUsername("member2");
        em.flush(); //Update Query 실행X
    }

    @Test
    void queryHint_readOnly_paging() {
        // given
        memberRepository.save(new Member("user1", 10));
        memberRepository.save(new Member("user1", 20));
        memberRepository.save(new Member("user1", 30));

        em.flush();
        em.clear();

        // when
        Pageable pageable = PageRequest.of(0, 2, Sort.by("age").descending());
        Page<Member> page = memberRepository.findByUsername("user1", pageable);

        // then
        List<Member> content = page.getContent();
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(content.size()).isEqualTo(2);
        assertThat(content.get(0).getAge()).isEqualTo(30);
        assertThat(content.get(1).getAge()).isEqualTo(20);

        // 읽기 전용 상태에서는 변경 감지 (dirty checking)이 되지 않음
        Member readOnlyMember = content.get(0);
        readOnlyMember.setUsername("user999");

        em.flush(); // flush 되더라도 변경이 DB에 반영되지 않음 (readOnly니까)

        // 확인용 출력
        List<Member> check = memberRepository.findByUsername("user999", Pageable.unpaged()).getContent();
        assertThat(check.size()).isEqualTo(0); // username이 바뀌지 않았기 때문에 0명
    }
}