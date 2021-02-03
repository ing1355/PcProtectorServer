package oms.pc_protector.restApi.process.mapper;

import oms.pc_protector.restApi.process.model.ProcessVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessMapper {

    public List<ProcessVO> selectProcessAll(String idx);

    public List<ProcessVO> selectProcessList(@Param(value = "processType") String processType,
                                             @Param(value = "idx") String idx);
    public ProcessVO existProcess(ProcessVO processVO);
    public List<ProcessVO> searchProcess(@Param(value = "displayName") String displayName,
                                         @Param(value = "registryName") String registryName,
                                         @Param(value = "departmentIdx") String idx);

    public int insertProcess(ProcessVO processVO);
    public int insertUnApprovedProcess(ProcessVO processVO);
    public int insertRequiredProcess(ProcessVO processVO);

    public void modifyToUnApprovedProcess(Long idx);
    public void modifyToRequiredProcess(Long idx);

    public void processUpdate(ProcessVO processVO);

    public void processDelete(ProcessVO processVO);
}
