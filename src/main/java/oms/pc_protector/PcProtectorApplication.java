package oms.pc_protector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.*;


@SpringBootApplication
@EnableScheduling
public class PcProtectorApplication {
    public static void main(String[] args) {

        SpringApplication.run(PcProtectorApplication.class, args);
    }

}
