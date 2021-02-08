package oms.pc_protector.restApi.user.mapper;

import oms.pc_protector.restApi.user.model.RequestUserVO;
import oms.pc_protector.restApi.user.model.UserRequestVO;
import oms.pc_protector.restApi.user.model.UserSearchInputVO;
import oms.pc_protector.restApi.user.model.UserVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    public List<UserVO> selectUserInfoAll(String departmentIdx);

    public UserVO selectById(String id, String code);

    public int selectSameId(String id, String departmentIdx);

    public List<UserVO> search(UserSearchInputVO userSearchVO);

    public void insertUserInfo(String id);

    public void insertUserInfoUserInfoFromAdmin(UserRequestVO userRequestVO);

    public void RegisterUserInfo(UserVO userVO);

    public boolean updateUserInfo_Front(RequestUserVO requestUserVO);

    public boolean deleteUserInfo(String id, String UserIdx);

    public void deleteAllUserInfo(String UserIdx);
}
