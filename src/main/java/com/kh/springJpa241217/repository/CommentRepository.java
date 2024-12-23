package com.kh.springJpa241217.repository;

import com.kh.springJpa241217.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardId(Long boardId); // 댓글들 검색
}
