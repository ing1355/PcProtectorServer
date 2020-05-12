package oms.pc_protector.restApi.statistics.mapper;

import oms.pc_protector.restApi.statistics.model.ResponseVO;
import oms.pc_protector.restApi.statistics.model.StatisticsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Repository
public interface StatisticsMapper {

    public List<LinkedHashMap> selectStatisticsByDepartment(ResponseVO responseVO);
}
