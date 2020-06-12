package oms.pc_protector.restApi.process.mapper;

import oms.pc_protector.restApi.process.model.ProcessVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessMapper {

    public List<ProcessVO> selectProcessAll();

    public List<ProcessVO> selectProcessList(String processType);
    public int existProcess(ProcessVO processVO);
    public List<ProcessVO> searchProcess(@Param(value = "displayName") String displayName,
                                         @Param(value = "registryName") String registryName);

    public int insertProcess(ProcessVO processVO);
    public int insertUnApprovedProcess(ProcessVO processVO);
    public int insertRequiredProcess(ProcessVO processVO);

    public void modifyToUnApprovedProcess(Long idx);
    public void modifyToRequiredProcess(Long idx);

    public void processUpdate(ProcessVO processVO);

    public void processDelete(ProcessVO processVO);

    public int deleteUnApprovedProcess(ProcessVO processVO);
    public int deleteRequiredProcess(ProcessVO processVO);
}
