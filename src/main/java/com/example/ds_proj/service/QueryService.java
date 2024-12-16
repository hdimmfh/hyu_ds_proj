package com.example.ds_proj.service;

import com.example.ds_proj.repository.DBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QueryService {
    @Autowired
    Map<String, Object[][]> scenario;
    @Autowired
    Map<String, Object[][]> encodedVar;
    @Autowired
    DBRepository dbRepository;


    public Map<String, Object[][]> getSolution(int sol_id) {
        return dbRepository.getDataSource("Solution_odl", 10, 14, sol_id);
    }

    public Map<String, Object[][]> getVar(int sol_id) {
        return dbRepository.getDataSource("Var_odl", 80000, 2, sol_id);
    }

    public Integer[] getUsedSolIds(int first, int last, int x) {
        if (x < 0 || 3000 <= x) return null;
        Object[][] subArray = getEncodedVar(first, last);
        if (subArray == null) return null;
        List<Integer> result = new ArrayList<>();
        int cnt = 0;
        for (Object[] row : subArray) {
            if (row[x + 1].toString().equals("1")) result.add(first + cnt);
            cnt++;
        }
        return result.toArray(new Integer[0]);
    }

    public Object[][] getEncodedVar(int first, int last) {
        if (first < 0 || 80000 <= last || first > last) return null;
        return Arrays.copyOfRange(encodedVar.get("rows"), first, last + 1);
    }

    public Object[][] getScenario(int first, int last) {
        if (first < 0 || 80000 <= last || first > last) return null;
        return Arrays.copyOfRange(scenario.get("rows"), first, last + 1);
    }

    public Object[][] getMaxOrMinScenario(int first, int last, int n, boolean isMax) {
        Object[][] subArray = getScenario(first, last);
        if (subArray == null) return null;
        Object[][] result = new Object[n][];
        PriorityQueue<Object[]> q = new PriorityQueue<>(Comparator.comparingDouble(y
                -> isMax ? -Double.parseDouble(y[2].toString()) : Double.parseDouble(y[2].toString())));
        for (Object[] row : subArray) {
            try {
                Double.parseDouble(row[2].toString()); q.add(row);
            } catch (NumberFormatException ignored) {}
        }
        for (int i = 0; i < n; i++) result[i] = q.poll();
        return result;
    }

    public Object[][] getMaxOrMinScenarioWithX(int first, int last, int n, Integer[] solIdList, boolean isMax) {
        Object[][] subArray = getScenarioWithX(first, last, solIdList);
        if (subArray == null) return null;
        Object[][] result = new Object[n][];
        PriorityQueue<Object[]> q = new PriorityQueue<>(Comparator.comparingDouble(y
                -> isMax ? -Double.parseDouble(y[2].toString()) : Double.parseDouble(y[2].toString())));
        for (Object[] row : subArray) {
            try {
                Double.parseDouble(row[2].toString());
                q.add(row);
            } catch (NumberFormatException ignored) {
            }
        }
        for (int i = 0; i < n; i++) result[i] = q.poll();
        return result;
    }


    public Object[][] getScenarioWithX(int first, int last, Integer[] solIdList) {
        if (first < 0 || 80000 <= last || first > last) return null;
        return Arrays.stream(scenario.get("rows")).filter(row -> {
            int rowIndex = Arrays.asList(scenario.get("rows")).indexOf(row);
            return rowIndex >= first && rowIndex <= last && contains(solIdList, rowIndex);
        }).toArray(Object[][]::new);
    }

    private boolean contains(Integer[] lst, int index) {
        for (Integer item : lst) if (item == index) return true;
        return false;
    }


}
