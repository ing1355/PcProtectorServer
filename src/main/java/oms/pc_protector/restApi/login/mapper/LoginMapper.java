package oms.pc_protector.restApi.login.mapper;

import org.springframework.stereotype.Repository;

@Repository
public interface LoginMapper {

    public int isExistId(String id);

    public String findPasswordById(String id);
}
