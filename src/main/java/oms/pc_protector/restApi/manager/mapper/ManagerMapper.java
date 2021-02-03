package oms.pc_protector.restApi.manager.mapper;

import oms.pc_protector.restApi.manager.model.FirstLoginRequestManagerVO;
import oms.pc_protector.restApi.manager.model.ManagerLockVO;
import oms.pc_protector.restApi.manager.model.ManagerVO;
import oms.pc_protector.restApi.manager.model.SearchManagerVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerMapper {

    public List<ManagerVO> findAll(String idx);

    public ManagerVO findById(String id);

    public List<ManagerVO> searchManager(SearchManagerVO searchManagerVO);

    public int selectSameId(@Param(value = "id") String id, @Param(value = "idx") String User_Idx);

    public void updateManagerInfo(ManagerVO managerVO);

    public void updateManagerInfoFirstLogin(FirstLoginRequestManagerVO firstLoginRequestManagerVO);

    public void updateManagerLock(ManagerLockVO managerLockVO);

    public void initManagerLock(String userId);

    public void updateManagerUnLock(ManagerLockVO managerLockVO);

    public void insertManager(ManagerVO managerVO);

    public boolean deleteManager(String id);
}
