package oms.pc_protector.restApi.user.mapper;

import oms.pc_protector.restApi.user.model.RequestUserVO;
import oms.pc_protector.restApi.user.model.UserRequestVO;
import oms.pc_protector.restApi.user.model.UserResponseVO;
import oms.pc_protector.restApi.user.model.UserVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface UserMapper {

    public List<UserVO> selectUserInfoAll();

    public UserVO selectById(String id);

    public List<UserVO> selectByDepartment(String departmentName);

    public UserResponseVO selectUserWithClientByIpAddress(String ipAddress);

    public UserResponseVO selectUserWithClientInfoById(String id);

    public void insertUserInfo(String id);

    public void insertUserInfoUserInfoFromAdmin(UserRequestVO userRequestVO);

    public void RegisterUserInfo(UserVO userVO);
    public boolean updateUserInfo(UserVO userVO);

    public boolean updateUserInfo_Front(RequestUserVO requestUserVO);

    public boolean deleteUserInfo(String id);
    public void deleteAllUserInfo();
}
