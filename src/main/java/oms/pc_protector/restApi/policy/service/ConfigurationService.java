package oms.pc_protector.restApi.policy.service;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.client.mapper.ClientMapper;
import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.dashboard.mapper.DashboardMapper;
import oms.pc_protector.restApi.dashboard.model.DashboardPeriodVO;
import oms.pc_protector.restApi.policy.mapper.ConfigurationMapper;
import oms.pc_protector.restApi.policy.model.*;
import oms.pc_protector.restApi.process.service.ProcessService;
import oms.pc_protector.restApi.result.mapper.ResultMapper;
import oms.pc_protector.restApi.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j2
@Service
public class ConfigurationService {

    private final ConfigurationMapper configurationMapper;
    private final ProcessService processService;
    private final UserService userService;
    private final ResultMapper resultMapper;
    private final DashboardMapper dashboardMapper;
    private final ClientMapper clientMapper;

    public ConfigurationService(UserService userService,
                                ConfigurationMapper configurationMapper,
                                ProcessService processService,
                                ResultMapper resultMapper,
                                DashboardMapper dashboardMapper,
                                ClientMapper clientMapper) {
        this.configurationMapper = configurationMapper;
        this.processService = processService;
        this.userService = userService;
        this.resultMapper = resultMapper;
        this.dashboardMapper = dashboardMapper;
        this.clientMapper = clientMapper;
    }


    @Transactional
    public LinkedHashMap findConfiguration() {
        LinkedHashMap<String, Object> configMap = new LinkedHashMap<>();
        ConfigurationVO configurationVO = configurationMapper.selectConfiguration();
        SecurityUsbDetailsVO securityUsbMap = findSecurityUsbDetails();
        EditProgramVO editProgramVO = findEditProgramFlag();
        if (configurationVO == null) {
            ConfigurationVO empty_config = new ConfigurationVO();
            configurationMapper.insertConfiguration(empty_config);
        } else {
            configMap.put("config", configurationVO);
        }
        if (editProgramVO == null) {
            EditProgramVO empty_edit = new EditProgramVO();
            configurationMapper.insertEditProgramFlag(empty_edit);
        } else {
            configMap.put("editProgram_set", editProgramVO);
        }
        if (securityUsbMap == null) {
            SecurityUsbDetailsVO empty_security = new SecurityUsbDetailsVO();
            configurationMapper.insertSecurityUsbDetails(empty_security);
        } else {
            configMap.put("securityUsb_input", securityUsbMap);
        }
        configMap.put("force_run", configurationMapper.selectForceRun());
        return configMap;
    }

    @Transactional
    public void updateConfiguration_service(ConfigurationVO configurationVO) {
        configurationMapper.updateConfiguration(configurationVO);
    }

    @Transactional
    public void updateSecurityUsbDetails_service(SecurityUsbDetailsVO securityUsbDetailsVO) {
        configurationMapper.updateSecurityUsbDetails(securityUsbDetailsVO);
    }

    @Transactional
    public void updateEditProgramFlag_service(EditProgramVO editProgramVO) {
        configurationMapper.updateEditProgramFlag(editProgramVO);
    }

    @Transactional
    public void updateForceRun(boolean param) {
        configurationMapper.updateForceRun(param);
    }

    @Transactional
    public List<PeriodDateVO> findScheduleAll() {
        return Optional.ofNullable(configurationMapper.selectScheduleAll())
                .orElseGet(ArrayList::new);
    }

    @Transactional
    public NowScheduleVO findNextSchedule() {
        return configurationMapper.selectNextSchedule();
    }


    @Transactional
    public PeriodDateVO findAppliedSchedule() {
        return Optional.ofNullable(configurationMapper.selectAppliedSchedule())
                .orElseGet(PeriodDateVO::new);
    }


    @Transactional
    public int registerSchedule(PeriodDateVO periodDateVO) {
        return configurationMapper.insertSchedule(periodDateVO);
    }


