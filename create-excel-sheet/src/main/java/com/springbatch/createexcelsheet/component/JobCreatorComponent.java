package com.springbatch.createexcelsheet.component;

import com.springbatch.createexcelsheet.component.step.StepToReadDBAndCreateExcelFile;
import com.springbatch.createexcelsheet.listner.JobCompletionNotificationListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.JobFlowBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

public class JobCreatorComponent {
    private StepToReadDBAndCreateExcelFile stepToCreateFile;
    private JobBuilderFactory jobBuilderFactory;
    private JobCompletionNotificationListener jobCompletionNotificationListener;

    public JobCreatorComponent(StepToReadDBAndCreateExcelFile stepToCreateFile, final JobBuilderFactory jobBuilderFactory,
                               final JobCompletionNotificationListener jobCompletionNotificationListener) {
        this.stepToCreateFile = stepToCreateFile;
        this.jobBuilderFactory = jobBuilderFactory;
        this.jobCompletionNotificationListener = jobCompletionNotificationListener;
    }

    public Job createJob() {
        final JobFlowBuilder flowBuilder = jobBuilderFactory
                .get("CreateExcelFile")
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionNotificationListener)
                .flow(stepToCreateFile.createStep()); //need to add steps

        return flowBuilder.end().build();
    }
}
