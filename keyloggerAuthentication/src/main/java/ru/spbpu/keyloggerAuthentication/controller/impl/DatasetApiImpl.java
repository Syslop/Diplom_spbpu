package ru.spbpu.keyloggerAuthentication.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.spbpu.keyloggerAuthentication.dto.MetricDTO;
import ru.spbpu.keyloggerAuthentication.openapi.api.DatasetApi;

import com.opencsv.CSVWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import ru.spbpu.keyloggerAuthentication.service.DatasetService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
public class DatasetApiImpl implements DatasetApi {
    private final DatasetService datasetService;

    @Autowired
    public DatasetApiImpl(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    @Override
    public ResponseEntity<Resource> getDataset() {
        List<MetricDTO> metrics = datasetService.getAllMetrics();

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(byteArrayOutputStream);
            CSVWriter csvWriter = new CSVWriter(writer);

            // Write CSV header
            String[] header = { "Speed", "Error", "Time", "Average Pause", "User ID" };
            csvWriter.writeNext(header);

            // Write data
            for (MetricDTO metric : metrics) {
                String[] data = {
                        String.valueOf(metric.getSpeed()),
                        String.valueOf(metric.getError()),
                        String.valueOf(metric.getTime()),
                        String.valueOf(metric.getAveragePause()),
                        metric.getUserId().toString()
                };
                csvWriter.writeNext(data);
            }

            csvWriter.close();

            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.csv");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(resource);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}