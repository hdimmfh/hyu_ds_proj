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
public class VisualService {

    public ResponseEntity<byte[]> getHistogramImage(Object[][] data) {
        try {
            double[] yValues = extractColumnAsDouble(data, 2);

            if (yValues.length == 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: No valid data points for histogram.".getBytes());


            HistogramDataset dataset = new HistogramDataset();
            dataset.addSeries("Y Values", yValues, 50);

            JFreeChart histogram = ChartFactory.createHistogram(
                    "Y Values Histogram",  // 제목
                    "Y Value",             // X축 레이블
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
                .map(row -> row[columnIndex])      // 해당 열 값 추출
                .filter(value -> value != null)    // null 값 제외
                .filter(value -> {
                    try {
                        Double.parseDouble(value.toString().trim());
                        return true;              // 변환 가능하면 유지
                    } catch (NumberFormatException e) {
                        return false;             // 변환 불가능하면 제외
                    }
                })
                .mapToDouble(value -> Double.parseDouble(value.toString().trim())) // double 변환
                .toArray();
    }

}
