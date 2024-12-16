
package com.example.ds_proj.controller;

import com.example.ds_proj.service.QueryService;
import com.example.ds_proj.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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


    @ResponseBody
    @GetMapping("histogram/{first}/{last}")
    public ResponseEntity<byte[]> getHistogram(@PathVariable("first") int first, @PathVariable("last") int last) {
        Object[][] data = dbQueryService.getScenario(first, last);
        return reportService.getHistogramImage(data);
    }

    @ResponseBody
    @GetMapping("histogram/{x}/{first}/{last}")
    public ResponseEntity<byte[]> getHistogramWithX(@PathVariable("x") int x
            , @PathVariable("first") int first, @PathVariable("last") int last) {
        Integer[] solIdList = dbQueryService.getUsedSolIds(first, last, x);
        Object[][] data = dbQueryService.getScenarioWithX(first, last, solIdList);
        return reportService.getHistogramImage(data);
    }

    @ResponseBody
    @GetMapping("histogram/{maxOrMin}/{n}/{first}/{last}")
    public ResponseEntity<byte[]> getMinMaxNHistogram(@PathVariable("maxOrMin") String maxOrMin
            , @PathVariable("n") int n, @PathVariable("first") int first, @PathVariable("last") int last) {
        Object[][] data = dbQueryService.getMaxOrMinScenario(first, last, n, maxOrMin.equalsIgnoreCase("max"));
        return reportService.getHistogramImage(data);
    }

    @ResponseBody
    @GetMapping("histogram/{x}/{maxOrMin}/{n}/{first}/{last}")
    public ResponseEntity<byte[]> getMinMaxNHistogramWithX(@PathVariable("maxOrMin") String maxOrMin
            , @PathVariable("x") int x, @PathVariable("n") int n
            , @PathVariable("first") int first, @PathVariable("last") int last) {
        Integer[] solIdList = dbQueryService.getUsedSolIds(first, last, x);
        Object[][] data = dbQueryService.getMaxOrMinScenarioWithX(first, last, n, solIdList,
                maxOrMin.equalsIgnoreCase("max"));
        return reportService.getHistogramImage(data);
    }


}
