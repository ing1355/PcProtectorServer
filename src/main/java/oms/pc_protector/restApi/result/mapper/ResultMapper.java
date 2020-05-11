package oms.pc_protector.restApi.result.mapper;

import oms.pc_protector.restApi.result.model.ResponseResultVO;
import oms.pc_protector.restApi.result.model.ResultProcessVO;
import oms.pc_protector.restApi.result.model.ResultVO;
import oms.pc_protector.restApi.result.model.SearchInputVO;
import oms.pc_protector.restApi.user.model.UserRequestVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


import java.util.LinkedHashMap;
import java.util.List;

@Repository
public interface ResultMapper {



    public List<?> selectCheckedResult();

    public List<ResponseResultVO> selectResultAll();

    public List<ResponseResultVO> selectBySearchInput(SearchInputVO searchInputVO);

    public List<ResultVO> selectById(String id);

    public List<ResultProcessVO> selectResultProcessById(String ipAddress, String checkTime);

    public ResultVO selectResultDetailsById(String ipAddress, String checkTime);

    public int selectCountAllByMonth(String month);

    public List<Integer> selectScoreByDepartmentWithMonth(@Param("department") String department,
                                                 @Param("month") String month);

    public void insertResult(ResultVO resultVO);

    public void insertResultProcess(ResultProcessVO resultProcessVO);
}
