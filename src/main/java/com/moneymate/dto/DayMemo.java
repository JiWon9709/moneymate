package com.moneymate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DayMemo {
    private Long id;
    private String content;
    private LocalDate memoDate;
    private LocalDateTime createdAt;
}
