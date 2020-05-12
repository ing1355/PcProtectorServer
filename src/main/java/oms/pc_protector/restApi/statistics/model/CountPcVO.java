package oms.pc_protector.restApi.statistics.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountPcVO {

    private int totalPc;
    private int runPc;
    private int avgScore;

    private int[] safePc;
    private int[] safePcDivideAllPc;
    private int[] safePcDivideRunPc;

}
