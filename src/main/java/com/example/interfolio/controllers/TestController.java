package com.example.interfolio.controllers;

import com.example.interfolio.dto.TestDto;
import com.example.interfolio.entity.TestEntity;
import com.example.interfolio.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController // RESTful 하게 컨트롤러를 만들기
public class TestController {
    @Autowired // 스프링 부트가 미리 생성해 놓은 리파지토리 객체 주입(DI) -> 인터페이스이므로 객체가 필요함
    private TestRepository testRepository;

    @GetMapping("/")
    public Map<String, Object> getHome() {
        Map<String, Object> testData = new HashMap<>();

        testData.put("test1", "content1");
        testData.put("test2", "content2");

        return testData;
    }

    @PostMapping("/")
    public TestDto postHome(@RequestBody TestDto testDto) {
        // 1. Dto를 엔티티로 변환
        TestEntity test = testDto.toEntity();
        // 2. 리파지터리로 엔티티를 DB에 저장
        TestEntity saved = testRepository.save(test);
        return testDto;
    }
}
