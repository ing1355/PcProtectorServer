package oms.pc_protector.restApi.log.service;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.restApi.log.mapper.LogMapper;
import oms.pc_protector.restApi.log.model.LogRequestVO;
import oms.pc_protector.restApi.log.model.LogVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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
