package com.kh.springJpa241217.service;

import com.kh.springJpa241217.dto.LoginReqDto;
import com.kh.springJpa241217.dto.MemberReqDto;
import com.kh.springJpa241217.entity.Member;
import com.kh.springJpa241217.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // spring 꺼가 좀더 유리하다고 돼있음

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Slf4j // 로그 정보를 출력하기 위함
@Service // 스프링 컨테이너에 빈(bean, 객체) 등록
@RequiredArgsConstructor // 생성자를 자동으로 생성
@Transactional // 여러개의 작업을 하나의 논리적인 단위로 묶어줌
public class AuthService {
    // 생성자를 통한 의존성 주입, 생성자를 통해서 의존성 주입을 받는 경우 Autowired 생략
    private final MemberRepository memberRepository;

    // 회원 가입 여부
    public boolean isMember(String email) {
        return memberRepository.existsByEmail(email);
    }

    // 회원 가입
    public boolean signUp(MemberReqDto memberReqDto) {
        try {
            Member member = convertDtoToEntity(memberReqDto);
            memberRepository.save(member); // 회원 가입, save() insert, update 역할
            return true;
        } catch (Exception e) {
            log.error("회원가입 실패 : {}", e.getMessage());
            return false;
        }
    }

    // 로그인
    public boolean login(LoginReqDto loginReqDto) {
        try {
            Optional<Member> member = memberRepository
                    .findByEmailAndPwd(loginReqDto.getEmail(), loginReqDto.getPwd());
            return member.isPresent(); // 해당 객체가 있음을 의미
        } catch (Exception e) {
            log.error("로그인 실패 : {}", e.getMessage());
            return false;
        }
    }

    // 전체 회원 조회
    public List<Member> allMembers() {
        try {
            List<Member> member = memberRepository.findAll();
            return member;
        } catch (Exception e) {
            log.error("전체 회원 조회 실패 : {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    // 이메일을 이용한 특정 회원 조회
    public Optional<Member> selectMember(String email) {
        return memberRepository.findByEmail(email);
    }

    // 회원 가입 DTO -> Entity
    private Member convertDtoToEntity(MemberReqDto memberReqDto) {
        Member member = new Member();
        member.setEmail(memberReqDto.getEmail());
        member.setName(memberReqDto.getName());
        member.setPwd(memberReqDto.getPwd());
        return member;
    }
}
