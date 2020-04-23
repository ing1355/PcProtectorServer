package oms.pc_protector.restApi.clientFile.service;

import oms.pc_protector.restApi.clientFile.mapper.ClientFileMapper;
import oms.pc_protector.restApi.clientFile.model.ClientFileVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientFileService {

    private final ClientFileMapper clientFileMapper;

    public ClientFileService(ClientFileMapper clientFileMapper) {
        this.clientFileMapper = clientFileMapper;
    }

    @Transactional
    public List<ClientFileVO> findClientFileAll() {
        return Optional.ofNullable(clientFileMapper.selectClientFileAll())
                .orElseThrow(() -> new RuntimeException("등록된 파일이 없습니다."));
    }


    @Transactional
    public ClientFileVO findRecentClientFile() {
        return Optional.ofNullable(clientFileMapper.selectRecentClientFile())
                .orElseGet(ClientFileVO::new);
    }


    @Transactional
    public String findRecentMd5() {
        return Optional.ofNullable(findRecentClientFile())
                .map(ClientFileVO::getMd5).orElse("");
    }


    @Transactional
    public void registerClientFile(ClientFileVO clientFileVO) {
        clientFileMapper.insertClientFile(clientFileVO);
    }


    @Transactional
    public void removeClientFile(ClientFileVO clientFileVO) {
        clientFileMapper.deleteClientFile(clientFileVO);
    }


}
