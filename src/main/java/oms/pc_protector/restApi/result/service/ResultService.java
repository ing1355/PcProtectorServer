package oms.pc_protector.restApi.result.service;

import oms.pc_protector.restApi.client.mapper.ClientMapper;
import oms.pc_protector.restApi.client.model.ClientVO;
import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.service.DepartmentService;
import oms.pc_protector.restApi.policy.mapper.ConfigurationMapper;
import oms.pc_protector.restApi.policy.model.NowScheduleVO;
import oms.pc_protector.restApi.policy.model.PeriodDateVO;
import oms.pc_protector.restApi.policy.service.ConfigurationService;
import oms.pc_protector.restApi.result.model.*;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.result.model.ResultVO;
import oms.pc_protector.restApi.result.mapper.ResultMapper;
import oms.pc_protector.restApi.user.model.UserRequestVO;
import oms.pc_protector.restApi.user.model.UserVO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.*;

@Log4j2
@Service
public class ResultService {

    private final ResultMapper resultMapper;
    private final DepartmentService departmentService;
    private final ConfigurationMapper configurationMapper;
    private final ClientMapper clientMapper;
    private final ConfigurationService configurationService;

    public ResultService(ResultMapper resultMapper,
                         DepartmentService departmentService,
                         ConfigurationMapper configurationMapper,
                         ClientMapper clientMapper,
                         ConfigurationService configurationService) {
        this.resultMapper = resultMapper;
        this.configurationMapper = configurationMapper;
        this.departmentService = departmentService;
        this.clientMapper = clientMapper;
        this.configurationService = configurationService;
    }

    @Transactional
    public List<ResponseResultVO> findAllResult() {
        return Optional.ofNullable(resultMapper.selectResultAll())
                .orElse(new ArrayList<>());
    }


    /* 사용자의 검색 파라미터에 해당하는 점검결과를 반환한다. */
    @Transactional
    public List<ResponseResultVO> findBySearchInput(SearchInputVO searchInputVO) {
        List<ResponseResultVO> resultList = new ArrayList<>();
        if (searchInputVO.getDepartmentCode() != null) {
            Long code = searchInputVO.getDepartmentCode();
            List<DepartmentVO> childCodeList = new ArrayList<>();
            childCodeList.add(departmentService.findByDepartmentCode(code));
            childCodeList.addAll(departmentService.findChildAscByParentCode(code));
            for (DepartmentVO childCode : childCodeList) {
                searchInputVO.setDepartmentCode(childCode.getCode());
                resultList.addAll(resultMapper.selectBySearchInput(searchInputVO));
            }
            return resultList;
        }
        resultList.addAll(resultMapper.selectBySearchInput(searchInputVO));
        return resultList;
    }


    /* 사용자 아이디에 해당하는 점검결과의 세부사항을 반환한다. */

    @Transactional
    public List<ResultVO> findUserDetailStaticInfo(String id) {
        List<ResultVO> list = resultMapper.findUserDetailStaticInfo(id);

        return list;
    }

    @Transactional
    public ResultVO findDetailsByUserId(String ipAddress, String checkTime) {
        return Optional.ofNullable(resultMapper.selectResultDetailsById(ipAddress, checkTime))
                .orElse(ResultVO.builder().build());
    }


    /*@Transactional(readOnly = true)
    public List<ResponseResultVO> findByDepartmentHierarchy(String department) {
        int parentCode = departmentService.findByDepartment(department).getCode();
        List<ResponseResultVO> result = new ArrayList<>();
        List<DepartmentVO> departmentList = departmentService.findChildAscByParentCode(parentCode);
        SearchInputVO searchInputVO = new SearchInputVO();
        searchInputVO.setDepartment(department);
        // 선택된 부서 결과 먼저 리스트에 넣는다.
        result = findByUserIdWithIpAddress(searchInputVO);
        // 하위 부서 결과를 리스트에 넣는다.
        for (DepartmentVO departmentVO : departmentList) {
            searchInputVO.setDepartment(departmentVO.getName());
            result.addAll(findByUserIdWithIpAddress(searchInputVO));
        }
        return result;
    }*/


    /* 월별 점검결과 수를 반환한다. */
    @Transactional
    public int countByMonth(String month) {
        return resultMapper.selectCountRunByMonth(month);
    }

