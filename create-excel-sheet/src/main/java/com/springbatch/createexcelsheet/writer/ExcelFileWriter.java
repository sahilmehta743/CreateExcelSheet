package com.springbatch.createexcelsheet.writer;

import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.core.io.WritableResource;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExcelFileWriter implements ItemWriter {
    private static final String COMMA = ",";
    private XSSFWorkbook workbook;

    @Setter
    private WritableResource resource;
    @Setter
    private String header;
    @Setter
    private String sheetName;

    @BeforeStep
    public void open() {
        workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        createHeaderRow(sheet);
    }

    private void createHeaderRow(final XSSFSheet sheet) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);

        XSSFRow sheetRow = sheet.createRow(0);
        sheetRow.setRowStyle(cellStyle);
        int columnIndex = 0;
        final List<String> columnNames = Arrays
                .stream(header.split(COMMA))
                .collect(Collectors.toList());
        for (String columnName : columnNames) {
            Cell cell = sheetRow.createCell(columnIndex);
            cell.setCellValue(columnName);
            columnIndex++;
        }
    }

    @Override
    public void write(List items) throws Exception {
        XSSFSheet sheet = workbook.getSheetAt(0);
        for (Object rowData : items) {
            Map<Object, Object> rowDetails = (Map<Object, Object>) rowData;
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            int cellNum = 0;
            for (Map.Entry<Object, Object> cellData : rowDetails.entrySet()) {
                Object cellValue = cellData.getValue();
                Cell cell = row.createCell(cellNum++);
                cell.setCellValue(String.valueOf(cellValue));
            }
        }
    }

    @AfterStep
    public void close() {
        if (workbook == null) {
            return;
        }
        try (BufferedOutputStream bufferedInputStream = new BufferedOutputStream(resource.getOutputStream())) {
            workbook.write(bufferedInputStream);
            bufferedInputStream.flush();
        } catch (IOException ioException) {
            throw new ItemStreamException("Error writing to output file", ioException);
        }
    }
}
