package com.example.ds_proj.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@Repository
public class DBRepository {
    @Autowired
    public Properties dbProperties;

    public Map<String, Object[][]> getDataSource(String table, int rn, int cn, int sol_id) {
        System.out.println("START : " + table);
        Object[][] cols = new Object[1][cn];
        Object[][] rows = null;
        Map<String, Object[][]> data = new HashMap<>();
        try {
            String filePath = getFilePath(table, sol_id);
            ClassPathResource resource = new ClassPathResource(filePath);
            int rowCount = -1;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (rowCount == -1) cols[0] = line.split(",");
                    else {String[] values = parseLine(line);
                        if (rows == null) rows = initializeRows(table, rn, cn);
                        rows[rowCount] = processRow(table, values);}
                    rowCount++;}}
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("DONE : " + table);
        data.put("cols", cols);data.put("rows", rows);
        return data;
    }

    private Object[][] initializeRows(String table, int rn, int cn) {
        if (table.equals("EncodedVar_st")) return new Character[rn][cn];
        else return new String[rn][cn];

    }

    private Object[] processRow(String table, String[] values) {
        if (table.equals("EncodedVar_st")) return replace01toChar(values);
        else return values;
    }

    private String getFilePath(String table, int sol_id) {
        if (table.equals("Solution_odl") || table.equals("Var_odl"))
            return dbProperties.getProperty(
                    table.toUpperCase()) +
                    "/" + table + "_" + getFolderRange(sol_id) +
                    "/" + table + "_" + getFileRange(sol_id) + ".csv";
        else return dbProperties.getProperty(table.toUpperCase());
    }

    private String getFolderRange(int sol_id) {
        int folderIndex = sol_id / 1000;
        return folderIndex + "to" + ((folderIndex + 1) * 1000 - 1);
    }

    private String getFileRange(int sol_id) {
        int folderIndex = sol_id / 10;
        return folderIndex * 10 + "to" + ((folderIndex + 1) * 10 - 1);
    }


    private Character[] replace01toChar(String[] values) {
        Character[] booleanValues = new Character[values.length];
        for (int i = 0; i < values.length; i++) booleanValues[i] = values[i].charAt(0);
        return booleanValues;
    }


    private Integer[] parseInteger(String[] values) {
        return Arrays.stream(values)
                .map(s -> s.replaceAll("[^0-9.-e]", "").trim())
                .filter(s -> !s.isEmpty() && s.matches("^[+-]?\\d*(\\.\\d+)?([eE][+-]?\\d+)?$"))
                .map(Integer::valueOf)
                .toArray(Integer[]::new);
    }

    private String[] parseLine(String line) {
        String[] parsedLine = line.replaceAll("\"", "").split("(?<=\\S),(?=\\S)");
        String[] result = new String[parsedLine.length];
        for (int i = 0; i < result.length; i++) result[i] = parseElement(parsedLine[i]);
        return result;
    }

    private String parseElement(String element) {
        if (!element.isEmpty() && element.charAt(0) == '[') {
            element = element.replaceAll("[\\[\\]]", "");
            String[] eArr = element.split(", ");
            String[] result = new String[eArr.length];
            for (int i = 0; i < eArr.length; i++) result[i] = roudDouble(eArr[i]);
            return Arrays.toString(result);
        }
        return element;
    }

    private String roudDouble(String value) {
        try {
            BigDecimal number = new BigDecimal(value);
            DecimalFormat df = new DecimalFormat("#.#####");
            df.setMaximumFractionDigits(5);
            df.setMinimumFractionDigits(0);
            return df.format(number);
        } catch (NumberFormatException e) {
            return value;
        }
    }
//    private Double[] parseDouble(String[] values) {
//        return Arrays.stream(values)
//                .map(s -> s.replaceAll("[^0-9.-e]", "").trim())
//                .filter(s -> !s.isEmpty() && s.matches("^[+-]?\\d*(\\.\\d+)?([eE][+-]?\\d+)?$"))
//                .map(Double::valueOf)
//                .toArray(Double[]::new);
//    }
}
