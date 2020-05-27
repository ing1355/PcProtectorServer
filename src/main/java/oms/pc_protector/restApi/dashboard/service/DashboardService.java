package oms.pc_protector.restApi.dashboard.service;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.client.service.ClientService;
import oms.pc_protector.restApi.dashboard.mapper.DashboardMapper;
import oms.pc_protector.restApi.dashboard.model.ChartVO;
import oms.pc_protector.restApi.department.model.DepartmentVO;
import oms.pc_protector.restApi.department.service.DepartmentService;
import oms.pc_protector.restApi.policy.model.NowScheduleVO;
import oms.pc_protector.restApi.policy.service.ConfigurationService;
import oms.pc_protector.restApi.result.service.ResultService;
import oms.pc_protector.restApi.statistics.mapper.StatisticsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.*;

@Log4j2
@Service
public class DashboardService {

    private ClientService clientService;
    private ResultService resultService;
    private DepartmentService departmentService;
    private DashboardMapper dashboardMapper;
    private ConfigurationService configurationService;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
    private String currentTime = format.format(System.currentTimeMillis());

    public DashboardService(ClientService clientService,
                            ResultService resultService,
                            DepartmentService departmentService,
                            DashboardMapper dashboardMapper,
                            ConfigurationService configurationService) {
        this.clientService = clientService;
        this.resultService = resultService;
        this.departmentService = departmentService;
        this.dashboardMapper = dashboardMapper;
        this.configurationService = configurationService;
    }

    @Transactional
    public HashMap<String, Object> dashboardTop() {
        LinkedHashMap<String, Object> dashboardTopMap = new LinkedHashMap<>();

        int totalPc = clientService.count();
        int runPc = resultService.countByMonth(currentTime);
        String resultRate = String.valueOf((int) (((double) runPc / (double) totalPc) * 100)) + "%";

        log.info("전체 PC : {}", totalPc);
        log.info("실행 PC : {}", runPc);
        log.info("진단 비율 : {}", resultRate);

        dashboardTopMap.put("totalPc", totalPc);
        dashboardTopMap.put("runPc", runPc);
        dashboardTopMap.put("resultRate", resultRate);
        return dashboardTopMap;
    }

    @Transactional
    public HashMap<String, Object> dashboardMiddle() {
        HashMap<String, Object> dashboardResultMap = new HashMap<>();
        dashboardResultMap.put("resultTop5", ResultTop5());
        dashboardResultMap.put("resultLowTop5", ResultLowTop5());
        dashboardResultMap.put("userCountByScore", userCountByScore());
        return dashboardResultMap;
    }

    @Transactional
    public HashMap<String, Object> dashboardBottom(String startdate, String enddate, String term) {
        HashMap<String, Object> dashboardResultMap = new HashMap<>();
        dashboardResultMap.put("resultAvgScore", resultAvgScoreByCurrentMonth());
        if (term.equals("3개월") || term.equals("6개월"))
            dashboardResultMap.put("resultChart", resultChart());
        else
            dashboardResultMap.put("resultChart", resultChartDays(startdate, enddate));
        return dashboardResultMap;
    }

    @Transactional
    public List<HashMap<String, Object>> findScoreListByDepartment() {
        List<DepartmentVO> departmentList = departmentService.findAll();
        List<HashMap<String, Object>> departmentScore = new ArrayList<>();

        for (DepartmentVO department : departmentList) {
            List<Integer> scoreList =
                    resultService.findScoreByDepartmentWithMonth(department.getName());
            int scoreSum = 0;
            int totalPc = scoreList.size();

            if (totalPc == 0) continue;
            for (int score : scoreList) scoreSum += score;
            int avgScore = scoreSum / totalPc;

            HashMap<String, Object> scoreMap = new HashMap<>();
            scoreMap.put("department", department.getName());
            scoreMap.put("score", avgScore);
            departmentScore.add(scoreMap);
        }
        return departmentScore;
    }

    @Transactional
    public List<HashMap<String, Object>> ResultTop5() {
        List<HashMap<String, Object>> departmentScore = findScoreListByDepartment();

        Collections.sort(departmentScore, new Comparator<HashMap<String, Object>>() {
            @Override
            public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                Integer score1 = (int) o1.get("score");
                Integer score2 = (int) o2.get("score");
                return score2.compareTo(score1);
            }
        });

        ArrayList<HashMap<String, Object>> scoreArray = new ArrayList<>();
        for (int i = 0; i < departmentScore.size(); i++) {
            if (i > 4) break;
            scoreArray.add(departmentScore.get(i));
        }
        return scoreArray;
    }

    @Transactional
    public List<HashMap<String, Object>> ResultLowTop5() {
        List<HashMap<String, Object>> departmentScore = findScoreListByDepartment();

        Collections.sort(departmentScore, new Comparator<HashMap<String, Object>>() {
            @Override
            public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                Integer score1 = (int) o1.get("score");
                Integer score2 = (int) o2.get("score");
                return score1.compareTo(score2);
            }
        });

        ArrayList<HashMap<String, Object>> scoreArray = new ArrayList<>();
        for (int i = 0; i < departmentScore.size(); i++) {
            if (i > 4) break;
            scoreArray.add(departmentScore.get(i));
        }
        return scoreArray;
    }

    @Transactional
    public HashMap<String, Object> userCountByScore() {
        LinkedHashMap<String, Object> userCountMap = new LinkedHashMap<>();
        int topScoreUserCount = dashboardMapper.selectUserCountByScore(90, 100);
        int middleScoreUserCount = dashboardMapper.selectUserCountByScore(70, 89);
        int lowScoreUserCount = dashboardMapper.selectUserCountByScore(0, 69);
        userCountMap.put("topScoreUserCount", topScoreUserCount);
        userCountMap.put("middleScoreUserCount", middleScoreUserCount);
        userCountMap.put("lowScoreUserCount", lowScoreUserCount);
        return userCountMap;
    }

    @Transactional
    public int resultAvgScoreByCurrentMonth() {
        log.info(dashboardMapper.selectAvgScoreByMonth(currentTime));
        return dashboardMapper.selectAvgScoreByMonth(currentTime);
    }

    @Transactional
    public List<ChartVO> resultChart() {
        LinkedHashMap<String, Object> scoreMap = new LinkedHashMap<>();
        List<HashMap<String, Object>> avgScoreList = new ArrayList<>();
        return Optional
                .ofNullable(dashboardMapper.selectAvgScoreByRecent6Months())
                .orElseGet(ArrayList::new);
    }

    @Transactional
    public List<ChartVO> resultChartDays(String startdate, String enddate) {
        LinkedHashMap<String, Object> scoreMap = new LinkedHashMap<>();
        List<HashMap<String, Object>> avgScoreList = new ArrayList<>();
        return Optional
                .ofNullable(dashboardMapper.selectAvgScoreByRecent1Months())
                .orElseGet(ArrayList::new);
    }

}
