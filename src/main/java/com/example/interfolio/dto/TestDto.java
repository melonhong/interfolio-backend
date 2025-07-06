package com.example.interfolio.dto;

import com.example.interfolio.entity.TestEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDto {
    private String title;
    private String content;

    public TestEntity toEntity() {
        return new TestEntity(null, this.title, this.content);
    }
}
