package ru.spbpu.keyloggerAuthentication.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.spbpu.keyloggerAuthentication.dto.UserDTO;
import ru.spbpu.keyloggerAuthentication.repository.UserRepository;

import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // language=sql
    private final static String ADD_USER_SQL = "INSERT INTO USERS (id, login, password) VALUES (?, ?, ?)";

    // language=sql
    private final static String FIND_USER_BY_LOGIN_AND_PASSWORD_SQL = "SELECT id FROM USERS WHERE login = ? AND password = ?";

    @Override
    public UUID add(UserDTO dto) {
        jdbcTemplate.update(ADD_USER_SQL,
                dto.getId(),
                dto.getLogin(),
                dto.getPassword());
        return dto.getId();
    }

    @Override
    public UUID getUserByLoginAndPassword(String login, String password) {
        return jdbcTemplate.queryForObject(FIND_USER_BY_LOGIN_AND_PASSWORD_SQL,
                UUID.class,
                login,
                password);
    }
}
