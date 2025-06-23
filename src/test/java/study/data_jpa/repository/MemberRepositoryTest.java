package study.data_jpa.repository;

import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
}