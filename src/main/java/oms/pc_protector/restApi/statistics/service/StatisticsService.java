package oms.pc_protector.restApi.statistics.service;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.service.DepartmentService;
import oms.pc_protector.restApi.statistics.mapper.StatisticsMapper;
import oms.pc_protector.restApi.statistics.model.StatisticsVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

@Log4j2
@Service
public class StatisticsService {

    /*
        1. 모든 부서 목록을 가지고 온다.
        2. 부서에 해당하는 아이디 목록을 가지고 온다.
        3. 아이디에 해당하는 점검 결과를 반환한다.
     */

    private final DepartmentService departmentService;

    private final StatisticsMapper statisticsMapper;


    public StatisticsService(DepartmentService departmentService, StatisticsMapper statisticsMapper) {
        this.departmentService = departmentService;
        this.statisticsMapper = statisticsMapper;
    }

    @Transactional
    public List<Object> findAll() {
        List<Object> departmentResultMap = new ArrayList<>();
        List<DepartmentVO> departmentList = departmentService.findAll();

        for (DepartmentVO department : departmentList) {

            LinkedHashMap<String, Object> objectMap = new LinkedHashMap<>();

            List<LinkedHashMap> statisticsList = statisticsMapper
                    .selectStatisticsByDepartment(department.getName());

            int totalPc = statisticsList.size();
            int runPc = totalPc;

            int[] safeScore = new int[16];
            int[] safeScoreAll = new int[16];
            int[] safeScoreRun = new int[16];

            for (LinkedHashMap statistics : statisticsList) {
                Object[] array = statistics.values().toArray();
                for (int i = 0; i < safeScore.length; i++) {

                    if (array[i].equals(0)) {
                        runPc--;
                        break;
                    }
                    if (array[i].equals(1)) safeScore[i]++;
                }
            }

            log.info("전체 PC 수 : " + totalPc);
            log.info("실행 PC 수 : " + runPc);

            for (int i = 0; i < safeScore.length; i++) {

                safeScoreAll[i] = (int) Math.round(
                        ((int) safeScore[i] / (double) totalPc) * 100);

                safeScoreRun[i] = runPc == 0 ? 0 : (int) Math.round(
                        ((int) safeScore[i] / (double) runPc) * 100);
            }

            objectMap.put("departmentName", department.getName());  // 부서 이름
            objectMap.put("safePc", safeScore);                     // 안전 PC
            objectMap.put("safePcAll", safeScoreAll);               // 안전 PC 전체
            objectMap.put("safePcRun", safeScoreRun);               // 안전 PC 실행

            departmentResultMap.add(objectMap);

        }
        return departmentResultMap;
    }


}
