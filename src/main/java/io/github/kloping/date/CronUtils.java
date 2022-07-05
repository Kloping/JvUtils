package io.github.kloping.date;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author github.kloping
 */
public class CronUtils {

    public static CronUtils INSTANCE = new CronUtils();

    public static final SchedulerFactory SCHEDULER_FACTORY = new StdSchedulerFactory();

    public Map<Integer, Scheduler> id2Scheduler = new HashMap<>();

    public void stopAll() {
        id2Scheduler.forEach((k, v) -> {
            try {
                v.shutdown(false);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        });
        id2Scheduler.clear();
    }

    public Integer addCronJob(String cron, Job job) {
        try {
            Scheduler scheduler = SCHEDULER_FACTORY.getScheduler();
            JobBuilder jobBuilder = JobBuilder.newJob(CronJob.class);
            jobBuilder.withIdentity("default-name", "default-group");
            JobDataMap map = new JobDataMap();
            map.put("job", job);
            jobBuilder.setJobData(map);
            JobDetail jobDetail = jobBuilder.build();
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity("default-name", "default-group").startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                    .build();
            scheduler.scheduleJob(jobDetail, cronTrigger);
            scheduler.start();
            int id = getId();
            id2Scheduler.put(id, scheduler);
            return id;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Scheduler stop(Integer id) {
        if (id2Scheduler.containsKey(id)) {
            Scheduler scheduler = id2Scheduler.get(id);
            try {
                scheduler.shutdown(false);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
            return scheduler;
        }
        return null;
    }

    private int id = 0;

    public synchronized Integer getId() {
        return ++id;
    }
}
