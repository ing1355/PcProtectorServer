package oms.pc_protector.restApi.statistics.mapper;

import oms.pc_protector.restApi.statistics.model.StatisticsResponseVO;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;

@Repository
public interface StatisticsMapper {

    public List<LinkedHashMap> selectStatisticsByDepartment(StatisticsResponseVO responseVO);

    public int countClientByMonth(StatisticsResponseVO responseVO);
}
