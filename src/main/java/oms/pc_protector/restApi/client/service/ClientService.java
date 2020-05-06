package oms.pc_protector.restApi.client.service;

import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.client.mapper.ClientMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private ClientMapper clientMapper;

    public ClientService(ClientMapper clientMapper) {
        this.clientMapper = clientMapper;
    }


    @Transactional
    public List<ClientVO> findClientById(String id) {
        return clientMapper.selectClientById(id);
    }


    @Transactional
    public void registerWrongMd5(ClientVO clientVO) {
        clientMapper.updateWrongMd5(clientVO);
    }


    @Transactional
    public List<ClientVO> findAll() {
        return Optional.ofNullable(clientMapper.selectClientAll())
                .orElse(Collections.EMPTY_LIST);
    }


    @Transactional
    public List<ClientVO> findById(String id) {
        return clientMapper.selectClientById(id);
    }


    @Transactional
    public void register(ClientVO clientVO){
        int test = clientMapper.insertClientInfo(clientVO);
    }


    @Transactional
    public void update(ClientVO clientVO){
        clientMapper.updateClientInfo(clientVO);
    }
}