    @Transactional
    public List<Integer> findScoreByDepartmentWithMonth(String department, String month) {
        return resultMapper.selectScoreByDepartmentWithMonth(department, month);
    }


    /* 해당 아이디의 점검결과의 취약 프로세스 목록을 반환한다. */
    @Transactional
    public HashMap<String, Object> findResultProcessByUserId(String ipAddress, String checkTime) {
        List<ResultProcessVO> resultProcess = Optional
                .ofNullable(resultMapper.selectResultProcessById(ipAddress, checkTime))
                .orElse(new ArrayList<ResultProcessVO>());

        ArrayList<String> item3Process = new ArrayList<>();
        ArrayList<String> item4Process = new ArrayList<>();
        ArrayList<String> item8Process = new ArrayList<>();
        ArrayList<String> item10Process = new ArrayList<>();
        ArrayList<String> item11Process = new ArrayList<>();
        ArrayList<String> item12Process = new ArrayList<>();
        ArrayList<String> item13Process = new ArrayList<>();
        ArrayList<String> item14Process = new ArrayList<>();
        ArrayList<String> item15Process = new ArrayList<>();

        for (ResultProcessVO process : resultProcess) {
            String type = process.getType();
            switch (type) {
                case "item3":
                    item3Process.add(process.getProcessName());
                    break;
                case "item4":
                    item4Process.add(process.getProcessName());
                    break;
                case "item8":
                    item8Process.add(process.getProcessName());
                    break;
                case "item10":
                    item10Process.add(process.getProcessName());
                    break;
                case "item11":
                    item11Process.add(process.getProcessName());
                    break;
                case "item12":
                    item12Process.add(process.getProcessName());
                    break;
                case "item13":
                    item13Process.add(process.getProcessName());
                    break;
                case "item14":
                    item14Process.add(process.getProcessName());
                    break;
                case "item15":
                    item15Process.add(process.getProcessName());
                    break;
            }

        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("item3", item3Process);
        map.put("item4", item4Process);
        map.put("item8", item8Process);
        map.put("item10", item10Process);
        map.put("item11", item11Process);
        map.put("item12", item12Process);
        map.put("item13", item13Process);
        map.put("item14", item14Process);
        map.put("item15", item15Process);
        return map;
    }


    /* 사용자 아이디에 해당하는 모든 아이템별 세부 점검사항을 반환한다. */
    @Transactional
    public HashMap findDetailsWithProcessListByUserId(String ipAddress, String checkTime) {
        ResultVO resultVO = Optional.ofNullable(findDetailsByUserId(ipAddress, checkTime))
                .orElse(ResultVO.builder().build());

        HashMap<String, Object> processListMap = Optional.ofNullable(findResultProcessByUserId(ipAddress, checkTime))
                .orElse(new HashMap<>());

        HashMap<String, Object> itemBox = new HashMap<>();
        HashMap<String, Object> item1 = new HashMap<>();
        HashMap<String, Object> item2 = new HashMap<>();
        HashMap<String, Object> item3 = new HashMap<>();
        HashMap<String, Object> item4 = new HashMap<>();
        HashMap<String, Object> item5 = new HashMap<>();
        HashMap<String, Object> item6 = new HashMap<>();
        HashMap<String, Object> item7 = new HashMap<>();
        HashMap<String, Object> item8 = new HashMap<>();
        HashMap<String, Object> item9 = new HashMap<>();
        HashMap<String, Object> item10 = new HashMap<>();
        HashMap<String, Object> item11 = new HashMap<>();
        HashMap<String, Object> item12 = new HashMap<>();
        HashMap<String, Object> item13 = new HashMap<>();
        HashMap<String, Object> item14 = new HashMap<>();
        HashMap<String, Object> item15 = new HashMap<>();
        HashMap<String, Object> item16 = new HashMap<>();

        item1.put("result", resultVO.getItem1Result());
        item1.put("installationStatusCheck", resultVO.getItem1InstallationStatusCheck());
        item1.put("executionStatusCheck", resultVO.getItem1ExecutionStatusCheck());

        item2.put("result", resultVO.getItem2Result());
        item2.put("ExecutionStatusCheck", resultVO.getItem2ExecutionStatusCheck());
        item2.put("UpdateStatusCheck", resultVO.getItem2UpdateStatusCheck());

        item3.put("result", resultVO.getItem3Result());
        item3.put("count", resultVO.getItem3Count());
        item3.put("processList", processListMap.get("item3"));

        item4.put("result", resultVO.getItem4Result());
        item4.put("count", resultVO.getItem4Count());
        item4.put("processList", processListMap.get("item4"));

        item5.put("result", resultVO.getItem5Result());
        item5.put("reasonsVulnerability", resultVO.getItem5ReasonsVulnerability());
        item5.put("passwordLength", resultVO.getItem5PasswordLength());
        item5.put("DetailReasons", resultVO.getItem5PasswordLength());

        item6.put("result", resultVO.getItem6Result());
        item6.put("pwLastChangePastDate", resultVO.getItem6PwLastChangePastDate());

        item7.put("result", resultVO.getItem7Result());
        item7.put("executionStatusCheck", resultVO.getItem7Result());
        item7.put("pwUsageStatus", resultVO.getItem7PwUsageStatus());
        item7.put("Period", resultVO.getItem7Period());

        item8.put("result", resultVO.getItem8Result());
        item8.put("processList", processListMap.get("item8"));

        item9.put("result", resultVO.getItem9Result());
        item9.put("processList", processListMap.get("item9"));

        item10.put("result", resultVO.getItem10Result());
        item10.put("processList", processListMap.get("item10"));

        item11.put("result", resultVO.getItem11Result());
        item11.put("processList", processListMap.get("item11"));

        item12.put("result", resultVO.getItem11Result());
        item12.put("processList", processListMap.get("item12"));

        item13.put("result", resultVO.getItem11Result());
        item13.put("processList", processListMap.get("item13"));

        item14.put("result", resultVO.getItem11Result());
        item14.put("processList", processListMap.get("item14"));

        item15.put("result", resultVO.getItem11Result());
        item15.put("processList", processListMap.get("item15"));

        item16.put("result", resultVO.getItem16Result());

        itemBox.put("item1", item1);
        itemBox.put("item2", item2);
        itemBox.put("item3", item3);
        itemBox.put("item4", item4);
        itemBox.put("item5", item5);
        itemBox.put("item6", item6);
        itemBox.put("item7", item7);
        itemBox.put("item8", item8);
        itemBox.put("item9", item9);
        itemBox.put("item10", item10);
        itemBox.put("item11", item11);
        itemBox.put("item12", item12);
        itemBox.put("item13", item13);
        itemBox.put("item14", item14);
        itemBox.put("item15", item15);
        itemBox.put("item16", item16);

        return itemBox;
    }


    /* 점검결과를 등록한다. */
    @Transactional
    public void registrationResult(InspectionResultsVO inspectionResults) {
        HashMap<String, Object> parsedResult = resultParseFromClient(inspectionResults);
        ClientVO clientVO = (ClientVO) parsedResult.get("clientInfo");
        ResultVO resultVO = (ResultVO) parsedResult.get("resultInfo");
        log.info("-------------------------");
        log.info("--------점검결과등록-------");
        log.info("ID : " + clientVO.getUserId());
        log.info("IP_ADDRESS : " + clientVO.getIpAddress());
        log.info("PC_NAME : " + clientVO.getPcName());
        log.info("OS : " + clientVO.getOs());
        log.info("AGENT_VERSION : " + clientVO.getPcProtectorVersion());
        log.info("VACCINE_VERSION : " + clientVO.getVaccineVersion());
        log.info("CHECK_TIME : " + resultVO.getCheckTime());
        log.info("사용자 전체 점수 : " + resultVO.getScore());
        log.info("-------------------------");
        Optional.ofNullable(resultVO).ifPresent(this::resultSet);

        resultProcessSet((HashMap<String, Object>) parsedResult.get("processList"),
                clientVO.getUserId(),
                clientVO.getIpAddress(),
                resultVO.getCheckTime());
    }

    /* 아이템별 결과값을 등록한다. */
    @Transactional
    public void resultSet(ResultVO resultVO) {
        PeriodDateVO temp = configurationMapper.selectAppliedSchedule();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        boolean Miss = resultMapper.selectClientForMiss(resultVO);
        if (!Miss) {
            if (temp.getPeriod() == 1) {
                start.set(Calendar.WEEK_OF_MONTH, temp.getFromWeek());
                start.set(Calendar.DAY_OF_WEEK, temp.getFromDay() + 1);
                end.set(Calendar.WEEK_OF_MONTH, temp.getToWeek());
                end.set(Calendar.DAY_OF_WEEK, temp.getToDay() + 1);
            } else if (temp.getPeriod() == 2) {
                start.set(Calendar.DAY_OF_WEEK, temp.getFromDay() + 1);
                end.set(Calendar.DAY_OF_WEEK, temp.getToDay() + 1);
            }
            resultVO.setStartTime(df.format(start.getTime()));
            resultVO.setEndTime(df.format(end.getTime()));
            if (df.format(start.getTime()).compareTo(df.format(now.getTime())) > 0 ||
                    df.format(end.getTime()).compareTo(df.format(now.getTime())) < 0)
                Optional.ofNullable(resultVO)
                        .ifPresent(resultMapper::insertResult);
            else
                Optional.ofNullable(resultVO)
                        .ifPresent(resultMapper::updateResultClient);
        }
    }


    /* 아이템별 취약 프로세스를 등록한다.*/
    @Transactional
    public void resultProcessSet(
            @NotNull HashMap<String, Object> processMap, String userId,
            String ipAddress, String checkTime) {
        ArrayList<String> processList = new ArrayList<>();
        Set<String> keyList = processMap.keySet();
        for (String key : keyList) {
            processList = (ArrayList<String>) processMap.get(key);
            for (String processName : processList) {
                if (!processName.equals("")) {
                    Optional.ofNullable(processName).ifPresentOrElse(p -> {
                        ResultProcessVO resultProcessVO = ResultProcessVO.builder()
                                .userId(userId)
                                .ipAddress(ipAddress)
                                .processName(p)
                                .type(key)
                                .checkTime(checkTime)
                                .build();
                        Optional.ofNullable(resultProcessVO)
                                .ifPresent(resultMapper::insertResultProcess);
                    }, () -> log.info("해당 프로세스 이름이 NULL 입니다."));
                }
            }
        }
    }


    /* Client에서 받아온 결과를 파싱하여 분류한다. */
    @Transactional
    public HashMap<String, Object> resultParseFromClient(InspectionResultsVO inspectionResults) {
        HashMap<String, Object> map = new HashMap<>();
        InspectionResultsVO inspectionResultsVO = inspectionResults;

        ClientVO clientVO = new ClientVO();
        int score = inspectionResults.getScore();
        Item1 item1 = new Item1();
        Item2 item2 = new Item2();
        Item3 item3 = new Item3();
        Item4 item4 = new Item4();
        Item5 item5 = new Item5();
        Item6 item6 = new Item6();
        Item7 item7 = new Item7();
        Item8 item8 = new Item8();
        Item9 item9 = new Item9();
        Item10 item10 = new Item10();
        Item11 item11 = new Item11();
        Item12 item12 = new Item12();
        Item13 item13 = new Item13();
        Item14 item14 = new Item14();
        Item15 item15 = new Item15();
        Item16 item16 = new Item16();

        clientVO = inspectionResultsVO.getClientVO();
        item1 = inspectionResultsVO.getItem1();
        item2 = inspectionResultsVO.getItem2();
        item3 = inspectionResultsVO.getItem3();
        item4 = inspectionResultsVO.getItem4();
        item5 = inspectionResultsVO.getItem5();
        item6 = inspectionResultsVO.getItem6();
        item7 = inspectionResultsVO.getItem7();
        item8 = inspectionResultsVO.getItem8();
        item9 = inspectionResultsVO.getItem9();
        item10 = inspectionResultsVO.getItem10();
        item11 = inspectionResultsVO.getItem11();
        item12 = inspectionResultsVO.getItem12();
        item13 = inspectionResultsVO.getItem13();
        item14 = inspectionResultsVO.getItem14();
        item15 = inspectionResultsVO.getItem15();
        item16 = inspectionResultsVO.getItem16();

        ResultVO resultVO = ResultVO.builder()
                .userId(clientVO.getUserId())
                .ipAddress(clientVO.getIpAddress())
                .checkTime(clientVO.getCheckTime())
                .score(score)

                .item1Result(Optional.of(item1.getResult())
                        .orElse(0))
                .item2Result(Optional.of(item2.getResult())
                        .orElse(0))
                .item3Result(Optional.of(item3.getResult())
                        .orElse(0))
                .item4Result(Optional.of(item4.getResult())
                        .orElse(0))
                .item5Result(Optional.of(item5.getResult())
                        .orElse(0))
                .item6Result(Optional.of(item6.getResult())
                        .orElse(0))
                .item7Result(Optional.of(item7.getResult())
                        .orElse(0))
                .item8Result(Optional.of(item8.getResult())
                        .orElse(0))
                .item9Result(Optional.of(item9.getResult())
                        .orElse(0))
                .item10Result(Optional.of(item10.getResult())
                        .orElse(0))
                .item11Result(Optional.of(item11.getResult())
                        .orElse(0))
                .item12Result(Optional.of(item12.getResult())
                        .orElse(0))
                .item13Result(Optional.of(item13.getResult())
                        .orElse(0))
                .item14Result(Optional.of(item14.getResult())
                        .orElse(0))
                .item15Result(Optional.of(item15.getResult())
                        .orElse(0))
                .item16Result(Optional.of(item16.getResult())
                        .orElse(0))

                .item3Count(Optional.of(item3.getCount())
                        .orElse(0))
                .item4Count(Optional.of(item4.getCount())
                        .orElse(0))
                .item10Count(Optional.of(item10.getCount())
                        .orElse(0))

                .item1InstallationStatusCheck(Optional
                        .of(item1.getInstallationStatusCheck())
                        .orElse(0))
                .item1ExecutionStatusCheck(Optional
                        .of(item1.getExecutionStatusCheck())
                        .orElse(0))
                .item2ExecutionStatusCheck(Optional
                        .of(item2.getExecutionStatusCheck())
                        .orElse(0))
                .item2UpdateStatusCheck(Optional
                        .of(item2.getUpdateStatusCheck())
                        .orElse(0))
                .item5ReasonsVulnerability(Optional
                        .of(item5.getReasonsVulnerability())
                        .orElse(0))
                .item5PasswordLength(Optional
                        .of(item5.getPasswordLength())
                        .orElse(0))
                .item5DetailReasons(Optional
                        .ofNullable(item5.getDetailReasons())
                        .orElse("취약사항 없음"))
                .item6PwLastChangePastDate(Optional
                        .of(item6.getPwLastChangePastDate())
                        .orElse(0))
                .item7ExecutionStatusCheck(Optional
                        .of(item7.getExecutionStatusCheck())
                        .orElse(0))
                .item7PwUsageStatus(Optional
                        .of(item7.getPwUsageStatus())
                        .orElse(0))
                .item7Period(Optional
                        .of(item7.getPeriod())
                        .orElse(0))
                .build();

        HashMap<String, Object> hashMap = new HashMap<>();
        ArrayList<String> arrayList = new ArrayList<>();

        Optional.ofNullable(item3.getContentList())
                .ifPresent(list -> hashMap.put("item3", list));

        Optional.ofNullable(item4.getContentList())
                .ifPresent(list -> hashMap.put("item4", list));

        Optional.ofNullable(item8.getContentList())
                .ifPresent(list -> hashMap.put("item8", list));

        Optional.ofNullable(item10.getContentList())
                .ifPresent(list -> hashMap.put("item10", list));

        Optional.ofNullable(item11.getContentList())
                .ifPresent(list -> hashMap.put("item11", list));

        Optional.ofNullable(item12.getContentList())
                .ifPresent(list -> hashMap.put("item12", list));

        Optional.ofNullable(item13.getContentList())
                .ifPresent(list -> hashMap.put("item13", list));

        Optional.ofNullable(item14.getContentList())
                .ifPresent(list -> hashMap.put("item14", list));

        Optional.ofNullable(item15.getContentList())
                .ifPresent(list -> hashMap.put("item15", list));

        map.put("clientInfo", clientVO);
        map.put("resultInfo", resultVO);
        map.put("processList", hashMap);
        return map;
    }
}
