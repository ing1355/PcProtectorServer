package oms.pc_protector.restApi.client.mapper;

import oms.pc_protector.restApi.client.model.ClientVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientMapper {

    public ClientVO selectById(String id, String UserIdx);

    public Integer selectClient(String id, String code);

    public List<ClientVO> selectClientHistory(String userId);

    public void loginUpdateTime(String id);

    public int selectClientByIdIp(ClientVO clientVO);

    public int selectSameIpAddress(String IpAddress);

    public ClientVO selectClientHistoryFirstById(String userId);

    public int insertClientInfo(ClientVO clientVO);

    public int insertClientHistory(ClientVO clientVO);

    public List<ClientVO> selectClientAll(String idx);

    public List<ClientVO> selectClientListById(String id);

    public boolean updateClientInfo(ClientVO clientVO);

    public boolean updateClientHistoryInfo(ClientVO clientVO);

    public void updateWrongMd5(ClientVO clientVO);

    public void deleteClientDuplicated(ClientVO clientVO);
}
