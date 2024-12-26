package com.kh.springJpa241217.controller;

import com.kh.springJpa241217.dto.LoginReqDto;
import com.kh.springJpa241217.dto.MemberReqDto;
import com.kh.springJpa241217.entity.Member;
import com.kh.springJpa241217.service.AuthService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService; // 의존성 주입

    // 회원 가입 여부 확인
    @GetMapping("/exists/{email}")
    public ResponseEntity<Boolean> isMember(@PathVariable String email) {
        boolean isTrue = authService.isMember(email);
        return ResponseEntity.ok(!isTrue);
    }

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<Boolean> signUp(@RequestBody MemberReqDto memberReqDto) {
        boolean isSuccess = authService.signUp(memberReqDto);
        return ResponseEntity.ok(isSuccess);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody LoginReqDto loginReqDto) {
        boolean isSuccess = authService.login(loginReqDto);
        return ResponseEntity.ok(isSuccess);
    }

//    // 전체 회원 조회
//    @GetMapping("/allMembers")
//    public ResponseEntity<List<Member>> allMembers() {
//        List<Member> members = authService.allMembers();
//        return ResponseEntity.ok(members);
//    }
//
//    // 이메일을 이용한 특정 회원 조회
//    @GetMapping("/selectMember/{email}")
//    public ResponseEntity<Optional<Member>> selectMember(@PathVariable String email) {
//        Optional<Member> member = authService.selectMember(email);
//        return ResponseEntity.ok(member);
//    }

    // (전체/특정) 회원 조회
    @GetMapping("/selectMember")
    public ResponseEntity<Object> selectMember(@RequestParam(required = false) String email) {
        if (email == null || email.isEmpty()) {
            List<Member> members = authService.allMembers();
            return ResponseEntity.ok(members);
        } else {
            Optional<Member> member = authService.selectMember(email);
            return ResponseEntity.ok(member);
        }
    }
}
