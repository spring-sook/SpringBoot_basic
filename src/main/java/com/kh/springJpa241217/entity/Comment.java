package com.kh.springJpa241217.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter @Setter @ToString
@NoArgsConstructor // 빌드 패턴 쓸 때 이게 있어야 에러가 안남
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId; // comment_id

    @ManyToOne
    @JoinColumn(name = "board_id")
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
