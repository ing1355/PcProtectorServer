package oms.pc_protector.restApi.log.service;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.log.mapper.LogMapper;
import oms.pc_protector.restApi.log.model.LogRequestVO;
import oms.pc_protector.restApi.log.model.LogVO;
import oms.pc_protector.restApi.manager.model.RequestManagerVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class LogService {

    private final LogMapper logMapper;

    public LogService(LogMapper logMapper) {
        this.logMapper = logMapper;
    }

    @Transactional
    public List<?> getAllLog() {
        return logMapper.getAllLog();
    }

    @Transactional
    public List<?> search(LogRequestVO logRequestVO) {
        return logMapper.search(logRequestVO);
    }

    @Transactional
    public void register(LogVO logVO) {
        logMapper.insert(logVO);
    }
}
