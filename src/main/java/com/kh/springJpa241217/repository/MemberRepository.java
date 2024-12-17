package com.kh.springJpa241217.repository;

import com.kh.springJpa241217.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // 스프링 컨테이너에 등록해줌 -> 이 객체가 싱글톤으로 등록됨 - 단 하나의 객체만 만들어서 대여(?)의 목적으로...
public interface MemberRepository extends JpaRepository<Member, Long> { // CRUD 관련 구문 여기에 만들어줌 like DAO
    // 기본적인 CRUD는 이미 만들어져 있음
    boolean existsByEmail(String email); // 이메일이 존재해? SELECT * FROM MEMBER WHERE email = ? ;
    Optional<Member> findByEmail(String email); // 이메일을 이용해서 해당하는 객체를 반환. null일때를 위해 Optional
    Optional<Member> findByPwd(String pwd);
    Optional<Member> findByEmailAndPwd(String email, String pwd); // WHERE문 2개인거임
}
