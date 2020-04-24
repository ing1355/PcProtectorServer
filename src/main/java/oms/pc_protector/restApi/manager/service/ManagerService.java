package oms.pc_protector.restApi.manager.service;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.manager.mapper.ManagerMapper;
import oms.pc_protector.restApi.manager.model.ManagerVO;
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
    public void updateManager(String id) throws JSONException {

        JSONObject jObject = new JSONObject(id);
        JSONObject new_data = new JSONObject();
        new_data = (JSONObject) jObject.get("new_data");
        ManagerVO managerVO = new ManagerVO();
        managerVO.setId((String) new_data.get("id"));
        managerVO.setName((String) new_data.get("name"));
        managerVO.setMobile((String) new_data.get("mobile"));
        managerVO.setEmail((String) new_data.get("email"));

        managerMapper.updateManagerInfo((String) jObject.get("old_id"),managerVO);
    }

    @Transactional
    public boolean removeManager(String id) {
        return Optional.of(managerMapper.deleteManager(id))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));
    }
}
