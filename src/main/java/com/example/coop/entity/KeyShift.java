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
public class KeyShift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long WorkerId;
    LocalDate since;
    LocalDate until;
}
