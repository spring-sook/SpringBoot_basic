package com.kh.springJpa241217.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

// @Data // 이건 Getter, Setter, ToString 다 들어있는거임.
@Entity // 해당 클래스가 Entity임을 나타냄(일반 클래스가 아니라 DB 테이블)
// @Table : 테이블 이름 지정, 테이블 이름은 소문자, 카멜 표기법은 snake 표기법으로 바뀜
@Table(name="member") // 대문자로 넣어도 소문자로 들어감. 왜냐하면, DB는 스네이크 표기법을 사용하기 때문
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString // 오버라이딩
public class Member {
    @Id // 해당 필드를 기본키로 지정
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)// 기본키 생성 전략, Auto: JPA가 자동으로 생성 전략을 정함
    private Long id;  // Primary Key

    @Column(nullable = false, length = 50) // not null, null값이 올수 없다는 제약 조건
    private String email;

    @Column(nullable = false, length = 50)
    private String pwd;

    @Column(length =  50)
    private String name;
//    @Column(length = 50)
//    private String phone;

    @Column(name = "register_date")
    private LocalDateTime regDate;

    @Column(name = "image_path")
    private String imgPath;

    @PrePersist // JPA의 콜백 메서드로 엔티티가 저자오디기 전에 실행, DB에 데이터가 삽입되기 전에 자동 설정
    protected void onCreate() {
        this.regDate = LocalDateTime.now();
    }

    // 게시글 목록에 대한 OneToMany
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;
}
