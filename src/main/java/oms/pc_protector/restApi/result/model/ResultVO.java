package oms.pc_protector.restApi.result.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ResultVO {

    private String userId;
    private String ipAddress;
    private String checkTime;
    private int score;
    private String departmentIdx;

    private int item1Result;
    private int item2Result;
    private int item3Result;
    private int item4Result;
    private int item5Result;
    private int item6Result;
    private int item7Result;
    private int item8Result;
    private int item9Result;
    private int item10Result;
    private int item11Result;
    private int item12Result;
    private int item13Result;
    private int item14Result;
    private int item15Result;
    private int item16Result;

    private int item1InstallationStatusCheck;
    private int item1ExecutionStatusCheck;

    private int item2ExecutionStatusCheck;
    private int item2UpdateStatusCheck;

    private int item3Count;
    private int item4Count;
    private int item10Count;

    private int item5ReasonsVulnerability;
    private int item5PasswordLength;
    private String item5DetailReasons;

    private int item6PwLastChangePastDate;

    private int item7ExecutionStatusCheck;
    private int item7PwUsageStatus;
    private int item7Period;

    private boolean inSchedule = false;
    private String startTime;
    private String endTime;
}
