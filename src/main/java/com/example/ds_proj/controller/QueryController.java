package com.example.ds_proj.controller;

import com.example.ds_proj.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/query")
public class QueryController {
    private final QueryService dbQueryService;

    @Autowired
    @Lazy
    QueryController(QueryService dbQueryService) {
        this.dbQueryService = dbQueryService;
    }

    @GetMapping("ping")
    public Object ping() {
        return "pong";
    }

    @ResponseBody
    @GetMapping("sol/{sol_id}")
    public Object querySolution(@PathVariable("sol_id") int sol_id) {
        return dbQueryService.getSolution(sol_id).get("rows")[sol_id % 10];
    }

    @ResponseBody
    @GetMapping("var/{sol_id}")
    public Object[] queryVar(@PathVariable("sol_id") int sol_id) {
        return dbQueryService.getVar(sol_id).get("rows")[sol_id % 10];
    }

    @ResponseBody
    @GetMapping("encoded_var/{first}/{last}")
    public Object[][] queryEncodedVar(@PathVariable("first") int first, @PathVariable("last") int last) {
        return dbQueryService.getEncodedVar(first, last);
    }

    @ResponseBody
    @GetMapping("scenario/{first}/{last}")
    public Object[][] queryScenario(@PathVariable("first") int first, @PathVariable("last") int last) {
        return dbQueryService.getScenario(first, last);
    }

    @ResponseBody
    @GetMapping("scenario/{maxOrMin}/{n}/{first}/{last}")
    public Object[][] queryMinMaxNScenario(@PathVariable("maxOrMin") String maxOrMin, @PathVariable("n") int n,
                       @PathVariable("first") int first, @PathVariable("last") int last) {
        return dbQueryService.getMaxOrMinScenario(first, last, n, maxOrMin.equalsIgnoreCase("max"));
    }

    @ResponseBody
    @GetMapping("scenario/{x}/{maxOrMin}/{n}/{first}/{last}")
    public Object[][] queryMinMaxNScenarioWithX(@PathVariable("x") int x, @PathVariable("maxOrMin") String maxOrMin,
                        @PathVariable("n") int n, @PathVariable("first") int first, @PathVariable("last") int last) {
        Integer[] solIdList = dbQueryService.getUsedSolIds(first, last, x);
        return dbQueryService.getMaxOrMinScenarioWithX(first, last, n, solIdList, maxOrMin.equalsIgnoreCase("max"));
    }
}
