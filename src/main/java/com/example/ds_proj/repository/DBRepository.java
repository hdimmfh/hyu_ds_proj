package com.example.ds_proj.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Repository
public class DBRepository {
    @Autowired
    public Properties dbProperties;

    @SuppressWarnings("unchecked")
    public Map<String, Object[][]> getDataSource(String table, int rn, int cn, int sol_id) {
        String[][] cols = new String[1][cn];
        Object[][] rows = null;

        Map<String, Object[][]> data = new HashMap<>();
        try {
            String filePath = getFilePath(table, sol_id);
            System.out.println(filePath);
            ClassPathResource resource = new ClassPathResource(filePath);

            int rowCount = -1;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    if (rowCount == -1) cols[0] = values;
                    else if (table.equals("EncodedVar_st")) {
                        if (rows == null) rows = new Character[rn][cn];
                        rows[rowCount] = replace01toBooleans(values);
                    } else {
                        if (rows == null) rows = new String[rn][cn];
                        rows[rowCount] = values;
                    }
                    rowCount++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        data.put("cols", cols);
        data.put("rows", rows);
        return data;
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


    private Character[] replace01toBooleans(String[] values) {
        Character[] booleanValues = new Character[values.length];
        for (int i = 0; i < values.length; i++) booleanValues[i] = values[i].charAt(0);
        return booleanValues;
    }


}
