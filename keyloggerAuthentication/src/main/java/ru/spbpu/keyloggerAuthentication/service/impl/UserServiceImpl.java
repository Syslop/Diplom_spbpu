package ru.spbpu.keyloggerAuthentication.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spbpu.keyloggerAuthentication.dto.MetricDTO;
import ru.spbpu.keyloggerAuthentication.dto.UserDTO;
import ru.spbpu.keyloggerAuthentication.repository.MetricRepository;
import ru.spbpu.keyloggerAuthentication.repository.UserRepository;
import ru.spbpu.keyloggerAuthentication.service.UserService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private String scriptPath = "./predict.py";

    private String pythonInterpreter = "./python.exe";

    private final UserRepository userRepository;

    private final MetricRepository metricRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, MetricRepository metricRepository) {
        this.userRepository = userRepository;
        this.metricRepository = metricRepository;
    }

    @Override
    public UUID addUser(UserDTO userDTO, MetricDTO metricDTO) {
        UUID userId = userRepository.add(userDTO);

        metricRepository.add(metricDTO);
        return userId;
    }

    @Override
    public UUID getAuthWithMetric(UserDTO userDTO, MetricDTO metricDTO) {
        UUID userId = userRepository.getUserByLoginAndPassword(userDTO.getLogin(), userDTO.getPassword());
        String userIdFromAI = predictUserId(metricDTO);
        if (userId.toString().equals(userIdFromAI)) {
            metricDTO.setUserId(userId);
            metricRepository.add(metricDTO);
            return userId;
        } else {
            return null;
        }
    }

    @Override
    public UUID getAuthWithoutMetric(UserDTO userDTO, MetricDTO metricDTO) {
        UUID userId = userRepository.getUserByLoginAndPassword(userDTO.getLogin(), userDTO.getPassword());
        metricDTO.setUserId(userId);
        metricRepository.add(metricDTO);
        return userId;
    }

    public String predictUserId(MetricDTO metricDTO) {
        try {
            String metricsJson = String.format(
                    "\"{\\\"Speed\\\": %f, \\\"Error\\\": %f, \\\"Time\\\": %f, \\\"Average Pause\\\": %f}\"",
                    metricDTO.getSpeed(), metricDTO.getError(), metricDTO.getTime(), metricDTO.getAveragePause()
            );

            List<String> command = new ArrayList<>();
            command.add(pythonInterpreter);
            command.add(scriptPath);
            command.add(metricsJson);

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Python script exited with code: " + exitCode);
            }

            String userIdFromAI = "";
            if (output.length() >= 36) {
                userIdFromAI = output.substring(output.length() - 36);
            } else {
                System.out.println("String is shorter than 36 characters.");
            }
            return userIdFromAI;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
