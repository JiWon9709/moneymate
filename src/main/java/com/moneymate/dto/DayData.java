package com.moneymate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DayData {
    private int day;
    private DayOfWeek dayOfWeek;
    private List<DaySaving> savings = new ArrayList<>();
    private List<DayExpense> expenses = new ArrayList<>();
    private List<DayMemo> memos = new ArrayList<>();
    private Long totalSaving = 0L;
    private Long totalExpense = 0L;

    public DayData(int day) {
        this.day = day;
    }

    public DayData(int day, DayOfWeek dayOfWeek) {
        this.day = day;
        this.dayOfWeek = dayOfWeek;
    }
}
