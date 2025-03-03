package oms.pc_protector.Scheduler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.restApi.client.mapper.ClientMapper;
import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.dashboard.mapper.DashboardMapper;
import oms.pc_protector.restApi.dashboard.model.DashboardPeriodVO;
import oms.pc_protector.restApi.manager.mapper.ManagerMapper;
import oms.pc_protector.restApi.policy.mapper.ConfigurationMapper;
import oms.pc_protector.restApi.policy.model.NowScheduleVO;
import oms.pc_protector.restApi.policy.model.PeriodDateVO;
import oms.pc_protector.restApi.result.mapper.ResultMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class SchedulerService {
    private final ConfigurationMapper configurationMapper;
    private final ResultMapper resultMapper;
    private final ClientMapper clientMapper;
    private final ManagerMapper managerMapper;
    private final DashboardMapper dashboardMapper;

    public SchedulerService(ConfigurationMapper configurationMapper,
                            ResultMapper resultMapper,
                            ClientMapper clientMapper,
                            ManagerMapper managerMapper,
                            DashboardMapper dashboardMapper) {
        this.clientMapper = clientMapper;
        this.resultMapper = resultMapper;
        this.configurationMapper = configurationMapper;
        this.managerMapper = managerMapper;
        this.dashboardMapper = dashboardMapper;
    }

    @PostConstruct
    public void onStartup() {
        managerMapper.updateRoot();
        cronJobSch();
    } // 최초 서버 구동시 1회 실행

    @SneakyThrows
    @Scheduled(cron = "0 0 8 * * *") // 매일 8시
    public void cronJobSch() {
        List<DashboardPeriodVO> periodVOList = dashboardMapper.selectAllDashboardPeriod();

        for (DashboardPeriodVO periodVO : periodVOList) {

            PeriodDateVO Now_Schedule = configurationMapper.selectAppliedSchedule(periodVO.getDepartmentIdx());
            SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar start = Calendar.getInstance();
            Calendar next_start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            Calendar next_end = Calendar.getInstance();
            Calendar now = Calendar.getInstance();
            List<ClientVO> temp = clientMapper.selectClientAll(periodVO.getDepartmentIdx());

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
                next_start.add(Calendar.MONTH, 1);
                next_end.add(Calendar.MONTH, 1);
                int next_month = next_start.get(Calendar.MONTH);
                next_start.set(Calendar.WEEK_OF_MONTH, Now_Schedule.getFromWeek());
                next_start.set(Calendar.DAY_OF_WEEK, Now_Schedule.getFromDay() + 1);
                next_end.set(Calendar.WEEK_OF_MONTH, Now_Schedule.getToWeek());
                next_end.set(Calendar.DAY_OF_WEEK, Now_Schedule.getToDay() + 1);
                if (Now_Schedule.getFromWeek() == 1) {
                    if (next_start.get(Calendar.MONTH) != next_end.get(Calendar.MONTH)) {
                        log.info("Schedule case 1");
                        next_start.add(Calendar.MONTH, 1);
                        next_start.set(Calendar.DAY_OF_MONTH, next_end.getMinimum(Calendar.DAY_OF_MONTH));
                    } else if (next_start.get(Calendar.MONTH) == start.get(Calendar.MONTH) && next_end.get(Calendar.MONTH) == end.get(Calendar.MONTH)) {
                        log.info("Schedule case 2");
                        next_start.add(Calendar.DATE, 7);
                        next_end.add(Calendar.DATE, 7);
                    } else if (next_start.get(Calendar.DAY_OF_MONTH) > 15) {
                        log.info("Schedule case 3");
                        next_start.add(Calendar.DATE, 7);
                        next_end.add(Calendar.DATE, 7);
                    }
                } else if (Now_Schedule.getFromWeek() == 5) {
                    if (next_start.get(Calendar.MONTH) != next_end.get(Calendar.MONTH)) {
                        log.info("Schedule case 4");
                        next_end.set(Calendar.MONTH, next_start.get(Calendar.MONTH));
                        next_end.set(Calendar.DAY_OF_MONTH, next_start.getActualMaximum(Calendar.DAY_OF_MONTH));
                    } else if (next_start.get(Calendar.MONTH) != next_month && next_end.get(Calendar.MONTH) != next_month) {
                        log.info("Schedule case 5");
                        next_start.add(Calendar.DATE, -7);
                        next_end.add(Calendar.DATE, -7);
                    }

                }
            } else if (Now_Schedule.getPeriod() == 2) { // 매주
                start.set(Calendar.DAY_OF_WEEK, now.getMinimum((Calendar.DAY_OF_WEEK)));
                start.set(Calendar.HOUR_OF_DAY, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
                end.set(Calendar.HOUR_OF_DAY, 23);
                end.set(Calendar.MINUTE, 59);
                end.set(Calendar.SECOND, 59);

                start.set(Calendar.DAY_OF_WEEK, Now_Schedule.getFromDay() + 1);
                start.set(Calendar.HOUR_OF_DAY, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
                end.set(Calendar.DAY_OF_WEEK, Now_Schedule.getToDay() + 1);
                end.set(Calendar.HOUR_OF_DAY, 23);
                end.set(Calendar.MINUTE, 59);
                end.set(Calendar.SECOND, 59);

                next_start.set(Calendar.DAY_OF_WEEK, Now_Schedule.getFromDay() + 1);
                next_end.set(Calendar.DAY_OF_WEEK, Now_Schedule.getToDay() + 1);
                next_start.add(Calendar.DATE, 7);
                next_end.add(Calendar.DATE, 7);
            } else { // 매일
                start.set(Calendar.HOUR_OF_DAY, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
                end.set(Calendar.HOUR_OF_DAY, 23);
                end.set(Calendar.MINUTE, 59);
                end.set(Calendar.SECOND, 59);
                next_start.add(Calendar.DATE, 1);
                next_end.add(Calendar.DATE, 1);
            }

            Calendar dash_start = Calendar.getInstance();
            Date dash_1 = dft.parse(periodVO.getStartDate());
            Calendar dash_end = Calendar.getInstance();
            Date dash_2 = dft.parse(periodVO.getEndDate());

            dash_start.setTime(dash_1);
            dash_end.setTime(dash_2);

            if ((df.format(dash_end.getTime()).compareTo(df.format(now.getTime())) < 0 &&
                    start.getTime().compareTo(now.getTime()) <= 0 && end.getTime().compareTo(now.getTime()) >= 0) &&
                    df.format(now.getTime()).equals(df.format(start.getTime()))) {
                dashboardMapper.dashboardPeriodUpdate(new DashboardPeriodVO(dft.format(start.getTime()), dft.format(end.getTime()), periodVO.getDepartmentIdx()));
            }

            periodVO = dashboardMapper.selectDashboardPeriod(periodVO.getDepartmentIdx());
            dash_1 = dft.parse(periodVO.getStartDate());
            dash_2 = dft.parse(periodVO.getEndDate());
            dash_start.setTime(dash_1);
            dash_end.setTime(dash_2);

            if ((start.getTime().compareTo(dash_start.getTime()) >= 0 && start.getTime().compareTo(dash_end.getTime()) <= 0) ||
                    (end.getTime().compareTo(dash_start.getTime()) >= 0 && end.getTime().compareTo(dash_end.getTime()) <= 0) ||
                    now.getTime().compareTo(start.getTime()) > 0) {
                configurationMapper.updateNextSchedule(new NowScheduleVO(df.format(next_start.getTime()), df.format(next_end.getTime()), periodVO.getDepartmentIdx()));
            } else {
                configurationMapper.updateNextSchedule(new NowScheduleVO(df.format(start.getTime()), df.format(end.getTime()), periodVO.getDepartmentIdx()));
            }

            log.info("start : " + dft.format(start.getTime()));
            log.info("next_start : " + df.format(next_start.getTime()));
            log.info("end : " + dft.format(end.getTime()));
            log.info("next_end : " + df.format(next_end.getTime()));
            log.info("now : " + dft.format(now.getTime()));
            log.info("dash_start : " + df.format(dash_start.getTime()));
            log.info("dash_end : " + df.format(dash_end.getTime()));



            if (periodVO.getStartDate().compareTo(dft.format(now.getTime())) <= 0 &&
                    periodVO.getEndDate().compareTo(dft.format(now.getTime())) >= 0) {
                for (ClientVO client : temp) { // 각 클라이언트의 빈 데이터 셋 존재하는지 체크하여 없으면 생성
                    if (resultMapper.selectByScheduleIsExist(client.getUserId(), client.getIpAddress(), periodVO.getDepartmentIdx()) == 0) {
                        client.setCheckTime(periodVO.getStartDate());
                        resultMapper.insertEmptyResultBySchedule(client);
                    }
                }
            }
        }

    }
}