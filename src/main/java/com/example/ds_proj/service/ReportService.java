package com.example.ds_proj.service;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

@Service
public class ReportService {

    public ResponseEntity<byte[]> getHistogramImage(Object[][] data) {
        try {
            double[] yValues = extractColumnAsDouble(data, 2);

            if (yValues.length == 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: No valid data points for histogram.".getBytes());


            HistogramDataset dataset = new HistogramDataset();
            dataset.addSeries("Primal Objective", yValues, 50);

            JFreeChart histogram = ChartFactory.createHistogram(
                    "Primal Objective Histogram",  // 제목
                    "Primal Objective",             // X축 레이블
                    "Frequency",           // Y축 레이블
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,                 // 범례 표시 여부
                    true,                 // 툴팁 표시 여부
                    true                  // URL 생성 여부
            );

            BufferedImage chartImage = histogram.createBufferedImage(800, 600);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(chartImage, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/png");
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error generating histogram: " + e.getMessage()).getBytes());
        }
    }


    /**
     * 주어진 Object[][] 데이터에서 특정 열(index)을 double 배열로 추출
     * null 또는 비정상 데이터를 기본값 0으로 처리
     */
    private double[] extractColumnAsDouble(Object[][] data, int columnIndex) {
        return Arrays.stream(data)
                .filter(row -> row != null && row.length > columnIndex)  // Ensure row is not null and columnIndex exists
                .map(row -> row[columnIndex])                             // Extract the column value
                .filter(value -> value != null)                           // Ensure the column value is not null
                .filter(value -> {
                    try {
                        Double.parseDouble(value.toString().trim());  // Attempt to parse the value as double
                        return true;                                  // Keep if it's a valid double
                    } catch (NumberFormatException e) {
                        return false;                                 // Discard if it's not a valid double
                    }
                })
                .mapToDouble(value -> Double.parseDouble(value.toString().trim()))  // Convert to double
                .toArray();  // Return the double array
    }

}
