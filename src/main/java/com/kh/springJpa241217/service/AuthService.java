package com.kh.springJpa241217.service;

import com.kh.springJpa241217.dto.LoginReqDto;
import com.kh.springJpa241217.dto.MemberReqDto;
import com.kh.springJpa241217.dto.MemberResDto;
import com.kh.springJpa241217.dto.TokenDto;
import com.kh.springJpa241217.entity.Member;
import com.kh.springJpa241217.jwt.TokenProvider;
import com.kh.springJpa241217.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final AuthenticationManagerBuilder managerBuilder; // 인증을 담당하는 클래스
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    // 회원 가입 여부
    public boolean isMember(String email) {
        return memberRepository.existsByEmail(email);
    }

    // 회원 가입
    public MemberResDto signUp(MemberReqDto memberReqDto) {
        if (memberRepository.existsByEmail(memberReqDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }
        Member member = memberReqDto.toEntity(passwordEncoder);
        return MemberResDto.of(memberRepository.save(member));
    }

    // 로그인
    public TokenDto login(MemberReqDto memberReqDto) {
        UsernamePasswordAuthenticationToken authenticationToken = memberReqDto.toAuthentication();
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        return tokenProvider.generateTokenDto(authentication);
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
