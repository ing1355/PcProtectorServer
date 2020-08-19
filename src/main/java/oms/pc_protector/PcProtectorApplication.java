package oms.pc_protector;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


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
