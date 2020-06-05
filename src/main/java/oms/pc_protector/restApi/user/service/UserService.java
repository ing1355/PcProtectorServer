package oms.pc_protector.restApi.user.service;

import oms.pc_protector.restApi.client.service.ClientService;
import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.service.DepartmentService;
import oms.pc_protector.restApi.policy.model.PeriodDateVO;
import oms.pc_protector.restApi.policy.service.ConfigurationService;
import oms.pc_protector.restApi.result.mapper.ResultMapper;
import oms.pc_protector.restApi.user.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.client.mapper.ClientMapper;
import oms.pc_protector.restApi.user.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Log4j2
@Service
public class UserService {

    private final UserMapper userMapper;
    private final ClientMapper clientMapper;
    private final ClientService clientService;
    private final DepartmentService departmentService;
    private final ResultMapper resultMapper;

    public UserService(UserMapper userMapper,
                       ClientMapper clientMapper,
                       ClientService clientService,
                       DepartmentService departmentService,
                       ResultMapper resultMapper) {
        this.userMapper = userMapper;
        this.clientMapper = clientMapper;
        this.clientService = clientService;
        this.departmentService = departmentService;
        this.resultMapper = resultMapper;
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
        if (userSearchVO.getDepartmentCode() != null) {
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
    public boolean duplicateCheckClient(ClientVO clientVO) {
        int result = clientService.findSameClient(clientVO);
        return result > 0;
    }

    @Transactional(readOnly = true)
    public boolean duplicateCheckIpAddress(String IpAddress) {
        int result = clientService.findSameIpAddress(IpAddress);
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
        if (userRequestVO.getEmail() == null) {
            userRequestVO.setEmail("이메일 없음");
        }
        if (userRequestVO.getPhone() == null) {
            userRequestVO.setPhone("전화번호 없음");
        }
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
    public boolean agentLogin(ClientVO clientVO, ConfigurationService configurationService) {
        boolean duplicateId = duplicateCheckId(clientVO.getUserId());
        ClientVO client_prev = clientMapper.selectById(clientVO.getUserId());

        if (duplicateId) {
            log.info("클라이언트 PC 정보 업데이트 : " + clientVO.getUserId() + " / " + clientVO.getIpAddress());
            if(client_prev.getIpAddress() != null && !(client_prev.getIpAddress().equals(clientVO.getIpAddress()))) {
                resultMapper.updateResultByUpdateClient(clientVO);
            }
            PeriodDateVO periodDateVO = configurationService.findAppliedSchedule();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            Calendar now = Calendar.getInstance();

            if (periodDateVO.getPeriod() == 1) { // 매달
                start.set(Calendar.WEEK_OF_MONTH, periodDateVO.getFromWeek());
                start.set(Calendar.DAY_OF_WEEK, periodDateVO.getFromDay() + 1);
                start.set(Calendar.HOUR_OF_DAY, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
                end.set(Calendar.WEEK_OF_MONTH, periodDateVO.getToWeek());
                end.set(Calendar.DAY_OF_WEEK, periodDateVO.getToDay() + 1);
                end.set(Calendar.HOUR_OF_DAY, 23);
                end.set(Calendar.MINUTE, 59);
                end.set(Calendar.SECOND, 59);
            } else if (periodDateVO.getPeriod() == 2) { // 매주
                start.set(Calendar.DAY_OF_WEEK, periodDateVO.getFromDay() + 1);
                start.set(Calendar.HOUR_OF_DAY, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
                end.set(Calendar.DAY_OF_WEEK, periodDateVO.getToDay() + 1);
                end.set(Calendar.HOUR_OF_DAY, 23);
                end.set(Calendar.MINUTE, 59);
                end.set(Calendar.SECOND, 59);
            } else { // 매일
                start.set(Calendar.HOUR_OF_DAY, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
                end.set(Calendar.HOUR_OF_DAY, 23);
                end.set(Calendar.MINUTE, 59);
                end.set(Calendar.SECOND, 59);
            }

            clientVO.setCheckTime(df.format(now.getTime()));

            if (resultMapper.selectByScheduleIsExist((dft.format(start.getTime())),
                    dft.format(end.getTime()), clientVO.getUserId(), clientVO.getIpAddress()) == 0) {
                resultMapper.insertEmptyResultBySchedule(clientVO);
            }
            clientService.First_update(clientVO);
        } else {
            log.info("등록되지 않은 사용자입니다.");
            return false;
        }
        return true;
    }


}

