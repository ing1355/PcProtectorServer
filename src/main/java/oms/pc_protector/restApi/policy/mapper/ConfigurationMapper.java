package oms.pc_protector.restApi.policy.mapper;

import oms.pc_protector.restApi.policy.model.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigurationMapper {

    public ConfigurationVO selectConfiguration(String departmentIdx);

    public SecurityUsbDetailsVO selectSecurityUsbDetails(String departmentIdx);

    public List<PeriodDateVO> selectScheduleAll(String departmentIdx);

    public PeriodDateVO selectAppliedSchedule(String departmentIdx);

    public int countSecurityUsbDetails(String departmentIdx);

    public boolean selectAppliedFlag(String departmentIdx);

    public boolean selectForceRun(String departmentIdx);

    public EditProgramVO selectEditProgramFlag(String departmentIdx);

    public NowScheduleVO selectNextSchedule(String departmentIdx);

    public int insertConfiguration(ConfigurationVO configurationVO);

    public int updateConfiguration(ConfigurationVO configurationVO);

    public int insertSecurityUsbDetails(SecurityUsbDetailsVO securityUsbDetailsVO);

    public int updateSecurityUsbDetails(SecurityUsbDetailsVO securityUsbDetailsVO);

    public int updateSchedule(PeriodDateVO periodDateVO_new);

    public boolean insertEditProgramFlag(EditProgramVO editProgramVO);

    public boolean updateEditProgramFlag(EditProgramVO editProgramVO);

    public void updateNextSchedule(NowScheduleVO nowScheduleVO);

    public void updateForceRun(boolean param, String departmentIdx);
}
