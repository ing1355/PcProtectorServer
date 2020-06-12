package oms.pc_protector.restApi.policy.mapper;

import oms.pc_protector.restApi.policy.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ConfigurationMapper {

    public ConfigurationVO selectConfiguration();

    public SecurityUsbDetailsVO selectSecurityUsbDetails();

    public List<PeriodDateVO> selectScheduleAll();

    public PeriodDateVO selectAppliedSchedule();

    public boolean selectAppliedFlag();

    public boolean selectForceRun();

    public EditProgramVO selectEditProgramFlag();

    public NowScheduleVO selectNextSchedule();

    public int selectNextScheduleCount();

    public int insertConfiguration(ConfigurationVO configurationVO);

    public int updateConfiguration(ConfigurationVO configurationVO);

    public int insertSecurityUsbDetails(SecurityUsbDetailsVO securityUsbDetailsVO);

    public int updateSecurityUsbDetails(SecurityUsbDetailsVO securityUsbDetailsVO);

    public int insertSchedule(PeriodDateVO periodDateVO);

    public int insertNextSchedule(NowScheduleVO nowScheduleVO);

    public int updateSchedule(@Param("new_data") PeriodDateVO periodDateVO_new);

    public int updateApply(Long old_idx, Long new_idx);

    public int deleteSchedule(PeriodDateVO periodDateVO);

    public boolean insertEditProgramFlag(EditProgramVO editProgramVO);

    public boolean updateEditProgramFlag(EditProgramVO editProgramVO);

    public void updateNextSchedule(NowScheduleVO nowScheduleVO);

    public void updateForceRun(boolean param);
}
