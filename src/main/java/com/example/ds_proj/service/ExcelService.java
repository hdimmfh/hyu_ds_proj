package com.example.ds_proj.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Objects;

@Service
public class ExcelService {

    @Autowired
    QueryService dbQueryService;

    @Autowired
    Map<String, Object[][]> scenario;

    public ResponseEntity<byte[]> getExcel(String table, int id) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(table);
            CellStyle style = setWorkBookStyle(workbook);

            if (table.equals("Solution_Odl")) setSolutionWorkbook(sheet, style, id);
            else if (table.equals("Scenario_ap_st")) setScenarioWorkbook(sheet, style, id);
            else throw new RuntimeException("Error occurred (getExcel) :  wrong table name");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + table + "_sol_id.xlsx");
            return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void setCellColor(CellStyle style, Cell cell) {
        cell.setCellStyle(style);
    }

    private CellStyle setWorkBookStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private void setSolutionWorkbook(Sheet sheet, CellStyle style, int sol_id) {
        Object[] colData = dbQueryService.getSolution(sol_id).get("cols")[0];
        Object[][] rowData = dbQueryService.getSolution(sol_id).get("rows");

        // 첫 번째 행: 열 제목 설정
        Row columnRow = sheet.createRow(0);
        for (int i = 0; i < colData.length; i++) {
            Cell colCell = columnRow.createCell(i);
            setCellColor(style, colCell);
            colCell.setCellValue(colData[i] != null ? colData[i].toString() : "");
        }

        for (int i = 0; i < rowData.length; i++) {
            Row rowRow = sheet.createRow(i + 1);
            for (int j = 0; j < rowData[i].length; j++) {
                Cell rowCell = rowRow.createCell(j);
                rowCell.setCellValue(rowData[i][j].toString());
            }
        }
    }


    private void setScenarioWorkbook(Sheet sheet, CellStyle style, int x) {
        Object[] colData = scenario.get("cols")[0];

        Integer[] solIdList = dbQueryService.getUsedSolIds(0, 79999, x);
        Object[][] rowData = dbQueryService.getScenarioWithX(0, 79999, solIdList);

        // 첫 번째 행: 열 제목 설정
        Row columnRow = sheet.createRow(0);
        for (int i = 0; i < colData.length; i++) {
            Cell colCell = columnRow.createCell(i);
            setCellColor(style, colCell);
            colCell.setCellValue(colData[i] != null ? colData[i].toString() : "");
        }

        // 나머지 행: 데이터 채우기
        for (int i = 0; i < rowData.length; i++) {
            Row rowRow = sheet.createRow(i + 1); // 1번 행부터 시작
            for (int j = 0; j < rowData[i].length; j++) {
                Cell rowCell = rowRow.createCell(j);
                rowCell.setCellValue(rowData[i][j] != null ? rowData[i][j].toString() : "");
            }
        }
    }

}
