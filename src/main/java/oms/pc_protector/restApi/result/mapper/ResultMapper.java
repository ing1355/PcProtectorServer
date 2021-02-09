package oms.pc_protector.restApi.result.mapper;

import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.result.model.ResponseResultVO;
import oms.pc_protector.restApi.result.model.ResultVO;
import oms.pc_protector.restApi.result.model.SearchInputVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultMapper {

    public List<ResponseResultVO> selectResultAll(String departmentIdx);

    public List<ResponseResultVO> selectBySearchInput(SearchInputVO searchInputVO);

    public List<ResultVO> findUserDetailStaticInfo(String id, String departmentIdx);

    public List<ResultVO> selectById(String id);

    public int selectClientForMiss(ResultVO resultVO);

    public int selectExistByDay(String day, String departmentIdx);

    public int selectCountRunByMonth(String departmentIdx);

    public List<Integer> selectScoreByDepartmentWithMonth(
            @Param("departmentIdx") String departmentIdx,
            @Param("UserIdx") String UserIdx);

    public int selectByScheduleIsExist(@Param(value = "userId") String userId,
                                       @Param(value = "ipAddress") String IpAddress,
                                       @Param(value = "rootIdx") String rootIdx);

    public void insertResult(ResultVO resultVO);

    public void insertEmptyResultBySchedule(ClientVO clientVO);

    public boolean updateResultClient(ResultVO resultVO);

    public void updateResultClientNotInSchedule(ResultVO resultVO);
}
