package oms.pc_protector.restApi.policy.service;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.policy.mapper.ConfigurationMapper;
import oms.pc_protector.restApi.policy.model.*;
import oms.pc_protector.restApi.process.model.ProcessVO;
import oms.pc_protector.restApi.process.service.ProcessService;
import oms.pc_protector.restApi.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Security;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;

@Log4j2
@Service
public class ConfigurationService {

    private final ConfigurationMapper configurationMapper;
    private final ProcessService processService;
    private final UserService userService;

    public ConfigurationService(UserService userService,
                                ConfigurationMapper configurationMapper,
                                ProcessService processService) {
        this.configurationMapper = configurationMapper;
        this.processService = processService;
        this.userService = userService;
    }


    @Transactional
    public LinkedHashMap findConfiguration() {
        LinkedHashMap<String, Object> configMap = new LinkedHashMap<>();
        ConfigurationVO configurationVO = configurationMapper.selectConfiguration();
        SecurityUsbDetailsVO securityUsbMap = findSecurityUsbDetails();
        EditProgramVO editProgramVO = findEditProgramFlag();
        if(configurationVO == null) {
            ConfigurationVO empty_config = new ConfigurationVO();
            configurationMapper.insertConfiguration(empty_config);
        }
        else {
            configMap.put("config", configurationVO);
        }
        if(editProgramVO == null) {
            EditProgramVO empty_edit = new EditProgramVO();
            configurationMapper.insertEditProgramFlag(empty_edit);
        }
        else {
            configMap.put("editProgram_set", editProgramVO);
        }
        if(securityUsbMap == null) {
            SecurityUsbDetailsVO empty_security = new SecurityUsbDetailsVO();
            configurationMapper.insertSecurityUsbDetails(empty_security);
        }
        else {
            configMap.put("securityUsb_input", securityUsbMap);
        }
        configMap.put("force_run",configurationMapper.selectForceRun());
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
    public PeriodDateVO findAppliedSchedule() {
        return Optional.ofNullable(configurationMapper.selectAppliedSchedule())
                .orElseGet(PeriodDateVO::new);
    }


    @Transactional
    public int registerSchedule(PeriodDateVO periodDateVO) {
        return configurationMapper.insertSchedule(periodDateVO);
    }



    @Transactional
    public int updateSchedule(RequestPeriodDateVO requestPeriodDateVO) {
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
