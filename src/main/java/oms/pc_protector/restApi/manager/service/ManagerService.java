package oms.pc_protector.restApi.manager.service;

import oms.pc_protector.restApi.manager.mapper.ManagerMapper;
import oms.pc_protector.restApi.manager.model.ManagerVO;
import oms.pc_protector.restApi.policy.model.ConfigurationVO;
import oms.pc_protector.restApi.policy.model.EditProgramVO;
import oms.pc_protector.restApi.policy.model.SecurityUsbDetailsVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
public class ManagerService {

    private final ManagerMapper managerMapper;

    public ManagerService(ManagerMapper managerMapper) {
        this.managerMapper = managerMapper;
    }
    @Transactional
    public ManagerVO findManagers() {
        return managerMapper.searchManager();
    }
}
