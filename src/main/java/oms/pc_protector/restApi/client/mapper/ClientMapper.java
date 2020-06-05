package oms.pc_protector.restApi.client.mapper;

import oms.pc_protector.restApi.client.model.ClientVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientMapper {

    public ClientVO selectById(String id);

    public int selectByIdCount(String id);

    public int selectSameClient(ClientVO clientVO);

    public ClientVO selectSameIdIpAddress(ClientVO clientVO);

    public int selectSameIpAddress(String IpAddress);

    public void loginUpdateTime(String id);

    public int selectCountAll();

    public int insertClientInfo(ClientVO clientVO);

    public List<ClientVO> selectClientAll();

    public boolean updateClientInfo(ClientVO clientVO);

    public void updateWrongMd5(ClientVO clientVO);

    public void deleteClientInfo(ClientVO clientVO);
}
