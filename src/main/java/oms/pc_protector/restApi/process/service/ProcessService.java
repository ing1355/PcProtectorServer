package oms.pc_protector.restApi.process.service;

import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.restApi.process.mapper.ProcessMapper;
import oms.pc_protector.restApi.process.model.ProcessVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProcessService {

    private final ProcessMapper processMapper;


    public ProcessService(ProcessMapper processMapper) {
        this.processMapper = processMapper;
    }


    /* Client로 받은 프로세스 목록을 등록한다. */
    @Transactional
    public int insertProcess(List<ProcessVO> processVOList, String idx) throws UnsupportedEncodingException {
        int resultNum = 0;
        if (processVOList == null) return resultNum;
        for (ProcessVO processVO : processVOList) {
            processVO.setDepartmentIdx(idx);
            if (processMapper.existProcess(processVO) == null) {
                resultNum += processMapper.insertProcess(processVO);
            }
        }

        return resultNum;
    }


    /* 해당 종류의 프로세스 모든 정보 목록을 가져온다.*/
    @Transactional(readOnly = true)
    public List<ProcessVO> findProcessByType(String processType, String idx) {
        return processMapper.selectProcessList(processType, idx);
    }

    @Transactional(readOnly = true)
    public ProcessVO existProcess(ProcessVO processVO) throws UnsupportedEncodingException {
        return processMapper.existProcess(processVO);
    }

    @Transactional(readOnly = true)
    public List<ProcessVO> searchProcess(String displayName, String registryName, String User_Idx) throws UnsupportedEncodingException {
        displayName = URLDecoder.decode(displayName, "UTF-8");
        registryName = URLDecoder.decode(registryName, "UTF-8");
        if (displayName.contains("\\")) {
            displayName = displayName.replace("\\", "\\\\");
        }
        if (registryName.contains("\\")) {
            registryName = registryName.replace("\\", "\\\\");
        }
        if (displayName.contains("%")) {
            displayName = displayName.replace("%", "\\%");
        }
        if (registryName.contains("%")) {
            registryName = registryName.replace("%", "\\%");
        }
        return processMapper.searchProcess(displayName, registryName, User_Idx);
    }

    /* 해당 종류의 프로세스 Registry Item 목록을 가져온다. */
    @Transactional(readOnly = true)
    public List<ProcessVO> findRegistryItem(String processType, String idx) {
        List<ProcessVO> result = new ArrayList<>();
        List<ProcessVO> list = findProcessByType(processType, idx);
        for (ProcessVO process : list) {
            result.add(process);
        }
        return result;
    }


    public List<ProcessVO> findProcessAll(String User_Idx) {
        return processMapper.selectProcessAll(User_Idx);
    }


    public void updateProcess(ProcessVO processVO) throws UnsupportedEncodingException {
        processMapper.processUpdate(processVO);
    }


    public void deleteProcess(ProcessVO processVO) throws UnsupportedEncodingException {
        processMapper.processDelete(processVO);
    }


    @Transactional
    public List<ProcessVO> findUnApprovedProcessList(String idx) {
        return Optional.ofNullable(findRegistryItem("unApproved", idx))
                .orElseGet(ArrayList::new);
    }


    @Transactional
    public int insertProcessDirect(ProcessVO processVO) throws UnsupportedEncodingException {
        return processMapper.insertProcess(processVO);
    }

    @Transactional
    public List<ProcessVO> insertUnApprovedProcess(ProcessVO processVO) throws UnsupportedEncodingException {
        processMapper.insertUnApprovedProcess(processVO);
        return processMapper.selectProcessList("unApproved",processVO.getDepartmentIdx());
    }

    @Transactional
    public List<ProcessVO> insertRequiredProcess(ProcessVO processVO) throws UnsupportedEncodingException {
        processMapper.insertRequiredProcess(processVO);
        return processMapper.selectProcessList("required", processVO.getDepartmentIdx());
    }


    @Transactional
    public void modifyToRequiredProcess(Long idx) {
        processMapper.modifyToRequiredProcess(idx);
    }

    @Transactional
    public void modifyToUnApprovedProcess(Long idx) {
        processMapper.modifyToUnApprovedProcess(idx);
    }

    @Transactional
    public List<ProcessVO> findRequiredProcessList(String idx) {
        return Optional.ofNullable(findRegistryItem("required", idx))
                .orElseGet(ArrayList::new);
    }
}
