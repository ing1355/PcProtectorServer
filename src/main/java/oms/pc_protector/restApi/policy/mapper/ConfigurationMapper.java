package oms.pc_protector.restApi.policy.mapper;

import oms.pc_protector.restApi.policy.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigurationMapper {

    public ConfigurationVO selectConfiguration(String idx);

    public SecurityUsbDetailsVO selectSecurityUsbDetails(String idx);

    public List<PeriodDateVO> selectScheduleAll(String idx);

    public PeriodDateVO selectAppliedSchedule(String idx);

    public int countSecurityUsbDetails(String idx);

    public boolean selectAppliedFlag(String idx);

    public boolean selectForceRun(String idx);

    public EditProgramVO selectEditProgramFlag(String idx);

    public NowScheduleVO selectNextSchedule(String idx);

    public int insertConfiguration(ConfigurationVO configurationVO);

    public int updateConfiguration(ConfigurationVO configurationVO);

    public int insertSecurityUsbDetails(SecurityUsbDetailsVO securityUsbDetailsVO);

    public int updateSecurityUsbDetails(SecurityUsbDetailsVO securityUsbDetailsVO);

    public int insertSchedule(PeriodDateVO periodDateVO);

    public int updateSchedule(@Param("new_data") PeriodDateVO periodDateVO_new);

    public int updateApply(Long old_idx, Long new_idx);

    public int deleteSchedule(PeriodDateVO periodDateVO);

    public boolean insertEditProgramFlag(EditProgramVO editProgramVO);

    public boolean updateEditProgramFlag(EditProgramVO editProgramVO);

    public void updateNextSchedule(NowScheduleVO nowScheduleVO);

    public void updateForceRun(boolean param);
}
