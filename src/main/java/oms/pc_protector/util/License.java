package oms.pc_protector.util;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

@Log4j2
@Component
public class License implements CommandLineRunner {

    @Autowired
    ParseRoute parseRoute;

    @Override
    public void run(String... args) throws Exception {
        File file = new File("/home/oms/Desktop/oms.lic");
        log.info(file.getAbsoluteFile());
        FileReader filereader = new FileReader(file);
        //입력 버퍼 생성
        BufferedReader bufReader = new BufferedReader(filereader);
        String line = "";
        String product = "", domain = "", input_id = "";
        while ((line = bufReader.readLine()) != null) {
            if(line.contains("product")) {
                product = line.split("=")[1];
            } else if(line.contains("domain")) {
                domain = line.split("=")[1];
            } else if(line.contains("id")) {
                input_id = line.split("=")[1];
            }
        }
        //.readLine()은 끝에 개행문자를 읽지 않는다.
        bufReader.close();

        String hash_product = DigestUtils.sha512Hex(product);
        hash_product = DigestUtils.sha512Hex(hash_product);
        String hash_domain = DigestUtils.sha512Hex(domain);
        hash_domain = DigestUtils.sha512Hex(hash_domain);
        String hash_mac = DigestUtils.sha512Hex(getHardwareAddress());
        hash_mac = DigestUtils.sha512Hex(hash_mac);
        String hash_uuid = DigestUtils.sha512Hex(hash_product.substring(1,2) + hash_domain.substring(1,2) + hash_mac.substring(1,2));
        hash_uuid = DigestUtils.sha512Hex(hash_uuid);

        String id = DigestUtils.sha512Hex(hash_product + hash_domain + hash_mac + hash_uuid);
        id = DigestUtils.sha512Hex(id);

        if(!input_id.equals(id)) {
            System.exit(0);
        }
    }

    public String getHardwareAddress() throws UnknownHostException,
            SocketException
    {
        InetAddress ipAddress = InetAddress.getByName(parseRoute.getLocalIPAddress());
        NetworkInterface networkInterface = NetworkInterface
                .getByInetAddress(ipAddress);
        byte[] macAddressBytes = networkInterface.getHardwareAddress();
        StringBuilder macAddressBuilder = new StringBuilder();
        for (int macAddressByteIndex = 0; macAddressByteIndex < macAddressBytes.length; macAddressByteIndex++)
        {
            String macAddressHexByte = String.format("%02X",
                    macAddressBytes[macAddressByteIndex]);
            macAddressBuilder.append(macAddressHexByte);

            if (macAddressByteIndex != macAddressBytes.length - 1)
            {
                macAddressBuilder.append(":");
            }
        }

        return macAddressBuilder.toString();
    }
}
