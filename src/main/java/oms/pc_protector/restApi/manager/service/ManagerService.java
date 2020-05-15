package oms.pc_protector.restApi.manager.service;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.manager.mapper.ManagerMapper;
import oms.pc_protector.restApi.manager.model.ManagerVO;
import oms.pc_protector.restApi.manager.model.RequestManagerVO;
import oms.pc_protector.restApi.manager.model.SearchManagerVO;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Log4j2
@Service
public class ManagerService {

    private final ManagerMapper managerMapper;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ManagerService(ManagerMapper managerMapper) {
        this.managerMapper = managerMapper;
    }


    @Transactional(readOnly = true)
    public List<ManagerVO> findAll() {
        return Optional.ofNullable(managerMapper.findAll()).orElseGet(() -> Collections.EMPTY_LIST);
    }


    @Transactional(readOnly = true)
    public boolean duplicatedManager(String id) {
        return managerMapper.selectSameId(id) > 0;
    }


    @Transactional(readOnly = true)
    public List<ManagerVO> searchManager(SearchManagerVO searchManagerVO) {
        return Optional.ofNullable(managerMapper.searchManager(searchManagerVO)).orElseGet(() -> Collections.EMPTY_LIST);
    }


    @Transactional
    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }


    @Transactional
    public void insertManager(ManagerVO managerVO) {
        String encodedPassword = new BCryptPasswordEncoder().encode(managerVO.getPassword());
        log.info("암호화 전 비밀번호 : {}", managerVO.getPassword());
        log.info("암호화 후 비밀번호 : {}", encodedPassword);
        System.out.println(passwordEncoder.matches(managerVO.getPassword(), encodedPassword));
        managerVO.setPassword(encodedPassword);
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