    @Transactional
    public int updateSchedule(RequestPeriodDateVO requestPeriodDateVO) throws ParseException {
//        SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM");
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Calendar start = Calendar.getInstance();
//        Calendar next_start = Calendar.getInstance();
//        Calendar end = Calendar.getInstance();
//        Calendar next_end = Calendar.getInstance();
//        Calendar now = Calendar.getInstance();

//        if (requestPeriodDateVO.getNew_data().getPeriod() == 1) { // 매달
//            start.set(Calendar.WEEK_OF_MONTH, requestPeriodDateVO.getNew_data().getFromWeek());
//            start.set(Calendar.DAY_OF_WEEK, requestPeriodDateVO.getNew_data().getFromDay() + 1);
//            start.set(Calendar.HOUR_OF_DAY, 0);
//            start.set(Calendar.MINUTE, 0);
//            start.set(Calendar.SECOND, 0);
//            end.set(Calendar.WEEK_OF_MONTH, requestPeriodDateVO.getNew_data().getToWeek());
//            end.set(Calendar.DAY_OF_WEEK, requestPeriodDateVO.getNew_data().getToDay() + 1);
//            end.set(Calendar.HOUR_OF_DAY, 23);
//            end.set(Calendar.MINUTE, 59);
//            end.set(Calendar.SECOND, 59);
//            if (requestPeriodDateVO.getNew_data().getFromWeek() == 1) {
//                if (start.get(Calendar.MONTH) != end.get(Calendar.MONTH)) {
//                    start.add(Calendar.MONTH, 1);
//                    start.set(Calendar.DAY_OF_MONTH, 1);
//                } else if (start.get(Calendar.MONTH) == now.get(Calendar.MONTH) - 1 && end.get(Calendar.MONTH) == now.get(Calendar.MONTH) - 1) {
//                    start.add(Calendar.DATE, 7);
//                    end.add(Calendar.DATE, 7);
//                }
//            } else if (requestPeriodDateVO.getNew_data().getFromWeek() == 5) {
//                if (start.get(Calendar.MONTH) != end.get(Calendar.MONTH)) {
//                    end.set(Calendar.MONTH, start.get(Calendar.MONTH));
//                    end.set(Calendar.DAY_OF_WEEK, start.getActualMaximum(Calendar.DAY_OF_MONTH));
//                } else if (start.get(Calendar.MONTH) != now.get(Calendar.MONTH) && end.get(Calendar.MONTH) != now.get(Calendar.MONTH)) {
//                    start.add(Calendar.DATE, -7);
//                    end.add(Calendar.DATE, -7);
//                }
//
//            } else {
//                next_start.set(Calendar.WEEK_OF_MONTH, requestPeriodDateVO.getNew_data().getFromWeek());
//                next_start.set(Calendar.DAY_OF_WEEK, requestPeriodDateVO.getNew_data().getFromDay() + 1);
//                next_end.set(Calendar.WEEK_OF_MONTH, requestPeriodDateVO.getNew_data().getToWeek());
//                next_end.set(Calendar.DAY_OF_WEEK, requestPeriodDateVO.getNew_data().getToDay() + 1);
//            }
//            int count = resultMapper.selectCountByNowScheduleMonth(dfm.format(start.getTime()));
//            if (count > 0) {
//                next_start.add(Calendar.MONTH, 1);
//                next_end.add(Calendar.MONTH, 1);
//                int next_month = next_start.get(Calendar.MONTH);
//                next_start.set(Calendar.WEEK_OF_MONTH, requestPeriodDateVO.getNew_data().getFromWeek());
//                next_start.set(Calendar.DAY_OF_WEEK, requestPeriodDateVO.getNew_data().getFromDay() + 1);
//                next_end.set(Calendar.WEEK_OF_MONTH, requestPeriodDateVO.getNew_data().getToWeek());
//                next_end.set(Calendar.DAY_OF_WEEK, requestPeriodDateVO.getNew_data().getToDay() + 1);
//                if (requestPeriodDateVO.getNew_data().getFromWeek() == 1) {
//                    if (next_start.get(Calendar.MONTH) != next_end.get(Calendar.MONTH)) {
//                        next_start.add(Calendar.MONTH, 1);
//                        next_start.set(Calendar.DAY_OF_MONTH, next_end.getMinimum(Calendar.DAY_OF_MONTH));
//                    } else if (next_start.get(Calendar.MONTH) == start.get(Calendar.MONTH) && next_end.get(Calendar.MONTH) == end.get(Calendar.MONTH)) {
//                        next_start.add(Calendar.DATE, 7);
//                        next_end.add(Calendar.DATE, 7);
//                    }
//                } else if (requestPeriodDateVO.getNew_data().getFromWeek() == 5) {
//                    if (next_start.get(Calendar.MONTH) != next_end.get(Calendar.MONTH)) {
//                        next_end.set(Calendar.MONTH, next_start.get(Calendar.MONTH));
//                        next_end.set(Calendar.DAY_OF_MONTH, next_start.getActualMaximum(Calendar.DAY_OF_MONTH));
//                    } else if (next_start.get(Calendar.MONTH) != next_month && next_end.get(Calendar.MONTH) != next_month) {
//                        next_start.add(Calendar.DATE, -7);
//                        next_end.add(Calendar.DATE, -7);
//                    }
//
//                }
//            }
//            else {
//                next_start.set(Calendar.WEEK_OF_MONTH, start.get(Calendar.WEEK_OF_MONTH));
//                next_start.set(Calendar.DAY_OF_WEEK, start.get(Calendar.DAY_OF_WEEK));
//                next_end.set(Calendar.WEEK_OF_MONTH, end.get(Calendar.WEEK_OF_MONTH));
//                next_end.set(Calendar.DAY_OF_WEEK, end.get(Calendar.DAY_OF_WEEK));
//                if(dft.format(start.getTime()).compareTo(dft.format(now.getTime())) <= 0 &&
//                    dft.format(end.getTime()).compareTo(dft.format(now.getTime())) >= 0) {
//                    dashboardMapper.dashboardPeriodUpdate(new DashboardPeriodVO(dft.format(start.getTime()),dft.format(end.getTime())));
//                    List<ClientVO> temp = clientMapper.selectClientAll();
//                    for (ClientVO client : temp) { // 각 클라이언트의 빈 데이터 셋 존재하는지 체크하여 없으면 생성
//                        if (resultMapper.selectByScheduleIsExist((dft.format(start.getTime())),
//                                dft.format(end.getTime()), client.getUserId(), client.getIpAddress()) == 0) {
//                            client.setCheckTime(dft.format(start.getTime()));
//                            resultMapper.insertEmptyResultBySchedule(client);
//                        }
//                    }
//                }
//            }
//
//        } else if (requestPeriodDateVO.getNew_data().getPeriod() == 2) { // 매주
//            start.set(Calendar.DAY_OF_WEEK, now.getMinimum((Calendar.DAY_OF_WEEK)));
//            start.set(Calendar.HOUR_OF_DAY, 0);
//            start.set(Calendar.MINUTE, 0);
//            start.set(Calendar.SECOND, 0);
//            end.set(Calendar.HOUR_OF_DAY, 23);
//            end.set(Calendar.MINUTE, 59);
//            end.set(Calendar.SECOND, 59);
//
//            int count = resultMapper.selectCountBySchedule(new NowScheduleVO(dft.format(start.getTime()), dft.format(end.getTime())));
//            // 같은 주기에 스케줄 결과가 있는지 체크
//
//            start.set(Calendar.DAY_OF_WEEK, requestPeriodDateVO.getNew_data().getFromDay() + 1);
//            start.set(Calendar.HOUR_OF_DAY, 0);
//            start.set(Calendar.MINUTE, 0);
//            start.set(Calendar.SECOND, 0);
//            end.set(Calendar.DAY_OF_WEEK, requestPeriodDateVO.getNew_data().getToDay() + 1);
//            end.set(Calendar.HOUR_OF_DAY, 23);
//            end.set(Calendar.MINUTE, 59);
//            end.set(Calendar.SECOND, 59);
//
//            next_start.set(Calendar.DAY_OF_WEEK, requestPeriodDateVO.getNew_data().getFromDay() + 1);
//            next_end.set(Calendar.DAY_OF_WEEK, requestPeriodDateVO.getNew_data().getToDay() + 1);
//            if (count > 0) { // 같은 주기에 스케줄 결과가 있는지 체크
//                next_start.add(Calendar.DATE, 7);
//                next_end.add(Calendar.DATE, 7);
//            }
//            if(dft.format(start.getTime()).compareTo(dft.format(now.getTime())) <= 0 &&
//                    dft.format(end.getTime()).compareTo(dft.format(now.getTime())) >= 0) {
//                dashboardMapper.dashboardPeriodUpdate(new DashboardPeriodVO(dft.format(start.getTime()),dft.format(end.getTime())));
//                List<ClientVO> temp = clientMapper.selectClientAll();
//                for (ClientVO client : temp) { // 각 클라이언트의 빈 데이터 셋 존재하는지 체크하여 없으면 생성
//                    if (resultMapper.selectByScheduleIsExist((dft.format(start.getTime())),
//                            dft.format(end.getTime()), client.getUserId(), client.getIpAddress()) == 0) {
//                        client.setCheckTime(dft.format(start.getTime()));
//                        resultMapper.insertEmptyResultBySchedule(client);
//                    }
//                }
//            }
//        } else { // 매일
//            start.set(Calendar.HOUR_OF_DAY, 0);
//            start.set(Calendar.MINUTE, 0);
//            start.set(Calendar.SECOND, 0);
//            end.set(Calendar.HOUR_OF_DAY, 23);
//            end.set(Calendar.MINUTE, 59);
//            end.set(Calendar.SECOND, 59);
//            next_start.add(Calendar.DATE, 1);
//            next_end.add(Calendar.DATE, 1);
//        }
//
//        log.info("start : " + dft.format(start.getTime()));
//        log.info("next_start : " + df.format(next_start.getTime()));
//        log.info("end : " + dft.format(end.getTime()));
//        log.info("next_end : " + df.format(next_end.getTime()));
//        log.info("now : " + dft.format(now.getTime()));
//
//        configurationMapper.updateNextSchedule(new NowScheduleVO(df.format(next_start.getTime()), df.format(next_end.getTime())));
        PeriodDateVO Now_Schedule = requestPeriodDateVO.getNew_data();
        DashboardPeriodVO dashboardPeriodVO = dashboardMapper.selectDashboardPeriod();
        SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar start = Calendar.getInstance();
        Calendar next_start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        Calendar next_end = Calendar.getInstance();
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
            next_start.add(Calendar.MONTH, 1);
            next_end.add(Calendar.MONTH, 1);
            int next_month = next_start.get(Calendar.MONTH);
            next_start.set(Calendar.WEEK_OF_MONTH, Now_Schedule.getFromWeek());
            next_start.set(Calendar.DAY_OF_WEEK, Now_Schedule.getFromDay() + 1);
            next_end.set(Calendar.WEEK_OF_MONTH, Now_Schedule.getToWeek());
            next_end.set(Calendar.DAY_OF_WEEK, Now_Schedule.getToDay() + 1);
            if (Now_Schedule.getFromWeek() == 1) {
                if (next_start.get(Calendar.MONTH) != next_end.get(Calendar.MONTH)) {
                    next_start.add(Calendar.MONTH, 1);
                    next_start.set(Calendar.DAY_OF_MONTH, next_end.getMinimum(Calendar.DAY_OF_MONTH));
                } else if (next_start.get(Calendar.MONTH) == start.get(Calendar.MONTH) && next_end.get(Calendar.MONTH) == end.get(Calendar.MONTH)) {
                    next_start.add(Calendar.DATE, 7);
                    next_end.add(Calendar.DATE, 7);
                }
            } else if (Now_Schedule.getFromWeek() == 5) {
                if (next_start.get(Calendar.MONTH) != next_end.get(Calendar.MONTH)) {
                    next_end.set(Calendar.MONTH, next_start.get(Calendar.MONTH));
                    next_end.set(Calendar.DAY_OF_MONTH, next_start.getActualMaximum(Calendar.DAY_OF_MONTH));
                } else if (next_start.get(Calendar.MONTH) != next_month && next_end.get(Calendar.MONTH) != next_month) {
                    next_start.add(Calendar.DATE, -7);
                    next_end.add(Calendar.DATE, -7);
                }

            }
        } else if (Now_Schedule.getPeriod() == 2) { // 매주
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
        Date dash_1 = dft.parse(dashboardPeriodVO.getStartDate());
        Calendar dash_end = Calendar.getInstance();
        Date dash_2 = dft.parse(dashboardPeriodVO.getEndDate());

        dash_start.setTime(dash_1);
        dash_end.setTime(dash_2);

        log.info("start : " + dft.format(start.getTime()));
        log.info("next_start : " + df.format(next_start.getTime()));
        log.info("end : " + dft.format(end.getTime()));
        log.info("next_end : " + df.format(next_end.getTime()));
        log.info("now : " + dft.format(now.getTime()));

        if ((start.getTime().compareTo(dash_start.getTime()) >= 0 && start.getTime().compareTo(dash_end.getTime()) <= 0) ||
                (end.getTime().compareTo(dash_start.getTime()) >= 0 && end.getTime().compareTo(dash_end.getTime()) <= 0) ||
                now.getTime().compareTo(start.getTime()) > 0) {
            configurationMapper.updateNextSchedule(new NowScheduleVO(df.format(next_start.getTime()), df.format(next_end.getTime())));
        } else {
            configurationMapper.updateNextSchedule(new NowScheduleVO(df.format(start.getTime()), df.format(end.getTime())));
        }
        return configurationMapper.updateSchedule(requestPeriodDateVO.getNew_data());
    }

