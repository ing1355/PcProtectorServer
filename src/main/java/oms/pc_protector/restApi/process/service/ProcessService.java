package oms.pc_protector.restApi.process.service;

import oms.pc_protector.restApi.process.mapper.ProcessMapper;
import lombok.extern.slf4j.Slf4j;
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
    public int insertProcess(List<ProcessVO> processVOList) throws UnsupportedEncodingException {
        int resultNum = 0;
        if (processVOList == null) return resultNum;
        for (ProcessVO processVO : processVOList) {
            processVO.setDisplayName(URLDecoder.decode(processVO.getDisplayName(), "UTF-8"));
            processVO.setRegistryItem(URLDecoder.decode(processVO.getRegistryItem(), "UTF-8"));
            if (processVO.getDisplayName().contains("%")) {
                processVO.setDisplayName(processVO.getDisplayName().replace("%", "\\%"));
            }
            if (processVO.getRegistryItem().contains("%")) {
                processVO.setRegistryItem(processVO.getRegistryItem().replace("%", "\\%"));
            }
            if (processMapper.existProcess(processVO) == 0) {
                resultNum += processMapper.insertProcess((ProcessVO) processVO);
            }
        }

        return resultNum;
    }


    /* 해당 종류의 프로세스 모든 정보 목록을 가져온다.*/
    @Transactional(readOnly = true)
    public List<ProcessVO> findProcessByType(String processType) {
        return processMapper.selectProcessList(processType);
    }

    @Transactional(readOnly = true)
    public int existProcess(ProcessVO processVO) throws UnsupportedEncodingException {
        processVO.setDisplayName(URLDecoder.decode(processVO.getDisplayName(), "UTF-8"));
        processVO.setRegistryItem(URLDecoder.decode(processVO.getRegistryItem(), "UTF-8"));
        if (processVO.getDisplayName().contains("%")) {
            processVO.setDisplayName(processVO.getDisplayName().replace("%", "\\%"));
        }
        if (processVO.getRegistryItem().contains("%")) {
            processVO.setRegistryItem(processVO.getRegistryItem().replace("%", "\\%"));
        }
        return processMapper.existProcess(processVO);
    }

    @Transactional(readOnly = true)
    public List<ProcessVO> searchProcess(String displayName, String registryName) throws UnsupportedEncodingException {
        displayName = URLDecoder.decode(displayName, "UTF-8");
        registryName = URLDecoder.decode(registryName, "UTF-8");
        if (displayName.contains("%")) {
            displayName = displayName.replace("%", "\\%");
        }
        log.info(displayName);
        if (registryName.contains("%")) {
            registryName = registryName.replace("%", "\\%");
        }
        return processMapper.searchProcess(displayName, registryName);
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


    public void updateProcess(ProcessVO processVO) throws UnsupportedEncodingException {
        processVO.setDisplayName(URLDecoder.decode(processVO.getDisplayName(), "UTF-8"));
        processVO.setRegistryItem(URLDecoder.decode(processVO.getRegistryItem(), "UTF-8"));
        if (processVO.getDisplayName().contains("%")) {
            processVO.setDisplayName(processVO.getDisplayName().replace("%", "\\%"));
        }
        if (processVO.getRegistryItem().contains("%")) {
            processVO.setRegistryItem(processVO.getRegistryItem().replace("%", "\\%"));
        }
        processMapper.processUpdate(processVO);
    }


    public void deleteProcess(ProcessVO processVO) throws UnsupportedEncodingException {
        processVO.setDisplayName(URLDecoder.decode(processVO.getDisplayName(), "UTF-8"));
        processVO.setRegistryItem(URLDecoder.decode(processVO.getRegistryItem(), "UTF-8"));
        if (processVO.getDisplayName().contains("%")) {
            processVO.setDisplayName(processVO.getDisplayName().replace("%", "\\%"));
        }
        if (processVO.getRegistryItem().contains("%")) {
            processVO.setRegistryItem(processVO.getRegistryItem().replace("%", "\\%"));
        }
        processMapper.processDelete(processVO);
    }


    @Transactional
    public List<ProcessVO> findUnApprovedProcessList() {
        return Optional.ofNullable(findRegistryItem("unApproved"))
                .orElseGet(ArrayList::new);
    }


    @Transactional
    public int insertProcessDirect(ProcessVO processVO) throws UnsupportedEncodingException {
        processVO.setDisplayName(URLDecoder.decode(processVO.getDisplayName(), "UTF-8"));
        processVO.setRegistryItem(URLDecoder.decode(processVO.getRegistryItem(), "UTF-8"));
        if (processVO.getDisplayName().contains("%")) {
            processVO.setDisplayName(processVO.getDisplayName().replace("%", "\\%"));
        }
        if (processVO.getRegistryItem().contains("%")) {
            processVO.setRegistryItem(processVO.getRegistryItem().replace("%", "\\%"));
        }
        return processMapper.insertProcess(processVO);
    }

    @Transactional
    public List<ProcessVO> insertUnApprovedProcess(ProcessVO processVO) throws UnsupportedEncodingException {
        processVO.setDisplayName(URLDecoder.decode(processVO.getDisplayName(), "UTF-8"));
        processVO.setRegistryItem(URLDecoder.decode(processVO.getRegistryItem(), "UTF-8"));
        if (processVO.getDisplayName().contains("%")) {
            processVO.setDisplayName(processVO.getDisplayName().replace("%", "\\%"));
        }
        if (processVO.getRegistryItem().contains("%")) {
            processVO.setRegistryItem(processVO.getRegistryItem().replace("%", "\\%"));
        }
        processMapper.insertUnApprovedProcess(processVO);
        return processMapper.selectProcessList("unApproved");
    }

    @Transactional
    public List<ProcessVO> insertRequiredProcess(ProcessVO processVO) throws UnsupportedEncodingException {
        processVO.setDisplayName(URLDecoder.decode(processVO.getDisplayName(), "UTF-8"));
        processVO.setRegistryItem(URLDecoder.decode(processVO.getRegistryItem(), "UTF-8"));
        if (processVO.getDisplayName().contains("%")) {
            processVO.setDisplayName(processVO.getDisplayName().replace("%", "\\%"));
        }
        if (processVO.getRegistryItem().contains("%")) {
            processVO.setRegistryItem(processVO.getRegistryItem().replace("%", "\\%"));
        }
        processMapper.insertRequiredProcess(processVO);
        return processMapper.selectProcessList("required");
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
    public List<ProcessVO> findRequiredProcessList() {
        return Optional.ofNullable(findRegistryItem("required"))
                .orElseGet(ArrayList::new);
    }
}
