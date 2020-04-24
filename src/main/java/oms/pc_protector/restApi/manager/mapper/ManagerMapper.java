package oms.pc_protector.restApi.manager.mapper;

import oms.pc_protector.restApi.manager.model.ManagerVO;
import oms.pc_protector.restApi.manager.model.UpdateManagerVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerMapper {
    public List<ManagerVO> searchManager();
    public void updateManagerInfo(@Param("old_id")String old_id, @Param("new_data")ManagerVO new_data);
    public void insertManager(ManagerVO managerVO);
    public boolean deleteManager(String id);
}
