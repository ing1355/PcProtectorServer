package oms.pc_protector.Scheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.client.mapper.ClientMapper;
import oms.pc_protector.restApi.client.model.ClientVO;
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

    public SchedulerService(ConfigurationMapper configurationMapper,
                            ResultMapper resultMapper,
                            ClientMapper clientMapper) {
        this.clientMapper = clientMapper;
        this.resultMapper = resultMapper;
        this.configurationMapper = configurationMapper;
    }

    @PostConstruct
    public void onStartup() {
        cronJobSch();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cronJobSch() {
        PeriodDateVO Now_Schedule = configurationMapper.selectAppliedSchedule();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        if (Now_Schedule.getPeriod() == 1) { // 매달
            start.set(Calendar.WEEK_OF_MONTH, Now_Schedule.getFromWeek());
            start.set(Calendar.DAY_OF_WEEK, Now_Schedule.getFromDay() + 1);
            end.set(Calendar.WEEK_OF_MONTH, Now_Schedule.getToWeek());
            end.set(Calendar.DAY_OF_WEEK, Now_Schedule.getToDay() + 1);
            log.info(df.format(start.getTime()));
            log.info(Now_Schedule.getFromDay());
            log.info(df.format(end.getTime()));
            log.info(Now_Schedule.getToDay());
            if (resultMapper.selectByScheduleIsExist((df.format(start.getTime())),
                    df.format(end.getTime())) == 0) {
                List<ClientVO> temp = clientMapper.selectClientAll();
                for (ClientVO client : temp) {
                    client.setCheckTime(df.format(start.getTime()));
                    resultMapper.insertEmptyResultBySchedule(client);
                }
            }
        } else if (Now_Schedule.getPeriod() == 2) { // 매주
            start.set(Calendar.DAY_OF_WEEK, Now_Schedule.getFromDay() + 1);
            end.set(Calendar.DAY_OF_WEEK, Now_Schedule.getToDay() + 1);
            if (resultMapper.selectByScheduleIsExist(df.format(start.getTime()),
                    df.format(end.getTime())) == 0) {
                List<ClientVO> temp = clientMapper.selectClientAll();
                for (ClientVO client : temp) {
                    client.setCheckTime(df.format(start.getTime()));
                    resultMapper.insertEmptyResultBySchedule(client);
                }
            }
        } else { // 매일
            if (resultMapper.selectByScheduleIsExist(df.format(start.getTime()),
                    df.format(end.getTime())) == 0) {
                List<ClientVO> temp = clientMapper.selectClientAll();
                for (ClientVO client : temp) {
                    client.setCheckTime(df.format(start.getTime()));
                    resultMapper.insertEmptyResultBySchedule(client);
                }
            }
        }
    }
}