    @Transactional
    public int updateApply(Long old_idx, Long new_idx) {
        return configurationMapper.updateApply(old_idx, new_idx);
    }

    @Transactional
    public int deleteSchedule(PeriodDateVO periodDateVO) {
        return configurationMapper.deleteSchedule(periodDateVO);
    }

    @Transactional
    public boolean hasAppliedFlag() {
        return configurationMapper.selectAppliedFlag();
    }


    @Transactional
    public SecurityUsbDetailsVO findSecurityUsbDetails() {
        return Optional.ofNullable(configurationMapper.selectSecurityUsbDetails())
                .orElseGet(SecurityUsbDetailsVO::new);
    }


    @Transactional
    public EditProgramVO findEditProgramFlag() {
        return configurationMapper.selectEditProgramFlag();
    }


    @Transactional
    public ForceRunVO findForceRun() {
        ForceRunVO forceRunVO = new ForceRunVO();
        forceRunVO.setForceRun(configurationMapper.selectForceRun());
        return forceRunVO;
    }


    // 클라이언트에게 보내는 정책 설정.
    // 차후 리팩토링 필요한 메소드.
    @Transactional
    public HashMap findConfigurationToClient() {
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> securityUsbMap = new HashMap<>();
        HashMap<String, Object> unApprovedProcessMap = new HashMap<>();
        HashMap<String, Object> requiredProcessMap = new HashMap<>();
        HashMap<String, Object> periodDateMap = new HashMap<>();
        HashMap<String, Object> editProgram = new HashMap<>();

        ConfigurationVO configurationVO = configurationMapper.selectConfiguration();
        EditProgramVO editProgramVO = findEditProgramFlag();

        boolean vaccineInstallationExecution = configurationVO.isVaccineInstallationExecution();
        boolean vaccinePatch = configurationVO.isVaccinePatch();
        boolean osWithMsOfficePatch = configurationVO.isOsWithMsOfficePatch();
        boolean hwpPatch = configurationVO.isHwpPatch();
        boolean passwordStability = configurationVO.isPasswordStability();
        boolean passwordChange = configurationVO.isPasswordChange();
        boolean screenSaver = configurationVO.isScreenSaver();
        boolean sharedFolder = configurationVO.isSharedFolder();
        boolean usbAutoRun = configurationVO.isUsbAutoRun();

        boolean overDateActiveX = configurationVO.isOverDateActiveX();
        boolean acrobatSecurityPatch = configurationVO.isAcrobatSecurityPatch();
        boolean editProgramFlag = configurationVO.isEditProgram();
        boolean wirelessLanCard = configurationVO.isWirelessLanCard();
        boolean securityUsb = configurationVO.isSecurityUsb();
        boolean unApprovedProcess = configurationVO.isUnApprovedProcess();
        boolean requiredProcess = configurationVO.isRequiredProcess();

        securityUsbMap.put("securityUsbCheck", securityUsb);
        securityUsbMap.put("securityUsbArray", findSecurityUsbDetails());

        unApprovedProcessMap.put("unapprovedProcessCheck", unApprovedProcess);
        unApprovedProcessMap.put("unapprovedProcessArray", processService.findUnApprovedProcessList());

        requiredProcessMap.put("requiredProcessCheck", requiredProcess);
        requiredProcessMap.put("requiredProcessArray", processService.findRequiredProcessList());

        periodDateMap.put("periodDateCheck", hasAppliedFlag());
        periodDateMap.put("periodDateArray", findAppliedSchedule());
        periodDateMap.put("nextPeriodDateArray", findNextSchedule());

        boolean forceRun = (boolean) Optional.ofNullable(findConfiguration()
                .get("forceRun")).orElse(false);

        editProgram.put("editProgramInstalledCheck", editProgramFlag);
        editProgram.put("msWord", editProgramVO.isMsWord());
        editProgram.put("msPowerPoint", editProgramVO.isMsPowerPoint());
        editProgram.put("msExcel", editProgramVO.isMsExcel());
        editProgram.put("hwp", editProgramVO.isHwp());
        editProgram.put("pdf", editProgramVO.isPdf());

        map.put("vaccineInstallationExecutionCheck", vaccineInstallationExecution);
        map.put("vaccinePatchCheck", vaccinePatch);
        map.put("osWithMsOfficePatchCheck", osWithMsOfficePatch);
        map.put("hwpPatchCheck", hwpPatch);
        map.put("passwordStabilityCheck", passwordStability);
        map.put("passwordChangeCheck", passwordChange);
        map.put("screenSaverCheck", screenSaver);
        map.put("sharedFolderCheck", sharedFolder);
        map.put("usbAutoRunCheck", usbAutoRun);
        map.put("overDateActivexCheck", overDateActiveX);
        map.put("acrobatVersionCheck", acrobatSecurityPatch);
        map.put("editProgram", editProgram);
        map.put("wirelessLanCardCheck", wirelessLanCard);
        map.put("securityUsb", securityUsbMap);
        map.put("unapprovedProcess", unApprovedProcessMap);
        map.put("requiredProcess", requiredProcessMap);
        map.put("periodDate", periodDateMap);
        return map;
    }
}
