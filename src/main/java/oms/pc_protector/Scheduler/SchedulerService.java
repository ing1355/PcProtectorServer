package oms.pc_protector.Scheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.client.mapper.ClientMapper;
import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.dashboard.mapper.DashboardMapper;
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
    private final DashboardMapper dashboardMapper;

    public SchedulerService(ConfigurationMapper configurationMapper,
                            ResultMapper resultMapper,
                            ClientMapper clientMapper,
                            DashboardService dashboardService,
                            DashboardMapper dashboardMapper) {
        this.clientMapper = clientMapper;
        this.resultMapper = resultMapper;
        this.configurationMapper = configurationMapper;
        this.dashboardService = dashboardService;
        this.dashboardMapper = dashboardMapper;
    }

    @PostConstruct
    public void onStartup() {
        cronJobSch();
    } // 최초 서버 구동시 1회 실행

    @SneakyThrows
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정
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
        } else if (Now_Schedule.getPeriod() == 2) { // 매주

            start.set(Calendar.DAY_OF_WEEK, Now_Schedule.getFromDay() + 1);
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            end.set(Calendar.DAY_OF_WEEK, Now_Schedule.getToDay() + 1);
            end.set(Calendar.HOUR_OF_DAY, 23);
            end.set(Calendar.MINUTE, 59);
            end.set(Calendar.SECOND, 59);
        } else { // 매일
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            end.set(Calendar.HOUR_OF_DAY, 23);
            end.set(Calendar.MINUTE, 59);
            end.set(Calendar.SECOND, 59);
        }
//        log.info(df.format(start.getTime()));
//        log.info(df.format(now.getTime()));
//        log.info(Now_Schedule.getFromDay());
//        log.info(df.format(end.getTime()));
//        log.info(Now_Schedule.getToDay());
        log.info("start : " + dft.format(start.getTime()));
        log.info("end : " + dft.format(end.getTime()));
        log.info("now : " + dft.format(now.getTime()));
        if (df.format(start.getTime()).equals(df.format(now.getTime()))) { // 오늘이 현재 정책 점검 기간 시작 날인지 체크
            DashboardPeriodVO dashboardPeriodVO = dashboardMapper.selectDashboardPeriod();
            Date d1 = df.parse(dashboardPeriodVO.getEndDate());
            Calendar c1 = Calendar.getInstance();
            c1.setTime(d1);
            if (df.format(c1.getTime()).compareTo(df.format(now.getTime())) < 0) { // 현재 저장된 점검 기간이 지나야 새로운 점검 기간 업데이트
                dashboardService.dashboardPeriodUpdate(new DashboardPeriodVO(dft.format(start.getTime()), dft.format(end.getTime())));
            }
            for (ClientVO client : temp) { // 각 클라이언트의 빈 데이터 셋 존재하는지 체크하여 없으면 생성
                if (resultMapper.selectByScheduleIsExist((dft.format(start.getTime())),
                        dft.format(end.getTime()), client.getUserId(), client.getIpAddress()) == 0) {
                    client.setCheckTime(dft.format(start.getTime()));
                    resultMapper.insertEmptyResultBySchedule(client);
                }
            }
        }

    }
}