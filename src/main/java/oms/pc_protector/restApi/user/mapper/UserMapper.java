package oms.pc_protector.restApi.user.mapper;

import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.model.UpdateDepartmentVO;
import oms.pc_protector.restApi.user.model.RequestUserVO;
import oms.pc_protector.restApi.user.model.UserRequestVO;
import oms.pc_protector.restApi.user.model.UserResponseVO;
import oms.pc_protector.restApi.user.model.UserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface UserMapper {

    public List<UserVO> selectUserInfoAll();

    public UserVO selectById(String id);

    public List<UserVO> search(@Param(value = "userId") String userId,
                               @Param(value = "name") String name,
                               @Param(value = "department") String department,
                               @Param(value = "phone") String phone);


    public List<UserVO> selectByDepartment(String departmentName);

    public UserResponseVO selectUserWithClientByIpAddress(String ipAddress);

    public UserResponseVO selectUserWithClientInfoById(String id);

    public void insertUserInfo(String id);

    public void insertUserInfoUserInfoFromAdmin(UserRequestVO userRequestVO);

    public void RegisterUserInfo(UserVO userVO);

    public boolean updateUserInfo(UserVO userVO);

    public boolean updateUserInfo_Front(RequestUserVO requestUserVO);

    public void departmentModified(UpdateDepartmentVO updateDepartmentVO);

    public boolean departmentDeletedChild(String departmentName);
    public boolean departmentDeletedFirst(String departmentName);

    public boolean deleteUserInfo(String id);

    public void deleteAllUserInfo();
}
