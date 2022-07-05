package io.github.kloping.date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author HRS-Computer
 */
public class CronJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Object oJob = context.getJobDetail().getJobDataMap().get("job");
        if (oJob != null) {
            Job job = (Job) oJob;
            job.execute(context);
        }
    }
}