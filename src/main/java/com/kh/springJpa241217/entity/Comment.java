package com.kh.springJpa241217.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter @Setter @ToString(exclude = {"board", "member"}) // 순환 참조 방지
@NoArgsConstructor // 빌드 패턴 쓸 때 이게 있어야 에러가 안남
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId; // comment_id

    @ManyToOne
    @JoinColumn(name = "board_id")  // 참조키는 해당 객체의 기본키여야 함
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 1000)
    private String content;

    private LocalDateTime regDate;
    @PrePersist
    public void prePersist() {
        regDate = LocalDateTime.now();
    }
}
