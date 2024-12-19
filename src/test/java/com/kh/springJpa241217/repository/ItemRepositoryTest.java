package com.kh.springJpa241217.repository;

import com.kh.springJpa241217.constant.ItemSellStatus;
import com.kh.springJpa241217.entity.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트") // 테스트 이름
    public void createItemTest() {
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemNum("테스트 상품" + i);
            item.setPrice(1000 * i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            if(i%2 == 0 ) {
                item.setItemSellStatus(ItemSellStatus.SELL);
            } else {
                item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            }
//            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item saveItem = itemRepository.save(item);
            log.info("Item 저장 : {}", saveItem);
        }
    }

    @Test
    @DisplayName("상품 조회 테스트")
    public void findByItemNumOrItemDetailTEST() {
        this.createItemTest(); // 10개의 상품을 생성
        List<Item> itemList = itemRepository.findByItemNumOrItemDetail("테스트 상품5", "테스트 상품 상세 설명10");
        for (Item item : itemList) {
            log.info("상품 조회 테스트 : {}", item);
        }
    }

    @Test
    @DisplayName("상품 조회 OR 테스트")
    public void findByItemNumTest() {
        this.createItemTest(); // 10개의 상품을 생성
        List<Item> itemList = itemRepository.findByItemNum("테스트 상품5");
        for (Item item : itemList) {
            log.info("상품 조회 테스트 : {}", item);
        }
    }

    @Test
    @DisplayName("5000원 미만 상품 조회 테스트")
    public void findByPriceLessThanTest() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByPriceLessThan(5000);
        for (Item item : itemList) {
            log.info("5000원 미만 상품 조회 테스트 : {}", item);
        }
    }

    @Test
    @DisplayName("가격 5000원 이상, 판매중인 상품 조회 테스트")
    public void findByPriceGreaterThanEqualAndItemSellStatusTest() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByPriceGreaterThanEqualAndItemSellStatus(5000, ItemSellStatus.SELL);
        for (Item item : itemList) {
            log.info("가격 5000원 이상, 판매중인 상품 조회 테스트 : {}", item);
        }
    }

    @Test
    @DisplayName("상품 가격에 대한 내림 차순 정렬 테스트")
    public void findAllByOrderByPriceDescTEST() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findAllByOrderByPriceDesc();
        for (Item item : itemList) {
            log.info("상품 가격에 대한 내림 차순 정렬 테스트 : {}", item);
        }
    }

    @Test
    @DisplayName("상품 이름에 특정 키워드가 포함된 상품 검색 테스트")
    public void findByItemNumContainingTEST() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemNumContaining("상품5");
        for (Item item : itemList) {
            log.info("상품 이름에 특정 키워드가 포함된 상품 검색 테스트 : {}", item);
        }
    }

    @Test
    @DisplayName("특정 상품명에 특정 가격이 일치하는 상품 검색 테스트")
    public void findByItemNumAndPriceTEST() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemNumAndPrice("테스트 상품5", 4000);
        for (Item item : itemList) {
            log.info("특정 상품명에 특정 가격이 일치하는 상품 검색 테스트 : {}", item);
        }
    }

    @Test
    @DisplayName("가격 범위에 해당하는 조건 검색 테스트")
    public void findByPriceBetweenTEST() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByPriceBetween(3000, 7000);
        for (Item item : itemList) {
            log.info("가격 범위에 해당하는 조건 검색 테스트 : {}", item);
        }
    }

    // JPQL 테스트
    @Test
    @DisplayName("JPQL 상품 상세 정보 테스트")
    public void findByJpqlTest() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemDetail("설명1");
        for(Item item : itemList) {
            log.info("JPQL Like 검색 : {}", item);
        }
    }

    // native Query 테스트
    @Test
    @DisplayName("JPQL 상품 상세 정보 테스트")
    public void findByNativeTest() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemDetailNative("설명1");
        for(Item item : itemList) {
            log.info("Native Query Like 검색 : {}", item);
        }
    }
}