package com.springbatch.createexcelsheet.listner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

@Slf4j
public class NoRecordFoundListener extends StepExecutionListenerSupport {
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        final int readCount = stepExecution.getReadCount();
        ExitStatus exitStatus = null;
        if (readCount == 0 && stepExecution.getStatus() == BatchStatus.COMPLETED) {
            stepExecution.getJobExecution().setStatus(BatchStatus.FAILED);
            String errorMessage = "No record found in database !!!";
            log.error(errorMessage);
            Throwable throwable = new Throwable(errorMessage);
            stepExecution.getJobExecution().addFailureException(throwable);
            exitStatus = ExitStatus.FAILED;
        } else {
            exitStatus = stepExecution.getExitStatus();
        }
        return exitStatus;
    }
}
