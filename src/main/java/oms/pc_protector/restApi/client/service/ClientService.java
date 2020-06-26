package oms.pc_protector.restApi.client.service;

import oms.pc_protector.restApi.client.mapper.ClientMapper;
import oms.pc_protector.restApi.client.model.ClientVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private ClientMapper clientMapper;

    public ClientService(ClientMapper clientMapper) {
        this.clientMapper = clientMapper;
    }


    @Transactional
    public List<ClientVO> findAll() {
        return Optional.ofNullable(clientMapper.selectClientAll())
                .orElseGet(ArrayList::new);
    }

    public int count() {
        return clientMapper.selectCountAll();
    }

    public void loginUpdateTime(String id) {
        clientMapper.loginUpdateTime(id);
    }

    public ClientVO selectSameIdIpAddress(ClientVO clientVO) {
        return clientMapper.selectSameIdIpAddress(clientVO);
    }


    @Transactional
    public int findSameClient(ClientVO clientVO) {
        return clientMapper.selectSameClient(clientVO);
    }

    @Transactional
    public int findSameIpAddress(String IpAddress) {
        return clientMapper.selectSameIpAddress(IpAddress);
    }


    @Transactional
    public ClientVO findById(String id) {
        return clientMapper.selectById(id);
    }


    @Transactional
    public void register(ClientVO clientVO) {
        ClientVO temp = clientMapper.selectById(clientVO.getUserId());
        if (clientMapper.selectByIdCount(clientVO.getUserId()) > 0) {
            clientMapper.updateClientInfo(clientVO);
        } else {
            int test = clientMapper.insertClientInfo(clientVO);
        }
    }

    @Transactional
    public void First_update(ClientVO clientVO) {
        clientMapper.updateClientInfo(clientVO);
    }


    @Transactional
    public void registerWrongMd5(ClientVO clientVO) {
        clientMapper.updateWrongMd5(clientVO);
    }


    @Transactional
    public void update(ClientVO clientVO) {
        clientMapper.updateClientInfo(clientVO);
    }
}
