package oms.pc_protector.restApi.process.service;

import oms.pc_protector.restApi.process.mapper.ProcessMapper;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.process.model.ProcessVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class ProcessService {

    private final ProcessMapper processMapper;


    public ProcessService(ProcessMapper processMapper) {
        this.processMapper = processMapper;
    }


    /* Client로 받은 프로세스 목록을 등록한다. */
    @Transactional
    public int insertProcess(List<ProcessVO> processVOList) {
        int resultNum = 0;
        if (processVOList == null) return resultNum;
        for (ProcessVO processVO : processVOList)
            resultNum += processMapper.insertProcess((ProcessVO) processVO);
        return resultNum;
    }


    /* 해당 종류의 프로세스 모든 정보 목록을 가져온다.*/
    @Transactional(readOnly = true)
    public List<ProcessVO> findProcessByType(String processType) {
        return processMapper.selectProcessList(processType);
    }


    /* 해당 종류의 프로세스 Registry Item 목록을 가져온다. */
    @Transactional(readOnly = true)
    public List<ProcessVO> findRegistryItem(String processType) {
        List<ProcessVO> result = new ArrayList<>();
        List<ProcessVO> list = findProcessByType(processType);
        for (ProcessVO process : list) {
            result.add(process);
            log.debug("Process Registry Name : " + ((ProcessVO) process).getDisplayName());
        }
        return result;
    }


    public List<ProcessVO> findProcessAll() {
        return processMapper.selectProcessAll();
    }


    public void updateProcess(ProcessVO processVO) {
        processMapper.processUpdate(processVO);
    }


    public void deleteProcess(ProcessVO processVO) {
        processMapper.processDelete(processVO);
    }


    @Transactional
    public List<ProcessVO> findUnApprovedProcessList() {
        return Optional.ofNullable(findRegistryItem("unApproved"))
                .orElseGet(ArrayList::new);
    }


    @Transactional
    public int registerUnApprovedProcessList(ProcessVO processVO){
        List<ProcessVO> unApprovedProcessList = new ArrayList<>();
        for (ProcessVO process : processVO.getProcessVOList()) {
            process.setType("unApproved");
            unApprovedProcessList.add(process);
        }
        return insertProcess(unApprovedProcessList);
    }


    @Transactional
    public List<ProcessVO> findRequiredProcessList() {
        return Optional.ofNullable(findRegistryItem("required"))
                .orElseGet(ArrayList::new);
    }


    @Transactional
    public int registerRequiredProcessList(ProcessVO processVO){
        List<ProcessVO> requiredProcessList = new ArrayList<>();
        for (ProcessVO process : processVO.getProcessVOList()) {
            process.setType("required");
            requiredProcessList.add(process);
        }
        return insertProcess(requiredProcessList);
    }


    @Transactional
    public void modifyProcessList(ProcessVO processVO) {
        for (ProcessVO process : processVO.getProcessVOList()) {
            updateProcess(process);
        }
    }


    @Transactional
    public void removeProcessList(ProcessVO processVO) {
        for (ProcessVO process : processVO.getProcessVOList()) {
            deleteProcess(process);
        }
    }
}
