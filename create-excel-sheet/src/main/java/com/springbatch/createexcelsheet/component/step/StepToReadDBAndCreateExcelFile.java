package com.springbatch.createexcelsheet.component.step;

import com.springbatch.createexcelsheet.listner.NoRecordFoundListener;
import com.springbatch.createexcelsheet.service.ExcelFileWriterService;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.jdbc.core.ColumnMapRowMapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StepToReadDBAndCreateExcelFile {

    private final StepBuilderFactory stepBuilderFactory;
    private final NoRecordFoundListener noRecordFoundListener;
    private final ExcelFileWriterService excelFileWriterService;

    public StepToReadDBAndCreateExcelFile(StepBuilderFactory stepBuilderFactory,
                                          NoRecordFoundListener noRecordFoundListener,
                                          ExcelFileWriterService excelFileWriterService) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.noRecordFoundListener = noRecordFoundListener;
        this.excelFileWriterService = excelFileWriterService;
    }

    public static void main(String[] args) {
        List<String> names = Arrays.asList("Angela", "Aaron", "Bob", "Claire", "David");

        Stream<String> stringStream = names.stream()
                .filter(name -> name.startsWith("A"));
        stringStream.collect(Collectors.toList())
    }

    public Step createStep() {
        return stepBuilderFactory.get("CreateExcelFile")
                .listener(noRecordFoundListener)
                .chunk(10)
                //.reader()
                .writer(excelFileWriterService.write())
                .build();
    }

    public JdbcCursorItemReader jdbcCursorItemReader() {
        final JdbcCursorItemReader itemReader = new JdbcCursorItemReader();
        ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();
        return itemReader;
    }
}
