package oms.pc_protector.restApi.statistics.service;

import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.restApi.client.service.ClientService;
import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.service.DepartmentService;
import oms.pc_protector.restApi.statistics.mapper.StatisticsMapper;
import oms.pc_protector.restApi.statistics.model.RunPcAndScoreVO;
import oms.pc_protector.restApi.statistics.model.StatisticsResponseVO;
import oms.pc_protector.restApi.statistics.model.StatisticsVO;
import oms.pc_protector.restApi.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/* 점검결과통계 서비스 클래스 */

@Slf4j
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
    public List<HashMap<String, Object>> findAllByYearMonthOrDepartment(String yearMonth, String department, String User_Idx) {
        double beforeTime = System.currentTimeMillis();
        List<HashMap<String, Object>> departmentResultMap = new ArrayList<>();
        List<DepartmentVO> departmentList = new ArrayList<>();

        if (department == null) {
            departmentList = departmentService.findAll(User_Idx);
        } else {
            Long parentCode = departmentService.findByDepartment(department).getCode();
            departmentList.add(departmentService.findByDepartmentIdx(parentCode));
            departmentList.addAll(departmentService.findChildDescByParentCode(parentCode));
        }

        for (DepartmentVO departmentVO : departmentList) {
            int TotalPc = statisticsMapper.countClientByDepartment(departmentVO.getIdx());
            LinkedHashMap<String, Object> objectMap = new LinkedHashMap<>();
            StatisticsResponseVO statisticsResponseVO = new StatisticsResponseVO(yearMonth, departmentVO.getIdx());

            RunPcAndScoreVO runPcAndScoreVO = statisticsMapper.countRunPcByMonth(statisticsResponseVO);
            List<StatisticsVO> statisticsVO = statisticsMapper.selectItemsByMonth(statisticsResponseVO);
            int[] safePc = new int[16];
            double[] safePcDivideAllPc = new double[16];
            double[] safePcDivideRunPc = new double[16];
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
                    safePcDivideAllPc[i] = (double)((safePc[i] / (double)TotalPc) * 100);
                if(runPcAndScoreVO.getRunPc() != 0)
                    safePcDivideRunPc[i] = (double)(( safePc[i] / (double)runPcAndScoreVO.getRunPc() ) * 100);
            }
            objectMap.put("departmentName", departmentVO.getName());
            objectMap.put("departmentIdx", departmentVO.getCode());
            objectMap.put("totalPc", TotalPc);
            objectMap.put("runPc", runPcAndScoreVO.getRunPc());
            objectMap.put("avgScore", runPcAndScoreVO.getScore());
            objectMap.put("safePc", safePc);
            objectMap.put("safePcAll", safePcDivideAllPc);
            objectMap.put("safePcRun", safePcDivideRunPc);

            departmentResultMap.add(objectMap);
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
                Long score1 = (Long) o1.get("departmentIdx");
                Long score2 = (Long) o2.get("departmentIdx");
                return score1.compareTo(score2);
            }
        });
        return list;
    }

}
