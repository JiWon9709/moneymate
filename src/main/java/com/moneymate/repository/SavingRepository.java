package com.moneymate.repository;

import com.moneymate.entity.Saving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingRepository extends JpaRepository<Saving, Long> {
    List<Saving> findByYearAndMonth(Integer year, Integer month);
    List<Saving> findByYearAndMonthOrderBySavedDateDesc(Integer year, Integer month);
}
