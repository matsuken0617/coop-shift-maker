package com.example.coop.service;

import java.util.List;

import com.example.coop.dto.KeyShiftForm;
import com.example.coop.entity.KeyShift;
import com.example.coop.exception.CoopException;
import com.example.coop.repository.KeyShiftRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeyShiftService {
    @Autowired
    KeyShiftRepository ksRepo;

    /**
     * 作成
     * 
     * @param form
     * @return
     */
    public KeyShift createKeyShift(KeyShiftForm form) {
        // ID重複チェック
        KeyShift ks = form.toEntity();
        return ksRepo.save(ks);
    }

    /**
     * 取得
     * 
     * @param id
     * @return
     */
    public KeyShift getKeyShift(Long id) {
        KeyShift ks = ksRepo.findById(id).orElseThrow(
                () -> new CoopException(CoopException.NO_SUCH_KEY_SHIFT_EXISTS, "No exists"));
        return ks;
    }

    /**
     * 全件取得
     * 
     * @return
     */
    public List<KeyShift> getAllKeyShifts() {
        return ksRepo.findAll();
    }

    /**
     * 削除
     * 
     * @param id
     */
    public void deleteKeyShift(Long id) {
        KeyShift ks = getKeyShift(id);
        ksRepo.delete(ks);
    }
}
