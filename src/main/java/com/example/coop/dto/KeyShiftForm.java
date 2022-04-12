package com.example.coop.dto;

import java.time.LocalDate;

import com.example.coop.entity.KeyShift;

import lombok.Data;

@Data
public class KeyShiftForm {
    Long WorkerId;
    LocalDate since;
    LocalDate until;

    public KeyShift toEntity() {
        return new KeyShift(null, WorkerId, since, until);
    }
}
