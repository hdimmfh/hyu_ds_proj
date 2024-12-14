
package com.example.ds_proj.controller;

import com.example.ds_proj.service.QueryService;
import com.example.ds_proj.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    private final QueryService dbQueryService;
    private final ReportService reportService;

    @Autowired
    public ReportController(QueryService dbQueryService, ReportService reportService) {
        this.dbQueryService = dbQueryService;
        this.reportService = reportService;
    }

    /**
     * - 히스토그램
     * 1. 목적식의 시각화
     */
    @ResponseBody
    @GetMapping("histogram/{first}/{last}")
    public ResponseEntity<byte[]> getHistogram(@PathVariable("first") int first, @PathVariable("last") int last) {
        Object[][] data = dbQueryService.getScenario(first, last);
        return reportService.getHistogramImage(data);
    }


    /**
     * - 히스토그램
     * 2. 특정 변수가 포함되어있는 목적식의 시각화
     */
    @ResponseBody
    @GetMapping("histogram/{x}/{first}/{last}")
    public ResponseEntity<byte[]> getHistogramWithX(@PathVariable("x") int x, @PathVariable("first") int first, @PathVariable("last") int last) {
        Integer[] solIdList = dbQueryService.getUsedSolIds(first, last, x);
        Object[][] data = dbQueryService.getScenarioWithX(first, last, solIdList);
        return reportService.getHistogramImage(data);
    }

    /**
     * - 히스토그램
     * 2. 최대 또는 최소 n개 목적식의 시각화
     */
    @ResponseBody
    @GetMapping("histogram/{maxOrMin}/{n}/{first}/{last}")
    public ResponseEntity<byte[]> getMinMaxHistogram(@PathVariable("maxOrMin") String maxOrMin, @PathVariable("n") int n, @PathVariable("first") int first, @PathVariable("last") int last) {
        Object[][] data = dbQueryService.getMaxOrMinScenario(first, last, n, maxOrMin.equalsIgnoreCase("max"));
        return reportService.getHistogramImage(data);
    }



}
