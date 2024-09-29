package ru.spbpu.keyloggerAuthentication.controller.impl;

import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.spbpu.keyloggerAuthentication.dto.MetricDTO;
import ru.spbpu.keyloggerAuthentication.dto.UserDTO;
import ru.spbpu.keyloggerAuthentication.openapi.api.UserApi;
import ru.spbpu.keyloggerAuthentication.service.UserService;

import java.util.UUID;

@RestController
public class UserApiImpl implements UserApi {
    private final UserService userService;

    @Value("${isKeyloggerAuth}")
    private boolean isKeyloggerAuth;

    @Autowired
    public UserApiImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<UserAddResponse> addUser(UserAddRequest userAddRequest) {
        UserItem userItem = userAddRequest.getUser();
        MetricItem metricItem = userAddRequest.getMetric();

        UserDTO userDTO = new UserDTO(UUID.randomUUID(), userItem.getLogin(), userItem.getPassword());
        MetricDTO metricDTO = new MetricDTO(UUID.randomUUID(),
                metricItem.getSpeed(),
                metricItem.getError(),
                metricItem.getTime(),
                metricItem.getAveragePause(),
                userDTO.getId());

        UserAddResponse userAddResponse = new UserAddResponse(String.valueOf(userService.addUser(userDTO, metricDTO)), userDTO.getLogin());
        return ResponseEntity.ok(userAddResponse);
    }

    @Override
    public ResponseEntity<UserLoginResponse> getAuth(UserLoginRequest userLoginRequest) {
        UserItem userItem = userLoginRequest.getUser();
        MetricItem metricItem = userLoginRequest.getMetric();

        UserDTO userDTO = new UserDTO(null, userItem.getLogin(), userItem.getPassword());
        MetricDTO metricDTO = new MetricDTO(UUID.randomUUID(),
                metricItem.getSpeed(),
                metricItem.getError(),
                metricItem.getTime(),
                metricItem.getAveragePause(),
                null);

        UUID userId;
        if (isKeyloggerAuth){
            userId = userService.getAuthWithMetric(userDTO, metricDTO);
        } else {
            userId = userService.getAuthWithoutMetric(userDTO, metricDTO);
        }

        UserLoginResponse userLoginResponse = new UserLoginResponse(String.valueOf(userId), userDTO.getLogin(), true);
        if (userId == null) {
            userLoginResponse.setIsLogin(false);
            userLoginResponse.setLogin("Anonymous");
        }
        return ResponseEntity.ok(userLoginResponse);
    }
}
