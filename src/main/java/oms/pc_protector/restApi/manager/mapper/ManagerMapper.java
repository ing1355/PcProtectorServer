package oms.pc_protector.restApi.manager.mapper;

import oms.pc_protector.restApi.manager.model.ManagerVO;
import oms.pc_protector.restApi.manager.model.RequestManagerVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerMapper {
    public List<ManagerVO> searchManager();
    public void updateManagerInfo(RequestManagerVO requestManagerVO);
    public void insertManager(ManagerVO managerVO);
    public boolean deleteManager(String id);
}
