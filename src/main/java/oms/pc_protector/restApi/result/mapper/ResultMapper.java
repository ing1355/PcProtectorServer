package oms.pc_protector.restApi.result.mapper;

import oms.pc_protector.restApi.result.model.ResultProcessVO;
import oms.pc_protector.restApi.result.model.ResultVO;
import oms.pc_protector.restApi.user.model.UserRequestVO;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ResultMapper {

    public List<?> selectAllResult();
    public List<?> selectCheckedResult();
    public List<?> selectUnCheckedResult();

    public List<?> selectByUserInput(UserRequestVO userRequestVO);

    public List<ResultVO> selectById(String id);

    public List<ResultProcessVO> selectResultProcessById(String ipAddress, String checkTime);

    public ResultVO selectResultDetailsById(String ipAddress, String checkTime);

    public void insertResult(ResultVO resultVO);

    public void insertResultProcess(ResultProcessVO resultProcessVO);
}
