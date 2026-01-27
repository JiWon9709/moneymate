package com.moneymate.service;

import com.moneymate.entity.Saving;
import com.moneymate.repository.SavingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SavingService {

    private final SavingRepository savingRepository;

    public List<Saving> getSavingsByMonth(Integer year, Integer month) {
        return savingRepository.findByYearAndMonthOrderBySavedDateDesc(year, month);
    }

    public Long getTotalSavingByMonth(Integer year, Integer month) {
        return savingRepository.findByYearAndMonth(year, month)
                .stream()
                .mapToLong(Saving::getAmount)
                .sum();
    }

    @Transactional
    public Saving saveSaving(Saving saving) {
        return savingRepository.save(saving);
    }

    @Transactional
    public void deleteSaving(Long id) {
        savingRepository.deleteById(id);
    }

    public Saving getSavingById(Long id) {
        return savingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("저축 내역을 찾을 수 없습니다."));
    }
}
