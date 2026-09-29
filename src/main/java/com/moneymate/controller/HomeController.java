package com.moneymate.controller;

import com.moneymate.dto.*;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

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

        // 메모 목록
        List<Memo> memos = memoService.getAllMemosByMonth(year, month);

        // 캘린더 데이터 생성
        List<DayData> calendarData = buildCalendarData(year, month, savings, expenses, memos);

        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("savings", savings);
        model.addAttribute("totalSaving", totalSaving != null ? totalSaving : 0L);
        model.addAttribute("expenses", expenses);
        model.addAttribute("totalExpense", totalExpense != null ? totalExpense : 0L);
        model.addAttribute("memos", memos);
        model.addAttribute("calendarData", calendarData);
        model.addAttribute("defaultDate", getDefaultInputDate(year, month));
        model.addAttribute("balance", (totalSaving != null ? totalSaving : 0L) - (totalExpense != null ? totalExpense : 0L));

        return "index";
    }

    // 날짜 입력칸 기본값: 보고 있는 달이 이번 달이면 오늘, 아니면 그 달의 1일
    private LocalDate getDefaultInputDate(Integer year, Integer month) {
        LocalDate today = LocalDate.now();
        YearMonth viewing = YearMonth.of(year, month);
        return viewing.equals(YearMonth.from(today)) ? today : viewing.atDay(1);
    }

    private List<DayData> buildCalendarData(Integer year, Integer month, 
                                            List<Saving> savings, 
                                            List<Expense> expenses, 
                                            List<Memo> memos) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstDay = yearMonth.atDay(1);
        DayOfWeek firstDayOfWeek = firstDay.getDayOfWeek();
        
        // 첫 번째 날의 요일 (월요일=1, 일요일=7)
        int firstDayValue = firstDayOfWeek.getValue();
        
        List<DayData> calendar = new ArrayList<>();
        
        // 첫 주의 빈 칸 추가 (월요일부터 시작)
        for (int i = 1; i < firstDayValue; i++) {
            calendar.add(null);
        }
        
        // 날짜별 데이터 그룹화
        Map<Integer, DayData> dayDataMap = new HashMap<>();
        
        // 저축 데이터
        for (Saving saving : savings) {
            int day = saving.getSavedDate().getDayOfMonth();
            LocalDate savingDate = saving.getSavedDate();
            DayOfWeek dayOfWeek = savingDate.getDayOfWeek();
            dayDataMap.putIfAbsent(day, new DayData(day, dayOfWeek));
            DayData dayData = dayDataMap.get(day);
            if (dayData.getDayOfWeek() == null) {
                dayData.setDayOfWeek(dayOfWeek);
            }
            DaySaving daySaving = new DaySaving();
            daySaving.setId(saving.getId());
            daySaving.setAmount(saving.getAmount());
            daySaving.setDescription(saving.getDescription());
            daySaving.setSavedDate(saving.getSavedDate());
            dayData.getSavings().add(daySaving);
            dayData.setTotalSaving(dayData.getTotalSaving() + saving.getAmount());
        }
        
        // 지출 데이터
        for (Expense expense : expenses) {
            int day = expense.getExpenseDate().getDayOfMonth();
            LocalDate expenseDate = expense.getExpenseDate();
            DayOfWeek dayOfWeek = expenseDate.getDayOfWeek();
            dayDataMap.putIfAbsent(day, new DayData(day, dayOfWeek));
            DayData dayData = dayDataMap.get(day);
            if (dayData.getDayOfWeek() == null) {
                dayData.setDayOfWeek(dayOfWeek);
            }
            DayExpense dayExpense = new DayExpense();
            dayExpense.setId(expense.getId());
            dayExpense.setAmount(expense.getAmount());
            dayExpense.setCategory(expense.getCategory());
            dayExpense.setDescription(expense.getDescription());
            dayExpense.setExpenseDate(expense.getExpenseDate());
            dayData.getExpenses().add(dayExpense);
            dayData.setTotalExpense(dayData.getTotalExpense() + expense.getAmount());
        }
        
        // 메모 데이터
        for (Memo memo : memos) {
            int day = memo.getMemoDate().getDayOfMonth();
            LocalDate memoDate = memo.getMemoDate();
            DayOfWeek dayOfWeek = memoDate.getDayOfWeek();
            dayDataMap.putIfAbsent(day, new DayData(day, dayOfWeek));
            DayData dayData = dayDataMap.get(day);
            if (dayData.getDayOfWeek() == null) {
                dayData.setDayOfWeek(dayOfWeek);
            }
            DayMemo dayMemo = new DayMemo();
            dayMemo.setId(memo.getId());
            dayMemo.setContent(memo.getContent());
            dayMemo.setMemoDate(memo.getMemoDate());
            dayMemo.setCreatedAt(memo.getCreatedAt());
            dayData.getMemos().add(dayMemo);
        }
        
        // 캘린더에 날짜 데이터 추가
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate currentDate = yearMonth.atDay(day);
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            DayData dayData = dayDataMap.getOrDefault(day, new DayData(day, dayOfWeek));
            dayData.setDayOfWeek(dayOfWeek);
            calendar.add(dayData);
        }
        
        return calendar;
    }

    // 저축 추가
    @PostMapping("/saving/add")
    public String addSaving(@ModelAttribute Saving saving,
                            @RequestParam Integer year,
                            @RequestParam Integer month,
                            RedirectAttributes redirectAttributes) {
        // 년/월은 화면에서 선택한 달이 아니라 실제 저축 날짜 기준으로 설정
        if (saving.getSavedDate() == null) {
            saving.setSavedDate(LocalDate.now());
        }
        LocalDate savedDate = saving.getSavedDate();
        saving.setYear(savedDate.getYear());
        saving.setMonth(savedDate.getMonthValue());
        savingService.saveSaving(saving);
        // 저장된 달로 이동
        redirectAttributes.addAttribute("year", savedDate.getYear());
        redirectAttributes.addAttribute("month", savedDate.getMonthValue());
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
        // 년/월은 화면에서 선택한 달이 아니라 실제 지출 날짜 기준으로 설정
        if (expense.getExpenseDate() == null) {
            expense.setExpenseDate(LocalDate.now());
        }
        LocalDate expenseDate = expense.getExpenseDate();
        expense.setYear(expenseDate.getYear());
        expense.setMonth(expenseDate.getMonthValue());
        expenseService.saveExpense(expense);
        // 저장된 달로 이동
        redirectAttributes.addAttribute("year", expenseDate.getYear());
        redirectAttributes.addAttribute("month", expenseDate.getMonthValue());
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
        // 년/월은 화면에서 선택한 달이 아니라 실제 메모 날짜 기준으로 설정
        if (memo.getMemoDate() == null) {
            memo.setMemoDate(LocalDate.now());
        }
        LocalDate memoDate = memo.getMemoDate();
        memo.setYear(memoDate.getYear());
        memo.setMonth(memoDate.getMonthValue());
        memoService.saveMemo(memo);
        // 저장된 달로 이동
        redirectAttributes.addAttribute("year", memoDate.getYear());
        redirectAttributes.addAttribute("month", memoDate.getMonthValue());
        return "redirect:/";
    }

    // 메모 삭제
    @PostMapping("/memo/delete/{id}")
    public String deleteMemo(@PathVariable Long id,
                            @RequestParam Integer year,
                            @RequestParam Integer month,
                            RedirectAttributes redirectAttributes) {
        memoService.deleteMemo(id);
        redirectAttributes.addAttribute("year", year);
        redirectAttributes.addAttribute("month", month);
        return "redirect:/";
    }
}
