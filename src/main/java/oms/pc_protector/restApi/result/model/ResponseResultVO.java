package oms.pc_protector.restApi.result.model;

import lombok.*;

@Getter
@Setter
public class ResponseResultVO {

        private String userId;
        private String ipAddress;
        private String department;
        private String os;
        private String checkTime;
        private String pcProtectorVersion;
        private boolean wrongMd5;

        private int score;

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

}
