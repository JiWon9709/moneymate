package com.moneymate.repository;

import com.moneymate.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByYearAndMonth(Integer year, Integer month);
    List<Expense> findByYearAndMonthOrderByExpenseDateDesc(Integer year, Integer month);
}
