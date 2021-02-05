package oms.pc_protector.restApi.dashboard.mapper;

import oms.pc_protector.restApi.dashboard.model.DashboardPeriodVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DashboardMapper {

    public Integer selectAvgScoreByMonth(String departmentIdx);

    public Integer selectClientCount(String departmentIdx);

    public Integer selectAvgScoreByRecentMonths(@Param(value = "month") String month,
                                                @Param(value = "departmentIdx") String departmentIdx);

    public Integer selectUserCountByScore(@Param("startScore") int startScore,
                                          @Param("endScore") int endScore,
                                          @Param("departmentIdx") String departmentIdx);

    public List<DashboardPeriodVO> selectAllDashboardPeriod();

    public DashboardPeriodVO selectDashboardPeriod(String departmentIdx);

    public void dashboardPeriodUpdate(DashboardPeriodVO date);

}
