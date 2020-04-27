package oms.pc_protector.restApi.manager.service;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.manager.mapper.ManagerMapper;
import oms.pc_protector.restApi.manager.model.ManagerVO;
import oms.pc_protector.restApi.manager.model.RequestManagerVO;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Log4j2
@Service
public class ManagerService {

    private final ManagerMapper managerMapper;


    public ManagerService(ManagerMapper managerMapper) {
        this.managerMapper = managerMapper;
    }

    @Transactional(readOnly = true)
    public List<ManagerVO> findAll() {
        return Optional.ofNullable(managerMapper.searchManager()).orElseGet(() -> Collections.EMPTY_LIST);
    }

    @Transactional
    public void insertManager(ManagerVO managerVO) {
        managerMapper.insertManager(managerVO);
    }

    @Transactional
    public void updateManager(RequestManagerVO requestManagerVO) {
        managerMapper.updateManagerInfo(requestManagerVO);
    }

    @Transactional
    public boolean removeManager(String id) {
        return Optional.of(managerMapper.deleteManager(id))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));
    }
}
