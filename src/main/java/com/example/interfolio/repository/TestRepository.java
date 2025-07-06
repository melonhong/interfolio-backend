package com.example.interfolio.repository;

import com.example.interfolio.entity.TestEntity;
import org.springframework.data.repository.CrudRepository;

public interface TestRepository extends CrudRepository<TestEntity, Long> {
}
