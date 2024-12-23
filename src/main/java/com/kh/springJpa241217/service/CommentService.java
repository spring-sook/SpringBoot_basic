package com.kh.springJpa241217.service;

import com.kh.springJpa241217.dto.CommentReqDto;
import com.kh.springJpa241217.dto.CommentResDto;
import com.kh.springJpa241217.entity.Board;
import com.kh.springJpa241217.entity.Comment;
import com.kh.springJpa241217.entity.Member;
import com.kh.springJpa241217.repository.BoardRepository;
import com.kh.springJpa241217.repository.CommentRepository;
import com.kh.springJpa241217.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j // log 메시지 출력
@Service // 스프링 컨테이너에 빈 등록
@RequiredArgsConstructor // 생성자를 자동으로 생성
public class CommentService {
    private final MemberRepository memberRepository; // member 정보를 가져오기 위해
    private final BoardRepository boardRepository; // 게시글 정보를 가져오기 위해
    private final CommentRepository commentRepository; // 댓글 작성을 위해

    // 댓글 등록
    @Transactional
    public boolean commentRegister(CommentReqDto commentReqDto) {
        try{
            Member member = memberRepository.findByEmail(commentReqDto.getEmail())
                    .orElseThrow(()-> new RuntimeException("회원이 존재하지 않습니다."));
            Board board = boardRepository.findById(commentReqDto.getBoardId())
                    .orElseThrow(()-> new RuntimeException("게시글이 존재하지 않습니다."));
            Comment comment = new Comment();
            comment.setContent(commentReqDto.getContent());
            comment.setMember(member);
            comment.setBoard(board);
            commentRepository.save(comment);
            return true;
        } catch (Exception e) {
            log.error("댓글 등록 실패 : {}", e.getMessage());
            return false;
        }
    }

    // 댓글 삭제
    public boolean commentDelete(CommentReqDto commentReqDto) {
        try{
            Comment comment = commentRepository.findById(commentReqDto.getCommentId())
                    .orElseThrow(()-> new RuntimeException("존재하지 않는 댓글입니다."));
            if (comment.getMember().getEmail().equals(commentReqDto.getEmail())) {
                commentRepository.deleteById(commentReqDto.getCommentId());
                return true;
            } else {
                log.error("해당 댓글 작성자가 아닙니다.");
                return false;
            }
        } catch (Exception e) {
            log.error("댓글 삭제 실패 : {}", e.getMessage());
            return false;
        }
    }

    // 댓글 목록 조회
    public List<CommentResDto> commentList(Long boardId) {
        List<Comment> comments = commentRepository.findByBoardId(boardId);
        List<CommentResDto> commentsRes = new ArrayList<>();
        for(Comment comment : comments) {
            commentsRes.add(convertEntityToDto(comment));
        }
        return commentsRes;
    }

    private CommentResDto convertEntityToDto(Comment comment) {
        CommentResDto commentResDto = new CommentResDto();
        commentResDto.setEmail(comment.getMember().getEmail());
        commentResDto.setBoardId(comment.getBoard().getId());
        commentResDto.setCommentId(comment.getCommentId());
        commentResDto.setContent(comment.getContent());
        commentResDto.setRegDate(comment.getRegDate());
        return commentResDto;
    }
}
