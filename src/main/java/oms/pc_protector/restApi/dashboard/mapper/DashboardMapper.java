package oms.pc_protector.restApi.dashboard.mapper;

import oms.pc_protector.restApi.dashboard.model.DashboardPeriodVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DashboardMapper {

    public Integer selectAvgScoreByMonth(String idx);

    public Integer selectClientCount(String idx);

    public Integer selectAvgScoreByRecentMonths(@Param(value = "month") String month,
                                                @Param(value = "idx") String idx);

    public Integer selectUserCountByScore(@Param("startScore") int startScore,
                                          @Param("endScore") int endScore,
                                          @Param("idx") String idx);

    public List<DashboardPeriodVO> selectAllDashboardPeriod();

    public DashboardPeriodVO selectDashboardPeriod(String idx);

    public void dashboardPeriodUpdate(DashboardPeriodVO date);

}
