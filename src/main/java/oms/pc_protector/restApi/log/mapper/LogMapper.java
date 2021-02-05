package oms.pc_protector.restApi.log.mapper;


import oms.pc_protector.restApi.log.model.LogRequestVO;
import oms.pc_protector.restApi.log.model.LogVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogMapper {

    public List<?> getAllLog(String departmentIdx);

    public List<?> search(LogRequestVO logRequestVO);

    public void insert(LogVO logVO);
}