package oms.pc_protector.restApi.result.mapper;

import ch.qos.logback.core.net.server.Client;
import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.result.model.ResponseResultVO;
import oms.pc_protector.restApi.result.model.ResultProcessVO;
import oms.pc_protector.restApi.result.model.ResultVO;
import oms.pc_protector.restApi.result.model.SearchInputVO;
import oms.pc_protector.restApi.user.model.UserRequestVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.springframework.stereotype.Repository;


import java.util.LinkedHashMap;
import java.util.List;

@Repository
public interface ResultMapper {

    public List<ResponseResultVO> selectResultAll();

    public List<ResponseResultVO> selectBySearchInput(SearchInputVO searchInputVO);

    public List<ResultVO> findUserDetailStaticInfo(String id);

    public List<ResultVO> selectById(String id);

    public List<ResultProcessVO> selectResultProcessById(String ipAddress, String checkTime);

    public ResultVO selectResultDetailsById(String ipAddress, String checkTime);

    public int selectClientForMiss(ResultVO resultVO);

    public int selectCountRunByMonth(@Param(value = "startDate") String startDate,
                                     @Param(value = "endDate") String endDate);

    public List<Integer> selectScoreByDepartmentWithMonth(
            @Param("department") String department);

    public int selectByScheduleIsExist(@Param(value = "startChecktime") String startChecktime,
                                       @Param(value = "endChecktime") String endChecktime,
                                       @Param(value = "userId") String userId,
                                       @Param(value = "ipAddress") String IpAddress);

    public void insertResult(ResultVO resultVO);

    public void insertResultProcess(ResultProcessVO resultProcessVO);

    public void insertEmptyResultBySchedule(ClientVO clientVO);

    public void updateResultClient(ResultVO resultVO);
}
