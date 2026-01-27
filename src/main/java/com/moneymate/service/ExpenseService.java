package com.moneymate.service;

import com.moneymate.entity.Expense;
import com.moneymate.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public List<Expense> getExpensesByMonth(Integer year, Integer month) {
        return expenseRepository.findByYearAndMonthOrderByExpenseDateDesc(year, month);
    }

    public Long getTotalExpenseByMonth(Integer year, Integer month) {
        return expenseRepository.findByYearAndMonth(year, month)
                .stream()
                .mapToLong(Expense::getAmount)
                .sum();
    }

    @Transactional
    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Transactional
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("지출 내역을 찾을 수 없습니다."));
    }
}
