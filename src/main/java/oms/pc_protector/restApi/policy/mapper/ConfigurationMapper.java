package oms.pc_protector.restApi.policy.mapper;

import oms.pc_protector.restApi.policy.model.*;
import org.apache.ibatis.annotations.Param;
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

    public int insertConfiguration(ConfigurationVO configurationVO);

    public int updateConfiguration(ConfigurationVO configurationVO);

    public int insertSecurityUsbDetails(SecurityUsbDetailsVO securityUsbDetailsVO);

    public int updateSecurityUsbDetails(SecurityUsbDetailsVO securityUsbDetailsVO);

    public int insertSchedule(PeriodDateVO periodDateVO);

    public int updateSchedule(@Param("old_data") PeriodDateVO periodDateVO_old,
                              @Param("new_data") PeriodDateVO periodDateVO_new);
    public int deleteSchedule(PeriodDateVO periodDateVO);

    public boolean insertEditProgramFlag(EditProgramVO editProgramVO);

    public boolean updateEditProgramFlag(EditProgramVO editProgramVO);

    public void updateForceRun(boolean param);
}
