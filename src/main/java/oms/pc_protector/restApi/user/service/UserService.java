package oms.pc_protector.restApi.user.service;

import oms.pc_protector.restApi.client.service.ClientService;
import oms.pc_protector.restApi.user.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.client.mapper.ClientMapper;
import oms.pc_protector.restApi.user.model.RequestUserVO;
import oms.pc_protector.restApi.user.model.UserRequestVO;
import oms.pc_protector.restApi.user.model.UserResponseVO;
import oms.pc_protector.restApi.user.model.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class UserService {

    private final UserMapper userMapper;
    private final ClientMapper clientMapper;
    private final ClientService clientService;

    public UserService(UserMapper userMapper,
                       ClientMapper clientMapper,
                       ClientService clientService) {
        this.userMapper = userMapper;
        this.clientMapper = clientMapper;
        this.clientService = clientService;
    }


    @Transactional(readOnly = true)
    public List<UserVO> findAll() {
        return Optional.ofNullable(userMapper.selectUserInfoAll())
                .orElseGet(() -> Collections.EMPTY_LIST);
    }

    @Transactional(readOnly = true)
    public List<UserVO> search(String userId, String name, String department, String phone) {
        return userMapper.search(userId, name, department, phone);
    }

    @Transactional(readOnly = true)
    public UserResponseVO findUserWithClientById(String id) {
        return Optional.ofNullable(userMapper.selectUserWithClientInfoById(id))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));
    }


    @Transactional(readOnly = true)
    public UserResponseVO findUserWithClientByIpAddress(String ipAddress) {
        return Optional.ofNullable(userMapper.selectUserWithClientByIpAddress(ipAddress))
                .orElseGet(UserResponseVO::new);
    }


    @Transactional(readOnly = true)
    public UserVO findById(String id) {
        return Optional.ofNullable(userMapper.selectById(id))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));
    }


    @Transactional(readOnly = true)
    public List<UserVO> findByDepartment(String departmentName) {
        return Optional.ofNullable(userMapper.selectByDepartment(departmentName))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 부서입니다."));
    }


    @Transactional(readOnly = true)
    public boolean duplicateCheckIpAddress(String ipAddress) {
        List<ClientVO> clientList = clientService.findAll();
        for (ClientVO client : clientList) {
            if (client.getIpAddress().equals(ipAddress)) return true;
        }
        return false;
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
        for (UserVO user : userVOList) {
            userMapper.RegisterUserInfo(user);
        }
    }


    @Transactional
    public boolean updateUserInfo(RequestUserVO requestUserVO) {
        return userMapper.updateUserInfo_Front(requestUserVO);
    }

    @Transactional
    public boolean modifyUserInfo(String id, UserRequestVO userRequestVO) {
        UserVO userVO = findById(id);
        userVO.setName(userRequestVO.getName());
        userVO.setDepartmentIdx(userRequestVO.getDepartmentIdx());
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
    public UserResponseVO clientLogin(ClientVO clientVO) {
        UserResponseVO result = new UserResponseVO();
        boolean duplicateIpAddressCheck = duplicateCheckIpAddress(clientVO.getIpAddress());
        boolean duplicateId = duplicateCheckId(clientVO.getUserId());
        if (!duplicateIpAddressCheck) {
            if (!duplicateId) {
                log.info("새로운 아이디 등록 : " + clientVO.getUserId());
                userMapper.insertUserInfo(clientVO.getUserId());
            }
            log.info("새로운 클라이언트 등록 : " + clientVO.getUserId() + " / " + clientVO.getIpAddress());
            clientService.register(clientVO);
        } else {
            log.info("클라이언트 PC 정보 업데이트 : " + clientVO.getUserId() + " / " + clientVO.getIpAddress());
            clientService.update(clientVO);
        }
        result = findUserWithClientByIpAddress(clientVO.getIpAddress());
        return result;
    }


}

