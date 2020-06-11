package oms.pc_protector.restApi.statistics.service;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.client.service.ClientService;
import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.service.DepartmentService;
import oms.pc_protector.restApi.statistics.mapper.StatisticsMapper;
import oms.pc_protector.restApi.statistics.model.ResultStatisticsVO;
import oms.pc_protector.restApi.statistics.model.RunPcAndScoreVO;
import oms.pc_protector.restApi.statistics.model.StatisticsResponseVO;
import oms.pc_protector.restApi.statistics.model.StatisticsVO;
import oms.pc_protector.restApi.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/* 점검결과통계 서비스 클래스 */

@Log4j2
@Service
public class StatisticsService {

    private final DepartmentService departmentService;

    private final StatisticsMapper statisticsMapper;

    private final UserService userService;

    private final ClientService clientService;

    public StatisticsService(DepartmentService departmentService,
                             StatisticsMapper statisticsMapper,
                             UserService userService,
                             ClientService clientService) {
        this.departmentService = departmentService;
        this.statisticsMapper = statisticsMapper;
        this.userService = userService;
        this.clientService = clientService;
    }


    @Transactional(readOnly = true)
    public List<HashMap<String, Object>> findAllByYearMonthOrDepartment(String yearMonth, String department) {
        double beforeTime = System.currentTimeMillis();
        List<HashMap<String, Object>> departmentResultMap = new ArrayList<>();
        List<DepartmentVO> departmentList = new ArrayList<>();

        if (department == null) {
            departmentList = departmentService.findAll();
        } else {
            Long parentCode = departmentService.findByDepartment(department).getCode();
            departmentList.add(departmentService.findByDepartmentCode(parentCode));
            departmentList.addAll(departmentService.findChildDescByParentCode(parentCode));
        }


        for (DepartmentVO departmentVO : departmentList) {
            LinkedHashMap<String, Object> objectMap = new LinkedHashMap<>();
            StatisticsResponseVO statisticsResponseVO = new StatisticsResponseVO(yearMonth, departmentVO.getCode());
            int TotalPc = statisticsMapper.countClientByMonth(statisticsResponseVO);
            RunPcAndScoreVO runPcAndScoreVO = statisticsMapper.countRunPcByMonth(statisticsResponseVO);
            List<StatisticsVO> statisticsVO = statisticsMapper.selectItemsByMonth(statisticsResponseVO);
            int[] safePc = new int[16];
            int[] safePcDivideAllPc = new int[16];
            int[] safePcDivideRunPc = new int[16];
            for (StatisticsVO statisticsVO1 : statisticsVO) {
                if (statisticsVO1.getItem1Result() == 1)
                    safePc[0] += 1;
                if (statisticsVO1.getItem2Result() == 1)
                    safePc[1] += 1;
                if (statisticsVO1.getItem3Result() == 1)
                    safePc[2] += 1;
                if (statisticsVO1.getItem4Result() == 1)
                    safePc[3] += 1;
                if (statisticsVO1.getItem5Result() == 1)
                    safePc[4] += 1;
                if (statisticsVO1.getItem6Result() == 1)
                    safePc[5] += 1;
                if (statisticsVO1.getItem7Result() == 1)
                    safePc[6] += 1;
                if (statisticsVO1.getItem8Result() == 1)
                    safePc[7] += 1;
                if (statisticsVO1.getItem9Result() == 1)
                    safePc[8] += 1;
                if (statisticsVO1.getItem10Result() == 1)
                    safePc[9] += 1;
                if (statisticsVO1.getItem11Result() == 1)
                    safePc[10] += 1;
                if (statisticsVO1.getItem12Result() == 1)
                    safePc[11] += 1;
                if (statisticsVO1.getItem13Result() == 1)
                    safePc[12] += 1;
                if (statisticsVO1.getItem14Result() == 1)
                    safePc[13] += 1;
                if (statisticsVO1.getItem15Result() == 1)
                    safePc[14] += 1;
                if (statisticsVO1.getItem16Result() == 1)
                    safePc[15] += 1;
            }
            for (int i = 0; i < 16; i++) {
                if(TotalPc != 0)
                    safePcDivideAllPc[i] = (int)((safePc[i] / (double)TotalPc) * 100);
                if(runPcAndScoreVO.getRunPc() != 0)
                    safePcDivideRunPc[i] = (int)(( safePc[i] / (double)runPcAndScoreVO.getRunPc() ) * 100);
            }
            objectMap.put("departmentName", departmentVO.getName());
            objectMap.put("departmentCode", departmentVO.getCode());
            objectMap.put("totalPc", TotalPc);
            objectMap.put("runPc", runPcAndScoreVO.getRunPc());
            objectMap.put("avgScore", runPcAndScoreVO.getScore());
            objectMap.put("safePc", safePc);
            objectMap.put("safePcAll", safePcDivideAllPc);
            objectMap.put("safePcRun", safePcDivideRunPc);

            departmentResultMap.add(objectMap);
        }

//        HashMap<Long, Object> memoization = new HashMap<>();
//
//        for (DepartmentVO departmentVO : departmentList) {
//            log.info("부서 이름1 : {}", departmentVO.getName());
//            List<Long> childCodeList = new ArrayList<>();
//            List<DepartmentVO> childCode = departmentService.findChildDescByParentCode(departmentVO.getCode());
//            List<ResultStatisticsVO> childResultTemp = new ArrayList<>();
//            Long parentCode = departmentVO.getCode();
//
//            // 메모이제이션에 이미 등록되어 있다면 패스.
//            if (memoization.containsKey(departmentVO.getCode())) {
//                log.info("메모이제이션, 이미 저장된 부서 코드 : {}", departmentVO.getCode());
//                childResultTemp.add((ResultStatisticsVO) memoization.get(departmentVO.getCode()));
//                continue;
//            }
//
//            for (DepartmentVO departmentTemp : childCode) {
//                childCodeList.add(departmentTemp.getCode());
//            }
//
//            List<Long> parentWithChild = new ArrayList<>();
//
//            parentWithChild.addAll(childCodeList);
//            parentWithChild.add(parentCode);
//
//
//            for (Long departmentCode : parentWithChild) {
//                log.info("부서이름2 : {}", departmentService.findByDepartmentCode(departmentCode).getName());
//                log.info("부서코드2 : {}", departmentCode);
//
//                String departmentName = departmentService.findByDepartmentCode(departmentCode).getName();
//                Long myParentCode = departmentService.findByDepartmentCode(departmentCode).getParentCode();
//
//                List<LinkedHashMap> statisticsList = statisticsMapper.selectStatisticsByDepartment(new StatisticsResponseVO(departmentCode, yearMonth));
//                List<ClientVO> clientVO = clientService.findAll();
//
//
//                int totalPc = statisticsMapper.countClientByMonth(
//                        new StatisticsResponseVO(departmentCode, yearMonth));
//                int runPc = statisticsList.size();
//
////                List<UserVO> userList = userService.findByDepartmentCode(departmentCode);
//
////                for (UserVO user : userList) {
////                    totalPc += clientService.findById(user.getUserId()).size();
////                }
//
//                int sumScore = 0;
//                int avgScore = 0;
//                int[] safePc = new int[16];
//                int[] safePcDivideAllPc = new int[16];
//                int[] safePcDivideRunPc = new int[16];
//
//                for (LinkedHashMap statistics : statisticsList) {
//                    Object[] array = statistics.values().toArray();
//                    sumScore += (Integer) array[0];
//                    for (int i = 0; i < safePc.length; i++)
//                        if (array[i + 1].equals(1)) safePc[i]++;
//                }
//
//                // 임시저장한 하위부서 중 직속 하위부서가 있는지 확인한다.
//                boolean isExistChild = false;
//                for (ResultStatisticsVO countPcVO : childResultTemp) {
//                    if (countPcVO.getParentCode().equals(departmentCode)) {
//                        isExistChild = true;
//                        break;
//                    }
//                }
//
//                // 하위부서가 있다면?
//                if (isExistChild) {
//                    for (ResultStatisticsVO result : childResultTemp) {
//                        if (departmentCode.equals(result.getParentCode())) {
//                            totalPc += result.getTotalPc();
//                            runPc += result.getRunPc();
//                            sumScore += result.getSumScore();
//
//                            for (int i = 0; i < 16; i++) {
//                                int[] temp = result.getSafePc();
//                                safePc[i] += temp[i];
//                            }
//                        }
//                    }
//                    safePcDivideAllPc = allPcCalculator(safePc, safePcDivideAllPc, totalPc);
//                    safePcDivideRunPc = runPcCalculator(safePc, safePcDivideRunPc, runPc);
//                }
//
//                if (runPc > 0) avgScore = sumScore / runPc;
//
//                ResultStatisticsVO resultStatisticsVO = new ResultStatisticsVO();
//                resultStatisticsVO.setParentCode(myParentCode);
//                resultStatisticsVO.setTotalPc(totalPc);
//                resultStatisticsVO.setRunPc(runPc);
//                resultStatisticsVO.setSumScore(sumScore);
//                resultStatisticsVO.setAvgScore(avgScore);
//                resultStatisticsVO.setSafePc(safePc);
//                resultStatisticsVO.setSafePcDivideRunPc(safePcDivideRunPc);
//                resultStatisticsVO.setSafePcDivideAllPc(safePcDivideRunPc);
//
//                // 하위부서가 없다면?
//                if (!isExistChild) {
//                    safePcDivideAllPc = allPcCalculator(safePc, safePcDivideAllPc, totalPc);
//                    safePcDivideRunPc = runPcCalculator(safePc, safePcDivideRunPc, runPc);
//                }
//
//                // 처리된 부서는 임시 저장.
//                childResultTemp.add(resultStatisticsVO);
//
//                LinkedHashMap<String, Object> objectMap = new LinkedHashMap<>();
//                objectMap.put("departmentName", departmentName);                    // 부서 이름
//                objectMap.put("departmentCode", departmentCode);                    // 부서 코드
//                objectMap.put("totalPc", totalPc);                                  // 전체 PC
//                objectMap.put("runPc", runPc);                                      // 실행 PC
//                objectMap.put("avgScore", avgScore);                                // 평균 점수
//                objectMap.put("safePc", safePc);                                    // 안전 PC
//                objectMap.put("safePcAll", safePcDivideAllPc);                      // 안전 PC / 전체 PC
//                objectMap.put("safePcRun", safePcDivideRunPc);                      // 안전 PC / 실행 PC
//
//                // 메모이제이션 등록
//                memoization.put(departmentCode, resultStatisticsVO);
//                departmentResultMap.add(objectMap);
//            }
//
//        }
        double afterTime = System.currentTimeMillis();
        double secDiffTime = (afterTime - beforeTime) / 1000;
        log.info("걸린시간 : " + secDiffTime + "초");
        return objectSort(departmentResultMap);
    }


    public int[] allPcCalculator(int[] safePc, int[] safePcDivideAllPc, int totalPc) {
        for (int i = 0; i < safePc.length; i++) {
            safePcDivideAllPc[i] = (int) Math.round(
                    ((int) safePc[i] / (double) totalPc) * 100);
        }
        return safePcDivideAllPc;
    }


    public int[] runPcCalculator(int[] safePc, int[] safePcDivideRunPc, int runPc) {
        for (int i = 0; i < safePc.length; i++) {
            safePcDivideRunPc[i] = runPc == 0 ? 0 : (int) Math.round(
                    ((int) safePc[i] / (double) runPc) * 100);
        }
        return safePcDivideRunPc;
    }


    public List<HashMap<String, Object>> objectSort(List<HashMap<String, Object>> list) {
        Collections.sort(list, new Comparator<HashMap<String, Object>>() {
            @Override
            public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                Long score1 = (Long) o1.get("departmentCode");
                Long score2 = (Long) o2.get("departmentCode");
                return score1.compareTo(score2);
            }
        });
        return list;
    }

//    public
}
