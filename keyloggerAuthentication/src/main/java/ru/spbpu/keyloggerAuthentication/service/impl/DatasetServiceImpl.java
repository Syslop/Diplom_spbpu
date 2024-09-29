package ru.spbpu.keyloggerAuthentication.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spbpu.keyloggerAuthentication.dto.MetricDTO;
import ru.spbpu.keyloggerAuthentication.repository.MetricRepository;
import ru.spbpu.keyloggerAuthentication.service.DatasetService;

import java.util.List;
import java.util.UUID;

@Service
public class DatasetServiceImpl implements DatasetService {
    private final MetricRepository metricRepository;

    @Autowired
    public DatasetServiceImpl(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    @Override
    public List<MetricDTO> getAllMetrics() {
        return metricRepository.findAll();
    }
}
