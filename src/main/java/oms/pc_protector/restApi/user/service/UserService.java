package oms.pc_protector.restApi.user.service;

import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.restApi.client.mapper.ClientMapper;
import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.client.service.ClientService;
import oms.pc_protector.restApi.dashboard.mapper.DashboardMapper;
import oms.pc_protector.restApi.dashboard.model.DashboardPeriodVO;
import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.service.DepartmentService;
import oms.pc_protector.restApi.result.mapper.ResultMapper;
import oms.pc_protector.restApi.user.mapper.UserMapper;
import oms.pc_protector.restApi.user.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class UserService {

    private final UserMapper userMapper;
    private final ClientMapper clientMapper;
    private final ClientService clientService;
    private final DepartmentService departmentService;
    private final ResultMapper resultMapper;
    private final DashboardMapper dashboardMapper;

    public UserService(ClientMapper clientMapper,
                       ClientService clientService,
                       DepartmentService departmentService,
                       ResultMapper resultMapper,
                       DashboardMapper dashboardMapper,
                       UserMapper userMapper) {
        this.userMapper = userMapper;
        this.clientMapper = clientMapper;
        this.clientService = clientService;
        this.departmentService = departmentService;
        this.resultMapper = resultMapper;
        this.dashboardMapper = dashboardMapper;
    }


    @Transactional(readOnly = true)
    public List<UserVO> findAll(String User_Idx) {
        return Optional.ofNullable(userMapper.selectUserInfoAll(User_Idx))
                .orElseGet(ArrayList::new);
    }


    @Transactional
    public boolean findSameId(String id, String User_Idx) {
        int result = userMapper.selectSameId(id, User_Idx);
        return result > 0;
    }


    @Transactional(readOnly = true)
    public List<UserVO> findBySearchInput(UserSearchInputVO userSearchVO) {
        List<UserVO> userList = new ArrayList<>();
        if (userSearchVO.getDepartmentIdx() != null) {
            Long code = userSearchVO.getDepartmentIdx();
            List<DepartmentVO> childCodeList = new ArrayList<>();
            childCodeList.add(departmentService.findByDepartmentIdx(code));
            childCodeList.addAll(departmentService.findChildAscByParentCode(code));
            for (DepartmentVO childCode : childCodeList) {
                userSearchVO.setDepartmentIdx(childCode.getCode());
                userList.addAll(userMapper.search(userSearchVO));
            }
            return userList;
        }
        userList.addAll(userMapper.search(userSearchVO));
        return userList;
    }

    @Transactional(readOnly = true)
    public UserVO findById(String id, String code) {
        return Optional.ofNullable(userMapper.selectById(id, code))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));
    }

    @Transactional
    public void registryFromAdmin(UserRequestVO userRequestVO) {
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
            log.info("DEPARTMENTIDX : {}", user.getDepartmentIdx());
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
    public boolean removeUserInfo(String id, String User_Idx) {
        return Optional.of(userMapper.deleteUserInfo(id, User_Idx))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));
    }


    @Transactional
    public boolean agentLogin(ClientVO clientVO, String rootIdx) throws ParseException {
        ClientVO client_prev = clientMapper.selectById(clientVO.getUserId(), clientVO.getDepartmentIdx());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DashboardPeriodVO dashboardPeriodVO = dashboardMapper.selectDashboardPeriod(rootIdx);
        Date d1 = df.parse(dashboardPeriodVO.getStartDate());
        Date d2 = df.parse(dashboardPeriodVO.getEndDate());
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        start.setTime(d1);
        end.setTime(d2);

        if (client_prev != null) {
            log.info("클라이언트 PC 정보 업데이트 : " + clientVO.getUserId() + " / " + clientVO.getIpAddress());

            clientVO.setCheckTime(df.format(now.getTime()));
            if (resultMapper.selectByScheduleIsExist(clientVO.getUserId(), clientVO.getIpAddress(), rootIdx) == 0 &&
                start.getTime().compareTo(now.getTime()) <= 0 && end.getTime().compareTo(now.getTime()) >= 0) {
                resultMapper.insertEmptyResultBySchedule(clientVO);
                log.info("teststsetestestestest");
            }
            clientService.First_update(clientVO, client_prev);
        } else {
            log.info("등록되지 않은 사용자입니다.");
            return false;
        }
        return true;
    }
}

