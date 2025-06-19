package study.data_jpa.repository;

import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.data_jpa.entity.Member;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

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

}