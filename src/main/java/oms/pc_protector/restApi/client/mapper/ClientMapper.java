package oms.pc_protector.restApi.client.mapper;

import oms.pc_protector.restApi.client.model.ClientVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientMapper {

    public List<ClientVO> selectClientById(String id);

    public int selectSameIpAddress(String ipAddress);

    public int insertClientInfo(ClientVO clientVO);

    public List<ClientVO> selectClientAll();

    public boolean updateClientInfo(ClientVO clientVO);

    public void updateWrongMd5(ClientVO clientVO);
}
