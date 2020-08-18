package com.springbatch.createexcelsheet.service;

import com.springbatch.createexcelsheet.writer.ExcelFileWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;

@Service
public class ExcelFileWriterService {
    public ItemWriter<Object> write() {
        ExcelFileWriter excelFileWriter = new ExcelFileWriter();
        excelFileWriter.setHeader("Data1,Data2,Data3");
        WritableResource resource = new FileSystemResource("excelfile.xlsx");
        excelFileWriter.setResource(resource);
        excelFileWriter.setSheetName("sheet1");
        return excelFileWriter;
    }
}
