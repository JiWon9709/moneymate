package com.moneymate.service;

import com.moneymate.entity.Memo;
import com.moneymate.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoService {

    private final MemoRepository memoRepository;

    public Optional<Memo> getMemoByMonth(Integer year, Integer month) {
        return memoRepository.findByYearAndMonth(year, month);
    }

    public List<Memo> getAllMemosByMonth(Integer year, Integer month) {
        return memoRepository.findByYearAndMonthOrderByCreatedAtDesc(year, month);
    }

    @Transactional
    public Memo saveMemo(Memo memo) {
        // 항상 새 메모를 생성
        return memoRepository.save(memo);
    }

    @Transactional
    public void deleteMemo(Long id) {
        memoRepository.deleteById(id);
    }

    public Memo getMemoById(Long id) {
        return memoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("메모를 찾을 수 없습니다."));
    }
}
