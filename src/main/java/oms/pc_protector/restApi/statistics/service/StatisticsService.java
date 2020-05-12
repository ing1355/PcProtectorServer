package oms.pc_protector.restApi.statistics.service;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.client.service.ClientService;
import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.service.DepartmentService;
import oms.pc_protector.restApi.statistics.mapper.StatisticsMapper;
import oms.pc_protector.restApi.statistics.model.CountPcVO;
import oms.pc_protector.restApi.statistics.model.ResponseVO;
import oms.pc_protector.restApi.statistics.model.StatisticsVO;
import oms.pc_protector.restApi.user.model.UserVO;
import oms.pc_protector.restApi.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;

import java.time.LocalDate;
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

    public List<HashMap<String, Object>> findAllByYearMonthOrDepartment(String yearMonth, String department) {
        double beforeTime = System.currentTimeMillis();
        List<HashMap<String, Object>> departmentResultMap = new ArrayList<>();
        List<DepartmentVO> departmentList = new ArrayList<>();

        if (department == null) {
            departmentList = departmentService.findAll();
        } else {
            int parentCode = departmentService.findByDepartment(department).getCode();
            departmentList.add(departmentService.findByDepartmentCode(parentCode));
            departmentList.addAll(departmentService.findChildDescByParentCode(parentCode));
        }

        HashMap<Integer, Object> memoization = new HashMap<>();

        for (DepartmentVO departmentVO : departmentList) {
            List<Integer> childCodeList = new ArrayList<>();
            List<DepartmentVO> childCode = departmentService.findChildDescByParentCode(departmentVO.getCode());
            List<CountPcVO> childResultTemp = new ArrayList<>();
            int parentCode = departmentVO.getCode();

            // 메모이제이션에 이미 등록되어 있다면 패스.
            if (memoization.containsKey(departmentVO.getCode())) {
                childResultTemp.add((CountPcVO) memoization.get(departmentVO.getCode()));
                continue;
            }

            for (DepartmentVO departmentTemp : childCode) {
                childCodeList.add(departmentTemp.getCode());
            }

            List<Integer> parentWithChild = new ArrayList<>();

            parentWithChild.addAll(childCodeList);
            parentWithChild.add(parentCode);


            for (int departmentCode : parentWithChild) {
                String departmentName = departmentService.findByDepartmentCode(departmentCode).getName();
                List<LinkedHashMap> statisticsList = statisticsMapper
                        .selectStatisticsByDepartment(new ResponseVO(departmentCode, yearMonth));

                int totalPc = 0;
                int runPc = statisticsList.size();
                List<UserVO> userList = userService.findByDepartmentCode(departmentCode);

                for (UserVO user : userList) {
                    totalPc += clientService.findById(user.getUserId()).size();
                }

                int avgScore = 0;
                int[] safePc = new int[16];
                int[] safePcDivideAllPc = new int[16];
                int[] safePcDivideRunPc = new int[16];

                for (LinkedHashMap statistics : statisticsList) {
                    Object[] array = statistics.values().toArray();
                    avgScore += (Integer) array[0];
                    for (int i = 1; i < safePc.length; i++)
                        if (array[i].equals(1)) safePc[i]++;
                }

                if (runPc > 0) avgScore = avgScore / runPc;

                // 부모 코드라면?
                if (departmentCode == parentCode) {
                    for (CountPcVO result : childResultTemp) {
                        totalPc += result.getTotalPc();
                        runPc += result.getRunPc();
                        avgScore += result.getAvgScore();
                        for (int i = 0; i < 16; i++) {
                            int[] temp = result.getSafePc();
                            safePc[i] += temp[i];
                        }
                    }
                    avgScore = avgScore / (childResultTemp.size() + 1);
                    safePcDivideAllPc = allPcCalculator(safePc, safePcDivideAllPc, totalPc);
                    safePcDivideRunPc = runPcCalculator(safePc, safePcDivideRunPc, runPc);
                }

                CountPcVO countPcVO = new CountPcVO();
                countPcVO.setTotalPc(totalPc);
                countPcVO.setRunPc(runPc);
                countPcVO.setAvgScore(avgScore);
                countPcVO.setSafePc(safePc);
                countPcVO.setSafePcDivideRunPc(safePcDivideRunPc);
                countPcVO.setSafePcDivideAllPc(safePcDivideRunPc);

                // 자식 코드라면?
                if (departmentCode != parentCode) {
                    safePcDivideAllPc = allPcCalculator(safePc, safePcDivideAllPc, totalPc);
                    safePcDivideRunPc = runPcCalculator(safePc, safePcDivideRunPc, runPc);
                    childResultTemp.add(countPcVO);
                }

                LinkedHashMap<String, Object> objectMap = new LinkedHashMap<>();
                objectMap.put("departmentName", departmentName);             // 부서 이름
                objectMap.put("departmentCode", departmentCode);             // 부서 코드
                objectMap.put("totalPc", totalPc);                           // 전체 PC
                objectMap.put("runPc", runPc);                               // 실행 PC
                objectMap.put("avgScore", avgScore);                         // 평균 점수
                objectMap.put("safePc", safePc);                             // 안전 PC
                objectMap.put("safePcAll", safePcDivideAllPc);               // 안전 PC / 전체 PC
                objectMap.put("safePcRun", safePcDivideRunPc);               // 안전 PC / 실행 PC

                // 메모이제이션 등록
                memoization.put(departmentCode, countPcVO);
                departmentResultMap.add(objectMap);
            }

        }
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
                Integer score1 = (int) o1.get("departmentCode");
                Integer score2 = (int) o2.get("departmentCode");
                return score1.compareTo(score2);
            }
        });
        return list;
    }



}
