package oms.pc_protector.restApi.client.mapper;

import oms.pc_protector.restApi.client.model.ClientVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientMapper {

    public ClientVO selectById(String id, String departmentIdx);

    public String selectClient(String id, String departmentIdx);

    public String selectDepartmentByDepartmentIdx(String departmentidx);

    public void loginUpdateTime(String id, String departmentIdx);

    public int selectClientByIdIp(ClientVO clientVO);

    public ClientVO selectClientHistoryFirstById(String userId);

    public int insertClientInfo(ClientVO clientVO);

    public int insertClientHistory(ClientVO clientVO);

    public List<ClientVO> selectClientAll(String departmentIdx);

    public List<ClientVO> selectClientListById(String id, String departmentIdx);

    public boolean updateClientInfo(ClientVO clientVO);

    public boolean updateClientHistoryInfo(ClientVO clientVO);

    public void updateWrongMd5(ClientVO clientVO);

    public void deleteClientDuplicated(ClientVO clientVO);
}
