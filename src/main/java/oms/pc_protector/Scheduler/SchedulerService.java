package oms.pc_protector.Scheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.client.mapper.ClientMapper;
import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.dashboard.model.DashboardPeriodVO;
import oms.pc_protector.restApi.dashboard.service.DashboardService;
import oms.pc_protector.restApi.policy.mapper.ConfigurationMapper;
import oms.pc_protector.restApi.policy.model.PeriodDateVO;
import oms.pc_protector.restApi.result.mapper.ResultMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Log4j2
@Component
public class SchedulerService {
    private final ConfigurationMapper configurationMapper;
    private final ResultMapper resultMapper;
    private final ClientMapper clientMapper;
    private final DashboardService dashboardService;

    public SchedulerService(ConfigurationMapper configurationMapper,
                            ResultMapper resultMapper,
                            ClientMapper clientMapper,
                            DashboardService dashboardService) {
        this.clientMapper = clientMapper;
        this.resultMapper = resultMapper;
        this.configurationMapper = configurationMapper;
        this.dashboardService = dashboardService;
    }

    @PostConstruct
    public void onStartup() {
        cronJobSch();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cronJobSch() {
        PeriodDateVO Now_Schedule = configurationMapper.selectAppliedSchedule();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        List<ClientVO> temp = clientMapper.selectClientAll();

        if (Now_Schedule.getPeriod() == 1) { // 매달
            start.set(Calendar.WEEK_OF_MONTH, Now_Schedule.getFromWeek());
            start.set(Calendar.DAY_OF_WEEK, Now_Schedule.getFromDay() + 1);
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            end.set(Calendar.WEEK_OF_MONTH, Now_Schedule.getToWeek());
            end.set(Calendar.DAY_OF_WEEK, Now_Schedule.getToDay() + 1);
            end.set(Calendar.HOUR_OF_DAY, 23);
            end.set(Calendar.MINUTE, 59);
            end.set(Calendar.SECOND, 59);
            log.info(df.format(start.getTime()));
            log.info(df.format(now.getTime()));
            log.info(Now_Schedule.getFromDay());
            log.info(df.format(end.getTime()));
            log.info(Now_Schedule.getToDay());
        } else if (Now_Schedule.getPeriod() == 2) { // 매주

            start.set(Calendar.DAY_OF_WEEK, Now_Schedule.getFromDay() + 1);
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            end.set(Calendar.DAY_OF_WEEK, Now_Schedule.getToDay() + 1);
            end.set(Calendar.HOUR_OF_DAY, 23);
            end.set(Calendar.MINUTE, 59);
            end.set(Calendar.SECOND, 59);
            log.info(df.format(now.getTime()));
            log.info(Now_Schedule.getFromDay());
            log.info(df.format(end.getTime()));
            log.info(Now_Schedule.getToDay());
        } else { // 매일
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            end.set(Calendar.HOUR_OF_DAY, 23);
            end.set(Calendar.MINUTE, 59);
            end.set(Calendar.SECOND, 59);
            log.info(df.format(start.getTime()));
            log.info(df.format(end.getTime()));
        }

        if (df.format(start.getTime()).equals(df.format(now.getTime()))) {
            dashboardService.dashboardPeriodUpdate(new DashboardPeriodVO(df.format(start.getTime()), df.format(end.getTime())));
            for (ClientVO client : temp) {
                if (resultMapper.selectByScheduleIsExist((dft.format(now.getTime())),
                        dft.format(end.getTime()), client.getUserId(), client.getIpAddress()) == 0) {
                    client.setCheckTime(dft.format(start.getTime()));
                    resultMapper.insertEmptyResultBySchedule(client);
                }
            }
        }

    }
}