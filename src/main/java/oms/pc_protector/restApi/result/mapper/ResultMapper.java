package oms.pc_protector.restApi.result.mapper;

import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.policy.model.NowScheduleVO;
import oms.pc_protector.restApi.result.model.ResponseResultVO;
import oms.pc_protector.restApi.result.model.ResultProcessVO;
import oms.pc_protector.restApi.result.model.ResultVO;
import oms.pc_protector.restApi.result.model.SearchInputVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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

    public int selectExistByDay(String day);

    public int selectCountRunByMonth(@Param(value = "startDate") String startDate,
                                     @Param(value = "endDate") String endDate);

    public int selectCountBySchedule(NowScheduleVO nowScheduleVO);

    public int selectCountByNowScheduleMonth(@Param(value = "month") String month);

    public List<Integer> selectScoreByDepartmentWithMonth(
            @Param("department") String department,
            @Param(value = "startDate") String startDate,
            @Param(value = "endDate") String endDate);

    public int selectByScheduleIsExist(@Param(value = "startChecktime") String startChecktime,
                                       @Param(value = "endChecktime") String endChecktime,
                                       @Param(value = "userId") String userId,
                                       @Param(value = "ipAddress") String IpAddress);

    public void insertResult(ResultVO resultVO);

    public void insertResultProcess(ResultProcessVO resultProcessVO);

    public void insertEmptyResultBySchedule(ClientVO clientVO);

    public boolean updateResultClient(ResultVO resultVO);

    public void updateResultClientNotInSchedule(ResultVO resultVO);

    public void updateResultByUpdateClient(ClientVO clientVO);
}
