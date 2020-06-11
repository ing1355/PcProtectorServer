package oms.pc_protector.restApi.clientFile.mapper;

import oms.pc_protector.restApi.clientFile.model.ClientFileVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientFileMapper {

    public List<ClientFileVO> selectClientFile();

    public ClientFileVO selectClientFileRecent();

    public int selectExistFile(@Param(value = "version") String version);
    public int selectExistMd5(@Param(value = "md5") String md5);

    public void insertClientFile(ClientFileVO clientFileVO);

    public void update(ClientFileVO clientFileVO);

    public void deleteClientFile(ClientFileVO clientFileVO);
}
