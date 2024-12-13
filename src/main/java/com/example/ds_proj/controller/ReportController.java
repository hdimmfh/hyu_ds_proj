package com.example.ds_proj.controller;

import com.example.ds_proj.service.DBQueryService;
import com.example.ds_proj.service.VisualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/visual")
public class ReportController {
    private final DBQueryService dbQueryService;
    private final VisualService visualService;

    @Autowired
    public ReportController(DBQueryService dbQueryService, VisualService visualService) {
        this.dbQueryService = dbQueryService;
        this.visualService = visualService;
    }

    /**
     * - 히스토그램
     * 1. 목적식의 시각화
     */
    @ResponseBody
    @GetMapping("histogram/{first}/{last}")
    public ResponseEntity<byte[]> getHistogram(@PathVariable("first") int first, @PathVariable("last") int last) {
        Object[][] data = dbQueryService.getScenario(first, last);
        return visualService.getHistogramImage(data);
    }


    /**
     * - 히스토그램
     * 2. 특정 변수가 포함되어있는 목적식의 시각화
     */
    @ResponseBody
    @GetMapping("histogram/{x}/{first}/{last}")
    public ResponseEntity<byte[]> getHistogramWithX(@PathVariable("x") int x, @PathVariable("first") int first, @PathVariable("last") int last) {
        Integer[] solIdList = dbQueryService.getUsedSolIds(first, last, x);
        Object[][] data = dbQueryService.getScenario(first, last, solIdList);
        return visualService.getHistogramImage(data);
    }

    /**
     * - 히스토그램
     * 2. 최대 또는 최소 n개 목적식의 시각화
     */
    @ResponseBody
    @GetMapping("histogram/{maxOrMin}/{n}/{first}/{last}")
    public ResponseEntity<byte[]> getMinMaxHistogram(@PathVariable("maxOrMin") String maxOrMin,@PathVariable("n") int n, @PathVariable("first") int first, @PathVariable("last") int last) {
        Object[][] data = dbQueryService.getMaxOrMinScenario(first, last, n, maxOrMin.equalsIgnoreCase("max"));
        return visualService.getHistogramImage(data);
    }
}
