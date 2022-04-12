package com.example.coop.repository;

import java.util.List;

import com.example.coop.entity.Worker;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends CrudRepository<Worker, Long> {
    List<Worker> findAll();
}
