package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor //생성자 생략 어노테이션
public class MemberRepository {
    /*
    //스프링 부트 JPA에서는 '@PersistenceContext'를 '@Autowired'로 대체 가능 ==> '@RequiredArgsConstructor'로 코드 간략화 가능
    @PersistenceContext //엔티티 매니저를 빈으로 주입할 때 사용
    private EntityManager entityManager;
    */

    private final EntityManager entityManager;

    //회원 등록
    public void saveMember(Member member) {
        entityManager.persist(member);
    }

    //ID로 회원 검색
    public Member findMemberById(Long id) {
        return entityManager.find(Member.class, id);
    }

    //이름으로 회원 검색 ==> 이름 중복이 가능하므로 list 형식으로 반환
    public List<Member> findMemberByName(String name) {
        //setParameter : Member 클래스의 name 필드에 인자로 받은 name이 바인딩 된다.
        return entityManager.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name).getResultList();
    }

    //모든 회원 검색
    public List<Member> findMemberAll() {
        return entityManager.createQuery("select m from Member m", Member.class)
                .getResultList();
    }


}
