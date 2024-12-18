package com.kh.springJpa241217.service;

import com.kh.springJpa241217.dto.MemberReqDto;
import com.kh.springJpa241217.dto.MemberResDto;
import com.kh.springJpa241217.entity.Member;
import com.kh.springJpa241217.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor // 생성자를 통한 의존성 주입을 받기 위해 생성자를 Lombok을 통해서 생성
public class MemberService {
    private final MemberRepository memberRepository;

    // 회원 전체 조회
    public List<MemberResDto> getMemberList() {
        // DB로부터 모든 회원 정보를 Member Entity 객체로 읽어옴
        List<Member> members = memberRepository.findAll();
        // Frontend에 정보를 전달하기 위해 DTO List를 생성
        List<MemberResDto> memberResDtoLists = new ArrayList<>();
        for (Member member : members) { // Member Entity로 구성된 리스트 순회
            memberResDtoLists.add(convertEntityToDto(member));
        }
        return memberResDtoLists;
    }

    // 회원 상세 조회_강사님
    public MemberResDto getMemberDetail(String email) {
        Member member = memberRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));
        return convertEntityToDto(member);
    }

    // 회원 정보 수정_강사님
    public boolean modifyMember(MemberReqDto memberReqDto) {
        try{
            Member member = memberRepository.findByEmail(memberReqDto.getEmail())
                    .orElseThrow(()->new RuntimeException("해당 회원이 존재하지 않습니다."));
            member.setName(memberReqDto.getName());
            member.setImgPath(memberReqDto.getImgPath());
            memberRepository.save(member);
            return true;
        } catch (Exception e) {
            log.error("회원 정보 수정 : {}", e.getMessage());
            return false;
        }
    }

    // 회원 삭제
    public boolean deleteMember(String email) {
        try{
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(()->new RuntimeException("해당 회원이 존재하지 않습니다."));
            memberRepository.delete(member);
            return true;
        } catch (Exception e) {
            log.error("회원 삭제에 실패했습니다 : {}", e.getMessage());
            return false;
        }
    }

    // Member Entity => MemberResDto 변환
    private MemberResDto convertEntityToDto(Member member) {
        MemberResDto memberResDto = new MemberResDto();
        memberResDto.setEmail(member.getEmail());
        memberResDto.setName(member.getName());
        memberResDto.setRegDate(member.getRegDate());
        memberResDto.setImgPath(member.getImgPath());
        return memberResDto;
    }
}
