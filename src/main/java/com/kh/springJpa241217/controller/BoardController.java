package com.kh.springJpa241217.controller;

import com.kh.springJpa241217.dto.BoardReqDto;
import com.kh.springJpa241217.dto.BoardResDto;
import com.kh.springJpa241217.entity.Board;
import com.kh.springJpa241217.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 게시글 등록
    @PostMapping("/write")
    public ResponseEntity<Boolean> writeBoard(@RequestBody BoardReqDto boardReqDto) {
        boolean isSuccess = boardService.saveBoard(boardReqDto);
        return ResponseEntity.ok(isSuccess);
    }

    // 게시글 상세 조회
    @GetMapping("/contentDetail/{boardId}")
    public ResponseEntity<BoardResDto> contentDetail(@PathVariable Long boardId) {
        BoardResDto content = boardService.findByBoardId(boardId);
        return ResponseEntity.ok(content);
    }

    // 게시글 전체 조회
    @GetMapping("/posts")
    public ResponseEntity<List> boardList() {
        List boards = boardService.findAllBoard();
        return ResponseEntity.ok(boards);
    }

    // 게시글 검색
    @GetMapping("/search")
    public ResponseEntity<List<BoardResDto>> searchBoard(@RequestParam String keyword, @RequestParam String searchType) {
        List<BoardResDto> contents = boardService.searchBoard(keyword, searchType);
        return ResponseEntity.ok(contents);
    }

    // 게시글 삭제
    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteBoard(@RequestParam Long boardId, @RequestParam Long memberId) {
        boolean isSuccess = boardService.deleteBoard(boardId, memberId);
        return ResponseEntity.ok(isSuccess);
    }

    // 게시글 수정
    @PostMapping("/modify")
    public ResponseEntity<Boolean> modifyBoard(@RequestBody BoardReqDto boardReqDto) {
        boolean isSuccess = boardService.modifyBoard(boardReqDto);
        return ResponseEntity.ok(isSuccess);
    }

    // 게시글 페이징 카운트
    @GetMapping("/count")
    public ResponseEntity<Integer> boardPageCnt(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        int pageCnt = boardService.getBoardPageCount(page, size);
        return ResponseEntity.ok(pageCnt);
    }

    // 게시글 페이징 조회
    @GetMapping("/list/page")
    public ResponseEntity<List<BoardResDto>> boardPageList(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        List<BoardResDto> list = boardService.pagingBoardList(page, size);
        return ResponseEntity.ok(list);
    }
}
