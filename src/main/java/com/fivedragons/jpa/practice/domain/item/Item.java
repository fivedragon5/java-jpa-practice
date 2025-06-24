package com.fivedragons.jpa.practice.domain.item;

import com.fivedragons.jpa.practice.domain.Category;
import com.fivedragons.jpa.practice.exception.NotEnoughStockException;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    @Builder.Default // 빌더에서 기본값으로 사용하도록 명시
    private List<Category> categories = new ArrayList<>();

    // 비즈니스 로직
    /**
     * stock 증가
     */
    public void addStock(int stock) {
        this.stockQuantity += stock;
    }

    /**
     * stock 감소
     *  - 0보다 작을 수 없음
     */
    public void removeStock(int stock) {
        int restStock = this.stockQuantity - stock;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }


}
