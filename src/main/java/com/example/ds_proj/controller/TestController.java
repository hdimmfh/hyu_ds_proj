package com.example.ds_proj.controller;

import com.example.ds_proj.service.DBQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class TestController {
    private final DBQueryService dbQueryService;

    @Autowired
    @Lazy
    TestController(DBQueryService dbQueryService) {
        this.dbQueryService = dbQueryService;
    }

    @GetMapping("ping")
    public Object ping() {
        return "pong";
    }

    @ResponseBody
    @GetMapping("sol/{sol_id}")
    public Object testGetSolutionOdl(@PathVariable("sol_id") int sol_id) {
        return dbQueryService.getSolutionOdl(sol_id).get("rows")[sol_id % 10];
    }

    @ResponseBody
    @GetMapping("var/{sol_id}")
    public Object[] testGetVarOdl(@PathVariable("sol_id") int sol_id) {
        return dbQueryService.getVarOdl(sol_id).get("rows")[sol_id % 10];
    }

    @ResponseBody
    @GetMapping("encoded_var/{first}/{last}")
    public Object[][] testEncodedVar(@PathVariable("first") int first, @PathVariable("last") int last) {
        return dbQueryService.getEncodedVar(first, last);
    }

    @ResponseBody
    @GetMapping("scenario/{first}/{last}")
    public Object[][] testScenario(@PathVariable("first") int first, @PathVariable("last") int last) {
        return dbQueryService.getScenario(first, last);
    }

    @ResponseBody
    @GetMapping("scenario/{maxOrMin}/{n}/{first}/{last}")
    public Object[][] testMaxScenario(@PathVariable("maxOrMin") String maxOrMin,@PathVariable("n") int n, @PathVariable("first") int first, @PathVariable("last") int last) {
        return dbQueryService.getMaxOrMinScenario(first, last, n, maxOrMin.equalsIgnoreCase("max"));
    }
}