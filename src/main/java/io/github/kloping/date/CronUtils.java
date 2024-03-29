package io.github.kloping.date;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author github.kloping
 */
public class CronUtils {

    public static CronUtils INSTANCE = new CronUtils();

    public static final StdSchedulerFactory SCHEDULER_FACTORY = new StdSchedulerFactory();

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

    public synchronized Integer addCronJob(String cron, Job job) {
        try {
            int id = getId();
            Properties props = new Properties();
            props.put("org.quartz.scheduler.instanceName", "kloping-cron-" + id);
            props.put("org.quartz.threadPool.threadCount", "10");
            SCHEDULER_FACTORY.initialize(props);
            Scheduler scheduler = SCHEDULER_FACTORY.getScheduler();
            JobBuilder jobBuilder = JobBuilder.newJob(CronJob.class);
            jobBuilder.withIdentity("default-name-" + id, "default-group-" + id);
            JobDataMap map = new JobDataMap();
            map.put("job", job);
            jobBuilder.setJobData(map);
            JobDetail jobDetail = jobBuilder.build();
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("default-name-" + id, "default-group-" + id).startNow().withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
            scheduler.scheduleJob(jobDetail, cronTrigger);
            scheduler.start();
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

    private static int id = 0;

    public static synchronized Integer getId() {
        return ++id;
    }
}
