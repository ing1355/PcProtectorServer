package oms.pc_protector.restApi.client.service;

import oms.pc_protector.restApi.client.mapper.ClientMapper;
import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.login.service.LoginService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {

    private ClientMapper clientMapper;
    private LoginService loginservice;

    public ClientService(ClientMapper clientMapper, LoginService loginService) {
        this.clientMapper = clientMapper;
        this.loginservice = loginService;
    }

    public void loginUpdateTime(String id) {
        clientMapper.loginUpdateTime(id);
    }

    public List<ClientVO> selectClientListById(String id) {
        return clientMapper.selectClientListById(id);
    }

    public boolean findClient(String id, String code) {
        return clientMapper.selectClient(id, code) > 0;
    }

    @Transactional
    public int findSameIpAddress(String IpAddress) {
        return clientMapper.selectSameIpAddress(IpAddress);
    }

    @Transactional
    public void register(ClientVO clientVO) {
        if(clientMapper.selectClientByIdIp(clientVO) == 0) {
            if(loginservice.loginForClientFirst(clientVO)) {
                clientMapper.deleteClientDuplicated(clientVO);
                clientMapper.insertClientInfo(clientVO);
            } else { // 첫 로그인
                clientMapper.insertClientInfo(clientVO);
            }
        }
    }

    @Transactional
    public void First_update(ClientVO clientVO, ClientVO history) {
        if(history.getIpAddress() == null) { // 신규 가입
            clientMapper.insertClientHistory(clientVO);
        } else if(!clientVO.getMacAddress().equals(history.getMacAddress())) { // ip 변경
            clientMapper.insertClientHistory(clientVO);
        } else {
            clientMapper.updateClientHistoryInfo(clientVO);
        }
        clientMapper.updateClientInfo(clientVO);
    }


    @Transactional
    public void registerWrongMd5(ClientVO clientVO) {
        clientMapper.updateWrongMd5(clientVO);
    }
}
