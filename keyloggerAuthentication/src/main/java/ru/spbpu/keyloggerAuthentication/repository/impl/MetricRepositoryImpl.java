package ru.spbpu.keyloggerAuthentication.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.spbpu.keyloggerAuthentication.dto.MetricDTO;
import ru.spbpu.keyloggerAuthentication.repository.MetricRepository;

import java.util.List;
import java.util.UUID;

@Repository
public class MetricRepositoryImpl implements MetricRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MetricRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // language=sql
    private final static String ADD_METRIC_SQL = "INSERT INTO METRICS (id, speed, error, full_time, average_pause, user_id) VALUES (?, ?, ?, ?, ?, ?)";

    // language=sql
    private final static String FIND_ALL_METRICS_SQL = "SELECT id, speed, error, full_time, average_pause, user_id FROM METRICS";

    @Override
    public UUID add(MetricDTO dto) {
        jdbcTemplate.update(ADD_METRIC_SQL,
                dto.getId(),
                dto.getSpeed(),
                dto.getError(),
                dto.getTime(),
                dto.getAveragePause(),
                dto.getUserId());
        return dto.getId();
    }

    @Override
    public List<MetricDTO> findAll() {
        return jdbcTemplate.query(FIND_ALL_METRICS_SQL, (rs, rowNum) -> {
            MetricDTO metricDTO = new MetricDTO();
            metricDTO.setId(UUID.fromString(rs.getString("id")));
            metricDTO.setSpeed(rs.getDouble("speed"));
            metricDTO.setError(rs.getDouble("error"));
            metricDTO.setTime(rs.getDouble("full_time"));
            metricDTO.setAveragePause(rs.getDouble("average_pause"));
            metricDTO.setUserId(UUID.fromString(rs.getString("user_id")));
            return metricDTO;
        });
    }
}
