package study.data_jpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;
import study.data_jpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        return memberRepository.findById(id)
                .map(Member::getUsername)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
    }
    //도메인 컨버터
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {//실무에서 안씀 !!!!!! 써도 조회용으로만
        return member.getUsername(); //자동으로 Member 객체로 변환 -> 내부적으로는 memberRepository.findById(id).orElse(null)처럼 동작
    }

    @PostConstruct
    public void init(){
//        memberRepository.save(new Member("userA"));
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user"+i, i));
        }
    }

    //페이징과 정렬
    /**
     * `Pageable` -> 요청 정보 전달 (몇 페이지? 몇 개?)
     * `Page<T>`  -> 응답 정보 + 메타데이터
     * */
    @GetMapping("/members") // ex) /members?page=0&size=3&sort=id,desc&sort=username,desc 호출
    public Page<MemberDto> list( @PageableDefault(size = 12, sort = "username", direction = Sort.Direction.DESC) Pageable pageable) {//페이징 관련 기본값 개별설정
        Page<Member> page = memberRepository.findAll(pageable);
//        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        Page<MemberDto> map = page.map(member -> new MemberDto(member));
        return map;
    }
}