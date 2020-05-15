package oms.pc_protector.restApi.user.service;

import oms.pc_protector.restApi.client.service.ClientService;
import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.service.DepartmentService;
import oms.pc_protector.restApi.user.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.client.mapper.ClientMapper;
import oms.pc_protector.restApi.user.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Log4j2
@Service
public class UserService {

    private final UserMapper userMapper;
    private final ClientMapper clientMapper;
    private final ClientService clientService;
    private final DepartmentService departmentService;

    public UserService(UserMapper userMapper,
                       ClientMapper clientMapper,
                       ClientService clientService,
                       DepartmentService departmentService) {
        this.userMapper = userMapper;
        this.clientMapper = clientMapper;
        this.clientService = clientService;
        this.departmentService = departmentService;
    }


    @Transactional(readOnly = true)
    public List<UserVO> findAll() {
        return Optional.ofNullable(userMapper.selectUserInfoAll())
                .orElseGet(ArrayList::new);
    }



    @Transactional
    public boolean findSameId(String id) {
        int result = userMapper.selectSameId(id);
        return result > 0;
    }


    @Transactional(readOnly = true)
    public List<UserVO> findBySearchInput(UserSearchInputVO userSearchVO) {
        List<UserVO> userList = new ArrayList<>();
        if(userSearchVO.getDepartmentCode() != null) {
            Long code = userSearchVO.getDepartmentCode();
            List<DepartmentVO> childCodeList = new ArrayList<>();
            childCodeList.add(departmentService.findByDepartmentCode(code));
            childCodeList.addAll(departmentService.findChildAscByParentCode(code));
            for (DepartmentVO childCode : childCodeList) {
                userSearchVO.setDepartmentCode(childCode.getCode());
                userList.addAll(userMapper.search(userSearchVO));
            }
            return userList;
        }
        userList.addAll(userMapper.search(userSearchVO));
        return userList;
    }


    @Transactional(readOnly = true)
    public UserResponseVO findUserWithClientById(String id) {
        return Optional.ofNullable(userMapper.selectUserWithClientInfoById(id))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));
    }


    @Transactional(readOnly = true)
    public UserResponseVO findUserWithClientByIpAddress(String ipAddress) {
        return Optional.ofNullable(userMapper.selectUserWithClientByIpAddress(ipAddress))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이피입니다."));
    }


    @Transactional(readOnly = true)
    public UserVO findById(String id) {
        return Optional.ofNullable(userMapper.selectById(id))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));
    }


    @Transactional(readOnly = true)
    public List<UserVO> findByDepartment(String department) {
        return Optional.ofNullable(userMapper.selectByDepartment(department))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 부서입니다."));
    }


    @Transactional(readOnly = true)
    public List<UserVO> findByDepartmentCode(Long departmentCode) {
        return Optional.ofNullable(userMapper.selectByDepartmentCode(departmentCode))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 부서코드입니다."));
    }


    @Transactional(readOnly = true)
    public boolean duplicateCheckIpAddress(String ipAddress) {
        int result = clientService.findSameIpAddress(ipAddress);
        return result > 0;
    }


    @Transactional(readOnly = true)
    public boolean duplicateCheckId(String id) {
        List<UserVO> clientList = findAll();
        for (UserVO user : clientList) {
            if (user.getUserId().equals(id)) return true;
        }
        return false;
    }


    @Transactional
    public void registryFromAdmin(UserRequestVO userRequestVO) {
        boolean duplicateIpAddressCheck = duplicateCheckIpAddress(userRequestVO.getIpAddress());
        if (duplicateIpAddressCheck) return;
        userMapper.insertUserInfoUserInfoFromAdmin(userRequestVO);
    }


    @Transactional
    public void registryFromAdminList(List<UserVO> userVOList) {
        userMapper.deleteAllUserInfo();
        log.info("-------------------------");
        log.info("------사용자 등록 EXCEL----");
        for (UserVO user : userVOList) {
            log.info("-------------------------");
            log.info("ID : {}", user.getUserId());
            log.info("NAME {}: ", user.getName());
            log.info("DEPARTMENTCODE : {}", user.getDepartmentCode());
            log.info("DEPARTMENT : {}", user.getDepartment());
            log.info("PHONE : {}", user.getPhone());
            log.info("EMAIL : {}", user.getEmail());
            log.info("-------------------------");
            userMapper.RegisterUserInfo(user);
        }
        log.info("-------------------------");
    }


    @Transactional
    public boolean updateUserInfo(RequestUserVO requestUserVO) {
        return userMapper.updateUserInfo_Front(requestUserVO);
    }

    @Transactional
    public boolean modifyUserInfo(String id, UserRequestVO userRequestVO) {
        UserVO userVO = findById(id);
        userVO.setName(userRequestVO.getName());
        userVO.setDepartmentCode(userRequestVO.getDepartmentCode());
        userVO.setDepartment(userRequestVO.getDepartment());
        userVO.setEmail(userRequestVO.getEmail());
        userVO.setPhone(userRequestVO.getPhone());
        return userMapper.updateUserInfo(userVO);
    }


    @Transactional
    public boolean removeUserInfo(String id) {
        return Optional.of(userMapper.deleteUserInfo(id))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));
    }


    @Transactional
    public boolean agentLogin(ClientVO clientVO) {
        boolean duplicateIpAddress = duplicateCheckIpAddress(clientVO.getIpAddress());
        boolean duplicateId = duplicateCheckId(clientVO.getUserId());

        if (duplicateId) {
            if (duplicateIpAddress) {
                log.info("클라이언트 PC 정보 업데이트 : " + clientVO.getUserId() + " / " + clientVO.getIpAddress());
                clientService.update(clientVO);
                return true;
            }
            log.info("새로운 클라이언트 등록 : " + clientVO.getUserId() + " / " + clientVO.getIpAddress());
            clientService.register(clientVO);
        } else {
            log.info("등록되지 않은 사용자입니다.");
            return false;
        }
        return true;
    }


}

