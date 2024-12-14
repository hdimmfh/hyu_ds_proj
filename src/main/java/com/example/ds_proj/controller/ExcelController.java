package com.example.ds_proj.controller;

import com.example.ds_proj.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/excel")
public class ExcelController {
    private final ExcelService excelService;

    @Autowired
    public ExcelController(ExcelService excelService) {
        this.excelService = excelService;
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("sol/{sol_id}")
    public ResponseEntity<byte[]> getClusterSolutionExcel(@PathVariable("sol_id") int sol_id) {
        return excelService.clusterSolutionExcel(sol_id);
    }


}
