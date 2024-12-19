package com.kh.springJpa241217.entity;

import com.kh.springJpa241217.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity // 해당 클래스가 엔티티임을 나타냄(즉, 데이터베이스 테이블을 의미)
@Table(name = "item") // name 안넣어주면 클래스명으로 들어감
@Getter @Setter @ToString
public class Item {
    @Id // Primary Key, 기본키 필드 지정
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)  // 기본키 값을 JPA가 자동으로 생성
    private Long id; // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemNum; // 상품명

    @Column(nullable = false)
    private int price;  // 가격

    @Column(nullable = false)
    private int stockNumber; // 재고 수량

    @Lob // 대용량 데이터 매핑
    @Column
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;  // 상품 판매 상태

    private LocalDateTime regTime;
    private LocalDateTime updateTime;
}
