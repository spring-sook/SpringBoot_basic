package com.kh.springJpa241217.controller;

import com.kh.springJpa241217.dto.MemberReqDto;
import com.kh.springJpa241217.dto.MemberResDto;
import com.kh.springJpa241217.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 전체/특정 회원 조회
    @GetMapping("/getInfo")
    public ResponseEntity<Object> getInfo(@RequestParam(required = false) String email) {
        if (email == null || email.isEmpty()) {
            List<MemberResDto> members = memberService.getMemberList();
            return ResponseEntity.ok(members);
        } else {
            MemberResDto member = memberService.getMemberDetail(email);
            return ResponseEntity.ok(member);
        }
    }

    // 회원 정보 수정
    @PostMapping("/modify")
    public ResponseEntity<Boolean> modifyMember(@RequestBody MemberReqDto memberReqDto) {
        boolean isSuccess = memberService.modifyMember(memberReqDto);
        return ResponseEntity.ok(isSuccess);
    }

    // 회원 삭제
    @PostMapping("/delete/{email}")
    public ResponseEntity<Boolean> deleteMember(@PathVariable String email) {
        boolean isSuccess = memberService.deleteMember(email);
        return ResponseEntity.ok(isSuccess);
    }
}