package com.moneymate.repository;

import com.moneymate.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {
    Optional<Memo> findByYearAndMonth(Integer year, Integer month);
    List<Memo> findByYearAndMonthOrderByCreatedAtDesc(Integer year, Integer month);
}
