package oms.pc_protector.restApi.result.controller;

import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.result.model.ResponseResultVO;
import oms.pc_protector.restApi.result.model.SearchInputVO;
import oms.pc_protector.restApi.result.service.ResultService;
import oms.pc_protector.restApi.user.model.UserRequestVO;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.user.model.UserVO;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping("/v1/results")  // 진단상세조회 API
public class ResultController {

    private final ResponseService responseService;
    private final ResultService resultService;

    public ResultController(ResponseService responseService, ResultService resultService) {
        this.responseService = responseService;
        this.resultService = resultService;
    }


    // 모든 점검결과를 가져온다.
    @GetMapping(value = "")
    public SingleResult<?> findAllResult() {
        HashMap<String, Object> map = new HashMap<>();
        List<?> list = resultService.findAllResult();
        return responseService.getSingleResult(list);
    }


    // 해당 점검결과의 세부사항을 가져온다.
    @GetMapping(value = "/details")
    public SingleResult<?> findResultsDetailsByUserName(
            @RequestParam(value = "ip") String ipAddress,
            @RequestParam(value = "checkTime") String checkTime) {
        HashMap<String, Object> map = new HashMap<>();
        HashMap itemMap = Optional
                .ofNullable(resultService.findDetailsWithProcessListByUserId(ipAddress, checkTime))
                .orElseThrow(() -> new RuntimeException("파라미터를 확인해주세요."));
        map.put("itemDetails", itemMap);
        return responseService.getSingleResult(map);
    }


    // 조건 검색하여 점검결과를 가져온다.
    @GetMapping(value = "/search")
    public SingleResult<?> findByUserIdWithIpAddress(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "ip", required = false) String ipAddress,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "departmentCode", required = false) Long departmentCode) {
        SearchInputVO searchInputVO = new SearchInputVO();
        searchInputVO.setUserId(id);
        searchInputVO.setName(name);
        searchInputVO.setIpAddress(ipAddress);
        searchInputVO.setStartDate(startDate);
        searchInputVO.setEndDate(endDate);
        searchInputVO.setDepartmentCode(departmentCode);
        List<?> list = resultService.findBySearchInput(searchInputVO);
        return responseService.getSingleResult(list);
    }


}
