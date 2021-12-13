//package oms.pc_protector.util;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.net.UnknownHostException;
//
//@Slf4j
//@Component
//public class License implements CommandLineRunner {
//
//    @Autowired
//    ParseRoute parseRoute;
//
//    @Override
//    public void run(String... args) throws Exception {
//        try {
//            log.info("맥 주소 : {}", getHardwareAddress());
//            File file = new File("oms.lic");
//            File rootPath = file.getAbsoluteFile();
//            System.out.println("라이센스 경로 : " + rootPath);
//            FileReader filereader = new FileReader(file);
//            //입력 버퍼 생성
//            BufferedReader bufReader = new BufferedReader(filereader);
//            String line = "";
//            String product = "", domain = "", inputId = "";
//            while ((line = bufReader.readLine()) != null) {
//                if (line.contains("product")) {
//                    product = line.split("=")[1];
//                } else if (line.contains("domain")) {
//                    domain = line.split("=")[1];
//                } else if (line.contains("id")) {
//                    inputId = line.split("=")[1];
//                }
//            }
//            //.readLine()은 끝에 개행문자를 읽지 않는다.
//            bufReader.close();
//            String hashProduct = DigestUtils.sha512Hex(product);
//            hashProduct = DigestUtils.sha512Hex(hashProduct);
//            String hashDomain = DigestUtils.sha512Hex(domain);
//            hashDomain = DigestUtils.sha512Hex(hashDomain);
//            String hashMac = DigestUtils.sha512Hex(getHardwareAddress());
//            hashMac = DigestUtils.sha512Hex(hashMac);
//            String hashUuid = DigestUtils.sha512Hex(hashProduct.substring(1, 2) + hashDomain.substring(1, 2) + hashMac.substring(1, 2));
//            hashUuid = DigestUtils.sha512Hex(hashUuid);
//            String id = DigestUtils.sha512Hex(hashProduct + hashDomain + hashMac + hashUuid);
//            id = DigestUtils.sha512Hex(id);
//            if (!inputId.equals(id)) {
//                log.error("라이센스 불일치");
//                System.exit(0);
//            }
//            log.info("라이센스 일치");
//        } catch (FileNotFoundException | NullPointerException e) {
//            e.printStackTrace();
//            log.error("라이센스가 존재하지 않거나 일치하지 않습니다.");
//            System.exit(0);
//        }
//    }
//
//    public String getHardwareAddress() throws UnknownHostException,
//            SocketException
//    {
//        InetAddress ipAddress = InetAddress.getByName(parseRoute.getLocalIPAddress());
//        NetworkInterface networkInterface = NetworkInterface
//                .getByInetAddress(ipAddress);
//        byte[] macAddressBytes = networkInterface.getHardwareAddress();
//        StringBuilder macAddressBuilder = new StringBuilder();
//        for (int macAddressByteIndex = 0; macAddressByteIndex < macAddressBytes.length; macAddressByteIndex++)
//        {
//            String macAddressHexByte = String.format("%02X",
//                    macAddressBytes[macAddressByteIndex]);
//            macAddressBuilder.append(macAddressHexByte);
//
//            if (macAddressByteIndex != macAddressBytes.length - 1)
//            {
//                macAddressBuilder.append(":");
//            }
//        }
//
//        return macAddressBuilder.toString();
//    }
//}
