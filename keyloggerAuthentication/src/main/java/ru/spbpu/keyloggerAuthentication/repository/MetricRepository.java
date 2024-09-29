package ru.spbpu.keyloggerAuthentication.repository;

import ru.spbpu.keyloggerAuthentication.dto.MetricDTO;

import java.util.List;
import java.util.UUID;

public interface MetricRepository extends BaseRepository<MetricDTO, UUID> {
    List<MetricDTO> findAll();
}
