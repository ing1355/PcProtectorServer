package oms.pc_protector.Scheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import oms.pc_protector.restApi.policy.mapper.ConfigurationMapper;
import oms.pc_protector.restApi.policy.model.PeriodDateVO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SchedulerService {
    private final ConfigurationMapper configurationMapper;
    public SchedulerService(ConfigurationMapper configurationMapper) {
        this.configurationMapper = configurationMapper;
    }

    @PostConstruct
    public void onStartup() {
        System.out.println("test start");
    }

    @Scheduled(cron ="0 0 0 6 6 *")
    public void cronJobSch() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        PeriodDateVO schedule_period = configurationMapper.selectAppliedSchedule();
        Calendar c = Calendar.getInstance();
        Date now = new Date();
        String strDate = sdf.format(now);
        System.out.println("Java cron job expression:: " + c.getTime());
    }
}