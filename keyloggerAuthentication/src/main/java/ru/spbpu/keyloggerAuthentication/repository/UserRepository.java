package ru.spbpu.keyloggerAuthentication.repository;

import ru.spbpu.keyloggerAuthentication.dto.MetricDTO;
import ru.spbpu.keyloggerAuthentication.dto.UserDTO;

import java.util.UUID;

public interface UserRepository extends BaseRepository<UserDTO, UUID> {
    UUID getUserByLoginAndPassword(String login, String password);
}
