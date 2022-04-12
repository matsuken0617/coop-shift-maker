package com.example.coop.service;

import java.util.List;

import com.example.coop.dto.WorkerForm;
import com.example.coop.entity.Worker;
import com.example.coop.exception.CoopException;
import com.example.coop.repository.WorkerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkerService {
    @Autowired
    WorkerRepository wRepo;

    /**
     * 作成
     * 
     * @param form
     * @return
     */
    public Worker createWorker(WorkerForm form) {
        // ID重複チェック
        Long id = form.getId();
        if (wRepo.existsById(id)) {
            throw new CoopException(CoopException.Worker_ALREADY_EXISTS, "Worker already exists");
        }
        Worker p = form.toEntity();
        return wRepo.save(p);
    }

    /**
     * 取得
     * 
     * @param id
     * @return
     */
    public Worker getWorker(Long id) {
        Worker p = wRepo.findById(id).orElseThrow(
                () -> new CoopException(CoopException.NO_SUCH_Worker_EXISTS, "no Worker"));
        return p;
    }

    /**
     * 全件取得
     * 
     * @return
     */
    public List<Worker> getAllWorkers() {
        return wRepo.findAll();
    }

    /**
     * 削除
     * 
     * @param id
     */
    public void deleteWorker(Long id) {
        Worker p = getWorker(id);
        wRepo.delete(p);
    }
}
