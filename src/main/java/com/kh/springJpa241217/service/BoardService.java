package com.kh.springJpa241217.service;

import com.kh.springJpa241217.dto.BoardReqDto;
import com.kh.springJpa241217.dto.BoardResDto;
import com.kh.springJpa241217.dto.CommentReqDto;
import com.kh.springJpa241217.dto.CommentResDto;
import com.kh.springJpa241217.entity.Board;
import com.kh.springJpa241217.entity.Comment;
import com.kh.springJpa241217.entity.Member;
import com.kh.springJpa241217.repository.BoardRepository;
import com.kh.springJpa241217.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository; // 의존성 주입
    private final MemberRepository memberRepository;
    // 게시글 등록
    @Transactional // 실행중 예외가 발생하면 트랜잭션이 롤백됨
    public boolean saveBoard(BoardReqDto boardReqDto) {
        try {
            // 프론트엔드에서 전달한 이메일로 회원 정보를 가져옴
            Member member = memberRepository.findByEmail(boardReqDto.getEmail())
                    .orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));

            Board board = new Board();
            board.setTitle(boardReqDto.getTitle());
            board.setContent(boardReqDto.getContent());
            board.setImgPath(boardReqDto.getImgPath());
            board.setMember(member);
            boardRepository.save(board); // insert
            return true;
        } catch (Exception e) {
            log.error("게시글 등록 실패 : {}", e.getMessage());
            return false;
        }
    }

    // 게시글 상세 조회
    public BoardResDto findByBoardId(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));

        return convertEntityToDto(board);
    }

    // 게시글 전체 조회
    public List<BoardResDto> findAllBoard() {
        List<Board> boards = boardRepository.findAll(); // DB에 있는 모든 게시글 가져오기
        List<BoardResDto> boardResDtoList = new ArrayList<>();
        for(Board board : boards) {
            // convertEntityToDto를 통해서 BoardResDto를 변환 받아서 List에 추가
            boardResDtoList.add(convertEntityToDtoWithoutComments(board));
        }
        return boardResDtoList;
    }

    // 게시글 검색
    public List<BoardResDto> searchBoard(String keyword, String searchType) {
        List<Board> boards = new ArrayList<>();

        switch (searchType) {
            case "title":
                boards = boardRepository.findByTitleContaining(keyword);
                break;
            case "content":
                boards = boardRepository.findByContentContaining(keyword);
                break;
            case "titleContent":
                boards = boardRepository.findByTitleContainingOrContentContaining(keyword, keyword);
                break;
            case "writer":
                boards = boardRepository.findByMemberEmailContaining(keyword);
                break;
            case "all":
                boards = boardRepository.findByTitleContainingOrContentContainingOrMemberEmailContaining(keyword, keyword, keyword);
                break;
        }

        List<BoardResDto> boardResDtoList = new ArrayList<>();
        for(Board board : boards) {
            boardResDtoList.add(convertEntityToDtoWithoutComments(board));
        }

//        List<Board> boards = boardRepository.findByTitleContaining(keyword);
//        List<BoardResDto> boardResDtoList = new ArrayList<>();
//        for(Board board : boards) {
//            // convertEntityToDto를 통해서 BoardResDto를 변환 받아서 List에 추가
//            boardResDtoList.add(convertEntityToDto(board));
//        }
        return boardResDtoList;
    }

    // 게시글 페이지 수 조회
    public int getBoardPageCount(int page, int size) {
//        PageRequest pageRequest = PageRequest.of(page, size);
        PageRequest pageRequest = PageRequest.ofSize(size);
        return boardRepository.findAll(pageRequest).getTotalPages();
    }

    // 게시글 페이징
    public List<BoardResDto> pagingBoardList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Board> boards = boardRepository.findAll(pageable).getContent();
        List<BoardResDto> boardResDtoList = new ArrayList<>();
        for(Board board : boards) {
            // convertEntityToDto를 통해서 BoardResDto를 변환 받아서 List에 추가
            boardResDtoList.add(convertEntityToDtoWithoutComments(board));
        }
        return boardResDtoList;
    }

    // 게시글 삭제
    public boolean deleteBoard(Long boardId, Long memberId) {
        try {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
            if(memberId == board.getMember().getId()) {
                boardRepository.deleteById(boardId);
                return true;
            } else {
                log.info("해당 게시글을 작성한 작성자가 아닙니다.");
                return false;
            }
        } catch (Exception e) {
            log.info("게시글 삭제 중 오류 : {}", e.getMessage());
            return false;
        }
    }

    // 게시글 수정
    public boolean modifyBoard(BoardReqDto boardReqDto) {
        try{
            Board isBoard = boardRepository.findById(boardReqDto.getBoardId())
                    .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
            memberRepository.findByEmail(boardReqDto.getEmail())
                    .orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));
            if (isBoard.getMember().getEmail().equals(boardReqDto.getEmail())) {
                isBoard.setTitle(boardReqDto.getTitle());
                isBoard.setContent(boardReqDto.getContent());
                isBoard.setImgPath(boardReqDto.getImgPath());
                boardRepository.save(isBoard);
                return true;
            } else {
                log.error("게시글은 작성자만 수정할 수 있습니다.");
                return false;
            }
        } catch (Exception e) {
            log.error("게시글 작성 중 오류 : {}", e.getMessage());
            return false;
        }
    }

    // 댓글 목록 조회
    public List<CommentResDto> commentList(Long boardId) {
        try{
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(()->new RuntimeException("해당 게시글이 없습니다."));
            List<CommentResDto> commentResDtoList = new ArrayList<>();
            for (Comment comment : board.getComments()) {
                CommentResDto commentResDto = new CommentResDto();
                commentResDto.setEmail(comment.getMember().getEmail());
                commentResDto.setBoardId(comment.getBoard().getId());
                commentResDto.setCommentId(comment.getCommentId());
                commentResDto.setContent(comment.getContent());
                commentResDto.setRegDate(comment.getRegDate());
                commentResDtoList.add(commentResDto);
            }
            return commentResDtoList;
        }catch (Exception e) {
            log.error("게시글에 대한 댓글 조회 실패 : {}", e.getMessage());
            return null;
        }
    }

    // 영속성 제거/고아객체제거 예제를 위한 코드
    @Transactional // 중간에 실패하면 자동 롤백
    public boolean addComment(Long boardId, CommentReqDto commentReqDto) {
        try {
            // id로 board 객체 가져오기
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
            // email로 member 객체 가져오기
            Member member = memberRepository.findByEmail(commentReqDto.getEmail())
                    .orElseThrow(() -> new RuntimeException("회원 정보가 존재하지 않습니다."));
            // Dto -> Entity로 변환
            Comment comment = new Comment();
            comment.setContent(commentReqDto.getContent());
            comment.setMember(member);
            comment.setBoard(board);
            board.addComment(comment);
            boardRepository.save(board);
            return true;
        } catch (Exception e) {
            log.error("댓글 추가 실패 : {}", e.getMessage());
            return false;
        }
    }
    @Transactional
    public boolean removeComment(Long boardId, Long commentId) {
        try {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
            Comment targetComment = null; // 지울 댓글에 대한 변수 생성
            for (Comment comment : board.getComments()) {
                log.warn(comment.toString());
                if (comment.getCommentId().equals(commentId)) {
                    targetComment = comment;
                    break;
                }
            }
            if (targetComment == null) {
                log.error("해당 댓글이 존재하지 않습니다.");
            }
            board.removeComment(targetComment);
            boardRepository.save(board);
            return true;
        } catch (Exception e) {
            log.error("댓글 제거 실패 : {}", e.getMessage());
            return false;
        }
    }

    private BoardResDto convertEntityToDto(Board board) {
        BoardResDto boardResDto = new BoardResDto();
        boardResDto.setBoardId(board.getId());
        boardResDto.setTitle(board.getTitle());
        boardResDto.setContent(board.getContent());
        boardResDto.setImgPath(board.getImgPath());
        boardResDto.setEmail(board.getMember().getEmail());
        boardResDto.setRegDate(board.getRegDate());

        List<CommentResDto> commentResDtoList = new ArrayList<>();
        for (Comment comment : board.getComments()) {
            CommentResDto commentResDto = new CommentResDto();
            commentResDto.setEmail(comment.getMember().getEmail());
            commentResDto.setBoardId(comment.getBoard().getId());
            commentResDto.setCommentId(comment.getCommentId());
            commentResDto.setContent(comment.getContent());
            commentResDto.setRegDate(comment.getRegDate());
            commentResDtoList.add(commentResDto);
        }
        boardResDto.setComments(commentResDtoList);
        return boardResDto;
    }

    // 댓글 제외 DTO
    private BoardResDto convertEntityToDtoWithoutComments(Board board) {
        BoardResDto boardResDto = new BoardResDto();
        boardResDto.setBoardId(board.getId());
        boardResDto.setTitle(board.getTitle());
        boardResDto.setContent(board.getContent());
        boardResDto.setImgPath(board.getImgPath());
        boardResDto.setEmail(board.getMember().getEmail());
        boardResDto.setRegDate(board.getRegDate());
        boardResDto.setComments(new ArrayList<>());
        return boardResDto;
    }
}
