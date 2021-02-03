package oms.pc_protector.restApi.clientFile.mapper;

import oms.pc_protector.restApi.clientFile.model.ClientFileVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ClientFileMapper {

    public List<ClientFileVO> selectClientFile(String idx);

    public ClientFileVO selectClientFileRecent(String idx);

    public ArrayList<String> selectVersionList(String idx);

    public int selectExistFile(@Param(value = "version") String version,
                               @Param(value = "idx") String idx);
    public int selectExistMd5(@Param(value = "md5") String md5,
                              @Param(value = "idx") String idx);

    public void insertClientFile(ClientFileVO clientFileVO);

    public void update(ClientFileVO clientFileVO);

    public int deleteClientFile(ClientFileVO clientFileVO);
}
