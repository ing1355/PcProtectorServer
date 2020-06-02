package oms.pc_protector.restApi.dashboard.mapper;

import oms.pc_protector.restApi.dashboard.model.ChartVO;
import oms.pc_protector.restApi.dashboard.model.DashboardPeriodVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DashboardMapper {

    public Integer selectAvgScoreByMonth(@Param(value = "startDate") String startDate,
                                         @Param(value = "endDate") String endDate);

    public Integer selectAvgScoreByRecentMonths(@Param(value = "month") String month);

    public Integer selectUserCountByScore(@Param("startScore") int startScore,
                                          @Param("endScore") int endScore,
                                          @Param("startDate") String startDate,
                                          @Param("endDate") String endDate
    );

    public DashboardPeriodVO selectDashboardPeriod();

    public void dashboardPeriodUpdate(DashboardPeriodVO date);

}
