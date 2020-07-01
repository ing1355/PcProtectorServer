package oms.pc_protector.restApi.dashboard.mapper;

import oms.pc_protector.restApi.dashboard.model.DashboardPeriodVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardMapper {

    public Integer selectAvgScoreByMonth();

    public Integer selectClientCount();

    public Integer selectAvgScoreByRecentMonths(@Param(value = "month") String month);

    public Integer selectUserCountByScore(@Param("startScore") int startScore,
                                          @Param("endScore") int endScore);

    public DashboardPeriodVO selectDashboardPeriod();

    public void dashboardPeriodUpdate(DashboardPeriodVO date);

}
