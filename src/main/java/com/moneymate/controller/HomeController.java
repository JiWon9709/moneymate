package com.moneymate.controller;

import com.moneymate.entity.Expense;
import com.moneymate.entity.Memo;
import com.moneymate.entity.Saving;
import com.moneymate.service.ExpenseService;
import com.moneymate.service.MemoService;
import com.moneymate.service.SavingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final SavingService savingService;
    private final ExpenseService expenseService;
    private final MemoService memoService;

    @GetMapping("/")
    public String home(@RequestParam(required = false) Integer year,
                       @RequestParam(required = false) Integer month,
                       Model model) {
        // 현재 년월을 기본값으로 사용
        if (year == null || month == null) {
            LocalDate now = LocalDate.now();
            year = now.getYear();
            month = now.getMonthValue();
        }

        // 저축 내역
        List<Saving> savings = savingService.getSavingsByMonth(year, month);
        Long totalSaving = savingService.getTotalSavingByMonth(year, month);

        // 지출 내역
        List<Expense> expenses = expenseService.getExpensesByMonth(year, month);
        Long totalExpense = expenseService.getTotalExpenseByMonth(year, month);

        // 메모
        Optional<Memo> memo = memoService.getMemoByMonth(year, month);

        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("savings", savings);
        model.addAttribute("totalSaving", totalSaving != null ? totalSaving : 0L);
        model.addAttribute("expenses", expenses);
        model.addAttribute("totalExpense", totalExpense != null ? totalExpense : 0L);
        model.addAttribute("memo", memo.orElse(null));
        model.addAttribute("balance", (totalSaving != null ? totalSaving : 0L) - (totalExpense != null ? totalExpense : 0L));

        return "index";
    }

    // 저축 추가
    @PostMapping("/saving/add")
    public String addSaving(@ModelAttribute Saving saving,
                            @RequestParam Integer year,
                            @RequestParam Integer month,
                            RedirectAttributes redirectAttributes) {
        saving.setYear(year);
        saving.setMonth(month);
        savingService.saveSaving(saving);
        redirectAttributes.addAttribute("year", year);
        redirectAttributes.addAttribute("month", month);
        return "redirect:/";
    }

    // 저축 삭제
    @PostMapping("/saving/delete/{id}")
    public String deleteSaving(@PathVariable Long id,
                               @RequestParam Integer year,
                               @RequestParam Integer month,
                               RedirectAttributes redirectAttributes) {
        savingService.deleteSaving(id);
        redirectAttributes.addAttribute("year", year);
        redirectAttributes.addAttribute("month", month);
        return "redirect:/";
    }

    // 지출 추가
    @PostMapping("/expense/add")
    public String addExpense(@ModelAttribute Expense expense,
                            @RequestParam Integer year,
                            @RequestParam Integer month,
                            RedirectAttributes redirectAttributes) {
        expense.setYear(year);
        expense.setMonth(month);
        expenseService.saveExpense(expense);
        redirectAttributes.addAttribute("year", year);
        redirectAttributes.addAttribute("month", month);
        return "redirect:/";
    }

    // 지출 삭제
    @PostMapping("/expense/delete/{id}")
    public String deleteExpense(@PathVariable Long id,
                               @RequestParam Integer year,
                               @RequestParam Integer month,
                               RedirectAttributes redirectAttributes) {
        expenseService.deleteExpense(id);
        redirectAttributes.addAttribute("year", year);
        redirectAttributes.addAttribute("month", month);
        return "redirect:/";
    }

    // 메모 저장
    @PostMapping("/memo/save")
    public String saveMemo(@ModelAttribute Memo memo,
                          @RequestParam Integer year,
                          @RequestParam Integer month,
                          RedirectAttributes redirectAttributes) {
        memo.setYear(year);
        memo.setMonth(month);
        memoService.saveMemo(memo);
        redirectAttributes.addAttribute("year", year);
        redirectAttributes.addAttribute("month", month);
        return "redirect:/";
    }
}
