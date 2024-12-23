package com.kh.springJpa241217.service;

import com.kh.springJpa241217.dto.BoardResDto;
import com.kh.springJpa241217.dto.MemberReqDto;
import com.kh.springJpa241217.dto.MemberResDto;
import com.kh.springJpa241217.entity.Board;
import com.kh.springJpa241217.entity.Member;
import com.kh.springJpa241217.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    // 특정 회원이 작성한 게시글 목록
    public MemberResDto getMemberBoards(Long id) {
        try{
            Member member = memberRepository.findById(id)
                    .orElseThrow(()->new RuntimeException("해당 회원이 존재하지 않습니다."));
            return convertEntityToDtoWithBoard(member);
        } catch (Exception e) {
            log.error("특정 회원 게시물 조회 오류 : {}", e.getMessage());
            return null;
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
    private MemberResDto convertEntityToDtoWithBoard(Member member) {
        MemberResDto memberResDto = new MemberResDto();
        memberResDto.setEmail(member.getEmail());
        memberResDto.setName(member.getName());
        memberResDto.setRegDate(member.getRegDate());
        memberResDto.setImgPath(member.getImgPath());

        List<BoardResDto> boardResDtoList = new ArrayList<>();
        for (Board board : member.getBoards()) {
            BoardResDto boardResDto = new BoardResDto();
            boardResDto.setBoardId(board.getId());
            boardResDto.setTitle(board.getTitle());
            boardResDto.setContent(board.getContent());
            boardResDto.setImgPath(board.getImgPath());
            boardResDto.setRegDate(board.getRegDate());
            boardResDtoList.add(boardResDto);
        }

        memberResDto.setBoards(boardResDtoList);
        return memberResDto;
    }
}
