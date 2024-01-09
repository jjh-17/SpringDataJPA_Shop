package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //JPA가 조회 부분에서는 성능을 최적화 해준다.
@RequiredArgsConstructor        //생성자 생략 어노테이션
public class MemberService {

    private final MemberRepository memberRepository;

//    회원 가입
//    데이터 변경이 필요한 곳은 @Transactional을 선언하여 readOnly가 아님을 명시한다.
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.saveMember(member);
        return member.getId();
    }

    //중복 회원 방지 로직
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findMemberByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

//    id로 회원 조회
    public Member findMember(Long id) {
        return memberRepository.findMemberById(id);
    }

//    회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findMemberAll();
    }
}
