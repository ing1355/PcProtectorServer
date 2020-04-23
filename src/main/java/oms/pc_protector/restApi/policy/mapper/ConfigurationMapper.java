package oms.pc_protector.restApi.policy.mapper;

import oms.pc_protector.restApi.policy.model.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ConfigurationMapper {

    public ConfigurationVO selectConfiguration();

    public List<SecurityUsbDetailsVO> selectSecurityUsbDetails();

    public List<PeriodDateVO> selectScheduleAll();

    public HashMap selectAppliedSchedule();

    public boolean selectAppliedFlag();

    public boolean selectForceRun();

    public EditProgramVO selectEditProgramFlag();

    public int insertConfiguration(RequestConfigurationVO requestConfigurationVO);

    public int insertSecurityUsbDetails(SecurityUsbDetailsVO securityUsbDetailsVO);

    public int insertSchedule(PeriodDateVO periodDateVO);

    public boolean updateEditProgramFlag(EditProgramVO editProgramVO);
}
