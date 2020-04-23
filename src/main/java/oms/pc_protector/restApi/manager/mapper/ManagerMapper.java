package oms.pc_protector.restApi.manager.mapper;

import oms.pc_protector.restApi.manager.model.ManagerVO;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerMapper {
    public ManagerVO searchManager();
}
