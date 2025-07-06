package com.example.interfolio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // test_entity_seq 테이블을 생성하지 않기 위해
    private Long id;
    @Column
    private String title;
    @Column
    private String content;
}
