package oms.pc_protector.restApi.clientFile.mapper;

import oms.pc_protector.restApi.clientFile.model.ClientFileVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientFileMapper {

    public ClientFileVO selectClientFileAll();

    public int selectExistFile();

    public void insertClientFile(ClientFileVO clientFileVO);

    public void update(ClientFileVO clientFileVO);

    public void deleteClientFile(ClientFileVO clientFileVO);
}
