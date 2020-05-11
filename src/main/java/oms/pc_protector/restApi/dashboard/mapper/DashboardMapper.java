package oms.pc_protector.restApi.dashboard.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardMapper {

    public Integer selectAvgScoreByMonth(String month);

    public Integer selectUserCountByScore(@Param("startScore") int startScore,
                                          @Param("endScore") int endScore);
}
