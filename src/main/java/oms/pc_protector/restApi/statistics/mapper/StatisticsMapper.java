package oms.pc_protector.restApi.statistics.mapper;

import oms.pc_protector.restApi.statistics.model.RunPcAndScoreVO;
import oms.pc_protector.restApi.statistics.model.StatisticsResponseVO;
import oms.pc_protector.restApi.statistics.model.StatisticsVO;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;

@Repository
public interface StatisticsMapper {

    public List<LinkedHashMap> selectStatisticsByDepartment(StatisticsResponseVO responseVO);

    public int countClientByMonth(StatisticsResponseVO responseVO);

    public int countClientByDepartment(Long departmentIdx);

    public RunPcAndScoreVO countRunPcByMonth(StatisticsResponseVO responseVO);

    public List<StatisticsVO> selectItemsByMonth(StatisticsResponseVO responseVO);
}
