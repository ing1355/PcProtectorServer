package oms.pc_protector.restApi.manager.service;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.manager.mapper.ManagerMapper;
import oms.pc_protector.restApi.manager.model.FirstLoginRequestManagerVO;
import oms.pc_protector.restApi.manager.model.ManagerLockVO;
import oms.pc_protector.restApi.manager.model.ManagerVO;
import oms.pc_protector.restApi.manager.model.SearchManagerVO;
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
    public ManagerVO findById(String id) {
        return Optional.ofNullable(managerMapper.findById(id))
                .orElseThrow(() -> new RuntimeException("운영자 아이디가 존재하지 않습니다."));
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
    public void insertManager(ManagerVO managerVO) {
        String encodedPassword = new BCryptPasswordEncoder().encode(managerVO.getPassword());
        System.out.println(encodedPassword);
        managerVO.setPassword(encodedPassword);
        managerVO.setRoles("MANAGER");
        managerMapper.insertManager(managerVO);
    }


    @Transactional
    public void updateManager(ManagerVO managerVO) {
        String encodedPassword = new BCryptPasswordEncoder().encode(managerVO.getPassword());
        log.info("암호화 전 비밀번호 : {}", managerVO.getPassword());
        log.info("암호화 후 비밀번호 : {}", encodedPassword);
        System.out.println(passwordEncoder.matches(managerVO.getPassword(), encodedPassword));
        managerVO.setPassword(encodedPassword);
        managerMapper.updateManagerInfo(managerVO);
    }

    @Transactional
    public void updateManagerFirstLogin(FirstLoginRequestManagerVO firstLoginRequestManagerVO) {
        String encodedPassword = new BCryptPasswordEncoder().encode(firstLoginRequestManagerVO.getPassword());
        log.info("암호화 후 비밀번호 : {}", encodedPassword);
        firstLoginRequestManagerVO.setPassword(encodedPassword);
        managerMapper.updateManagerInfoFirstLogin(firstLoginRequestManagerVO);
    }

    @Transactional
    public void updateManagerLock(ManagerLockVO managerLockVO) {
        managerMapper.updateManagerLock(managerLockVO);
    }

    @Transactional
    public void initManagerLock(String userId) {
        managerMapper.initManagerLock(userId);
    }

    @Transactional
    public void updateManagerUnLock(ManagerLockVO managerLockVO) {
        String encodedPassword = new BCryptPasswordEncoder().encode("dmFWh++LdJf6eBKb/uhDwFfBybghv3ajctRl8EDNGUE");
        managerLockVO.setPassword(encodedPassword);
        managerMapper.updateManagerUnLock(managerLockVO);
    }

    @Transactional
    public boolean removeManager(String id) {
        return Optional.of(managerMapper.deleteManager(id))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));
    }


}
