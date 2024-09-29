package ru.spbpu.keyloggerAuthentication.service;

import ru.spbpu.keyloggerAuthentication.dto.MetricDTO;
import ru.spbpu.keyloggerAuthentication.dto.UserDTO;

import java.util.UUID;

public interface UserService {
    UUID addUser(UserDTO userDTO, MetricDTO metricDTO);

    UUID getAuthWithMetric(UserDTO userDTO, MetricDTO metricDTO);

    UUID getAuthWithoutMetric(UserDTO userDTO, MetricDTO metricDTO);
}
