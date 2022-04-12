package com.example.coop.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkShiftListForm {
    List<WorkShiftForm> list = new ArrayList<>();

    public void add(WorkShiftForm wsForm) {
        list.add(wsForm);
    }
}
