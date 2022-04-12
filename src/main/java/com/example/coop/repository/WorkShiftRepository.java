package com.example.coop.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.coop.entity.WorkShift;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkShiftRepository extends CrudRepository<WorkShift, Long> {
    // 全取得
    List<WorkShift> findAll();

    // 日付指定
    List<WorkShift> findByDate(LocalDate dt);

    // 期間指定
    List<WorkShift> findByDateBetween(LocalDate startDt, LocalDate endDt);

    // ワーカー、期間指定
    List<WorkShift> findByWorkerIdAndDateBetween(Long workerId, LocalDate startDt, LocalDate endDt);

    // ワーカー，日付指定
    List<WorkShift> findByWorkerIdAndDate(Long workerId, LocalDate dt);
}
