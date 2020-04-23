package oms.pc_protector.restApi.result.mapper;

import oms.pc_protector.restApi.result.model.ResultProcessVO;
import oms.pc_protector.restApi.result.model.ResultVO;
import oms.pc_protector.restApi.user.model.UserRequestVO;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ResultMapper {

    public List<?> selectResultAll();

    public List<?> selectByUserNameWithIpAddress(UserRequestVO userRequestVO);

    public List<ResultProcessVO> selectResultProcessById(String ipAddress, String checkTime);

    public ResultVO selectResultDetailsById(String ipAddress, String checkTime);

    public void insertResult(ResultVO resultVO);

    public void insertResultProcess(ResultProcessVO resultProcessVO);
}
