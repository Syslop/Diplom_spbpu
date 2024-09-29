package ru.spbpu.keyloggerAuthentication.service;

import ru.spbpu.keyloggerAuthentication.dto.MetricDTO;

import java.util.List;

public interface DatasetService {
    List<MetricDTO> getAllMetrics();
}
