package oms.pc_protector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class PcProtectorApplication {
//    static {
//        System.loadLibrary("ex");
//    }
//
//    private static native boolean CompareLicenseKey();

    public static void main(String[] args) {
//        if(CompareLicenseKey()) {
            SpringApplication.run(PcProtectorApplication.class, args);
//        } else {
//            System.exit(0);
//        }
    }


}
