package oms.pc_protector.restApi.result.controller;

import lombok.SneakyThrows;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.result.model.ResponseResultVO;
import oms.pc_protector.restApi.result.model.ResultVO;
import oms.pc_protector.restApi.result.model.SearchInputVO;
import oms.pc_protector.restApi.result.service.ResultService;
import oms.pc_protector.restApi.user.model.UserRequestVO;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.user.model.UserVO;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

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
    @SneakyThrows
    @GetMapping(value = "/search")
    public SingleResult<?> findByUserIdWithIpAddress(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "ip", required = false) String ipAddress,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "departmentCode", required = false) Long departmentCode) {
        SearchInputVO searchInputVO = new SearchInputVO();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = df.parse(startDate);
        Date d2 = df.parse(endDate);
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(d1);
        start.set(Calendar.HOUR_OF_DAY,0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        end.setTime(d2);
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        searchInputVO.setUserId(id);
        searchInputVO.setName(name);
        searchInputVO.setIpAddress(ipAddress);
        searchInputVO.setStartDate(df.format(start.getTime()));
        searchInputVO.setEndDate(df.format(end.getTime()));
        searchInputVO.setDepartmentCode(departmentCode);
        List<?> list = resultService.findBySearchInput(searchInputVO);
        return responseService.getSingleResult(list);
    }


    //사용자 관리 - 상세 보기 전용
    @GetMapping(value = "/userstaticbyid")
    public SingleResult<?> findUserDetailStaticInfo(
            @RequestParam(value = "id") String id) {
        List<ResultVO> list = resultService.findUserDetailStaticInfo(id);
        return responseService.getSingleResult(list);
    }
}
