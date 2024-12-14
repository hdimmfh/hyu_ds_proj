package com.example.ds_proj.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

@Service
public class ExcelService {

    @Autowired
    QueryService dbQueryService;


    public ResponseEntity<byte[]>  clusterSolutionExcel(int sol_id) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Solution_Odl");
            CellStyle style = setWorkBookStyle(workbook);

            Object[] colData = dbQueryService.getSolutionOdl(sol_id).get("cols")[0];
            Object[][] rowData = dbQueryService.getSolutionOdl(sol_id).get("rows");
            for (int i = 0; i < rowData.length; i++) {
                Row columnRow = sheet.createRow(i * 2);
                Row rowRow = sheet.createRow(i * 2 + 1);
                for (int j = 0; j < colData.length; j++) {
                    Cell colCell = columnRow.createCell(j);
                    setCellColor(style, colCell);
                    colCell.setCellValue(colData[j].toString());

                    Cell rowCell = rowRow.createCell(j);
                    if (rowData[i].length > j && !Objects.equals(rowData[i][j].toString(), ","))
                        rowCell.setCellValue(rowData[i][j].toString());
                }
            }

            // Convert to ByteArray
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=Solution_Odl_sol_id.xlsx");
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
}
