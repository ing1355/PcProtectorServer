package oms.pc_protector.restApi.user.mapper;

import oms.pc_protector.restApi.department.model.UpdateDepartmentVO;
import oms.pc_protector.restApi.user.model.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    public List<UserVO> selectUserInfoAll();

    public UserVO selectById(String id);

    public int selectSameId(String id);

    public List<UserVO> search(UserSearchInputVO userSearchVO);

    public List<UserVO> selectByDepartment(String departmentName);

    public List<UserVO> selectByDepartmentCode(Long departmentCode);

    public UserResponseVO selectUserWithClientByIpAddress(String ipAddress);

    public UserResponseVO selectUserWithClientInfoById(String id);

    public void insertUserInfo(String id);

    public void insertUserInfoUserInfoFromAdmin(UserRequestVO userRequestVO);

    public void RegisterUserInfo(UserVO userVO);

    public boolean updateUserInfo(UserVO userVO);

    public boolean updateUserInfo_Front(RequestUserVO requestUserVO);

    public void departmentModified(UpdateDepartmentVO updateDepartmentVO);

    public boolean deleteUserInfo(String id);

    public void deleteAllUserInfo();
}
