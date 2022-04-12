package com.example.coop.dto;

import com.example.coop.entity.Worker;

import lombok.Data;

@Data
public class WorkerForm {
    Long id;
    String name;

    public Worker toEntity() {
        Worker p = new Worker(id, name);
        return p;
    }
}
