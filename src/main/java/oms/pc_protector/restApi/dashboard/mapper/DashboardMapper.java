package oms.pc_protector.restApi.dashboard.mapper;

import oms.pc_protector.restApi.dashboard.model.ChartVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DashboardMapper {

    public Integer selectAvgScoreByMonth(String month);

    public List<ChartVO> selectAvgScoreByRecent1Months();
    public List<ChartVO> selectAvgScoreByRecent6Months();

    public Integer selectUserCountByScore(@Param("startScore") int startScore,
                                          @Param("endScore") int endScore);
}
