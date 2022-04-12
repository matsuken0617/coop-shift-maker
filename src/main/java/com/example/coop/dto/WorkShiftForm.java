package com.example.coop.dto;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.example.coop.entity.WorkShift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkShiftForm {
    Long id;
    Long workerId;
    String date;
    int work;

    public WorkShift toEntity() throws ParseException {
        LocalDate dt = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/MM/dd E"));
        return new WorkShift(id, workerId, dt, work);
    }
}
