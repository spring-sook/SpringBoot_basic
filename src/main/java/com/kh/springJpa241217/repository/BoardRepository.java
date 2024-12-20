package com.kh.springJpa241217.repository;

import com.kh.springJpa241217.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByTitleContaining(String keyword); // 제목 검색
    List<Board> findByContentContaining(String keyword); // 내용 검색
    List<Board> findByTitleContainingOrContentContaining(String title, String content); // 제목 + 내용 검색
    List<Board> findByMemberEmailContaining(String keyword); // 작성자 검색
    List<Board> findByTitleContainingOrContentContainingOrMemberEmailContaining(String title, String content, String author); // 전체 검색
}
