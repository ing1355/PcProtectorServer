package oms.pc_protector.restApi.statistics.service;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.client.service.ClientService;
import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.service.DepartmentService;
import oms.pc_protector.restApi.statistics.mapper.StatisticsMapper;
import oms.pc_protector.restApi.statistics.model.StatisticsVO;
import oms.pc_protector.restApi.user.model.UserVO;
import oms.pc_protector.restApi.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

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

    @Transactional
    public List<Object> findAll(String yearMonth) {
        List<Object> departmentResultMap = new ArrayList<>();
        List<DepartmentVO> departmentList = departmentService.findAll();
        log.info("---------점검결과통계----------");

        for (DepartmentVO department : departmentList) {
            LinkedHashMap<String, Object> objectMap = new LinkedHashMap<>();

            // 부서별 점검결과 목록을 가져온다.
            List<LinkedHashMap> statisticsList = statisticsMapper
                    .selectStatisticsByDepartment(department.getName(), yearMonth);

            // 전체 PC 수
            int totalPc = 0;

            // 실행 PC 수
            int runPc = statisticsList.size();

            // 해당 부서에 해당하는 아이디 목록을 가져온다.
            List<UserVO> userList = userService.findByDepartment(department.getName());

            for (UserVO user : userList) {
                // 해당 아이디의 모든 클라이언트를 가져온다.
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

            if(runPc > 0) avgScore = avgScore / runPc;


            for (int i = 0; i < safePc.length; i++) {
                safePcDivideAllPc[i] = (int) Math.round(
                        ((int) safePc[i] / (double) totalPc) * 100);

                safePcDivideRunPc[i] = runPc == 0 ? 0 : (int) Math.round(
                        ((int) safePc[i] / (double) runPc) * 100);
            }

            objectMap.put("departmentName", department.getName());       // 부서 이름
            objectMap.put("avgScore", avgScore);                         // 평균 점수
            objectMap.put("safePc", safePc);                             // 안전 PC
            objectMap.put("safePcAll", safePcDivideAllPc);               // 안전 PC / 전체 PC
            objectMap.put("safePcRun", safePcDivideRunPc);               // 안전 PC / 실행 PC

            departmentResultMap.add(objectMap);

        }

        return departmentResultMap;
    }


}
