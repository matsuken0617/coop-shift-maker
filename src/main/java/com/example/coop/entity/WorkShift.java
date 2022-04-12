package com.example.coop.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WorkShift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long workerId;
    LocalDate date;
    int work; // 〇なら1，☓なら-1
}
