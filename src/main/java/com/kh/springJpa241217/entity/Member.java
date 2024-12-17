package com.kh.springJpa241217.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

// @Data // 이건 Getter, Setter, ToString 다 들어있는거임.
@Entity
@Table(name="member") // 대문자로 넣어도 소문자로 들어감. 왜냐하면, DB는 스네이크 표기법을 사용하기 때문
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString
public class Member {
    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;  // Primary Key
    @Column(nullable = false) // not null
    private String email;
    @Column(nullable = false)
    private String pwd;

    private String name;
    private LocalDateTime regDate;
    private String imgPath;
}
