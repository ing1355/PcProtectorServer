package oms.pc_protector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.*;


@SpringBootApplication
@EnableScheduling
public class PcProtectorApplication {
    public static void main(String[] args) throws IOException {
        File file = new File("./oms.lic");
        FileReader filereader = new FileReader(file);
        //입력 버퍼 생성
        BufferedReader bufReader = new BufferedReader(filereader);
        String line = "";
        while ((line = bufReader.readLine()) != null) {
            System.out.println(line);
        }
        //.readLine()은 끝에 개행문자를 읽지 않는다.
        bufReader.close();

        SpringApplication.run(PcProtectorApplication.class, args);
    }

}
