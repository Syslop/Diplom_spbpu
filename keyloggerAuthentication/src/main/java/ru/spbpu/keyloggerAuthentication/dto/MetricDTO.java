package ru.spbpu.keyloggerAuthentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MetricDTO {
    private UUID id;

    private double speed;

    private double error;

    private double time;

    private double averagePause;

    private UUID userId;
}
