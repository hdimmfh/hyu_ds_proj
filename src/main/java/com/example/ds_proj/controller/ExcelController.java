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

    @ResponseBody
    @GetMapping("sol/{sol_id}")
    public ResponseEntity<byte[]> getSolutionExcel(@PathVariable("sol_id") int sol_id) {
        return excelService.getExcel("Solution_Odl", sol_id);
    }

    @ResponseBody
    @GetMapping("scenario/{x}")
    public ResponseEntity<byte[]> getScenarioExcelWithX(@PathVariable("x") int x) {
        return excelService.getExcel("Scenario_ap_st", x);
    }

}
