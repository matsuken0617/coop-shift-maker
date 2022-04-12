package com.example.coop.repository;

import java.util.List;

import com.example.coop.entity.KeyShift;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyShiftRepository extends CrudRepository<KeyShift, Long> {
    List<KeyShift> findAll();
}
