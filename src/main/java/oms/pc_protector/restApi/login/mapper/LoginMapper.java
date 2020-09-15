package oms.pc_protector.restApi.login.mapper;

import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.login.model.ClientLoginVO;
import oms.pc_protector.restApi.login.model.LoginVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginMapper {

    public int isExistId(String id);

    public int loginForClientFirst(ClientVO clientVO);

    public ClientVO findClient(ClientLoginVO loginVO);

    public String findPasswordById(String id);
}
