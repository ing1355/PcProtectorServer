package oms.pc_protector.restApi.clientFile.service;

import lombok.extern.log4j.Log4j2;
import oms.pc_protector.restApi.clientFile.mapper.ClientFileMapper;
import oms.pc_protector.restApi.clientFile.model.ClientFileVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class ClientFileService {

    private final ClientFileMapper clientFileMapper;

    public ClientFileService(ClientFileMapper clientFileMapper) {
        this.clientFileMapper = clientFileMapper;
    }

    @Transactional
    public ClientFileVO findClientFile() {
        return Optional.ofNullable(clientFileMapper.selectClientFile())
                .orElseThrow(() -> new RuntimeException("등록된 파일이 없습니다."));
    }


    @Transactional
    public String findRecentMd5() {
        return Optional.ofNullable(findClientFile())
                .map(ClientFileVO::getMd5).orElse("");
    }


    @Transactional
    public void registerClientFile(ClientFileVO clientFileVO) {
        clientFileMapper.insertClientFile(clientFileVO);
    }


    @Transactional
    public void removeClientFile(ClientFileVO clientFileVO) {
        clientFileMapper.deleteClientFile(clientFileVO);
    }


    @Transactional
    public boolean findExistFile() {
        int result = clientFileMapper.selectExistFile();
        return result > 0;
    }


    @Transactional
    public void update(ClientFileVO clientFileVO) {
        clientFileMapper.update(clientFileVO);
    }


    @Transactional
    public String makeMd5(InputStream inputStream) throws NoSuchAlgorithmException, IOException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        DigestInputStream digestInputStream = new DigestInputStream(bufferedInputStream, md5);

        while (digestInputStream.read() != -1) ;

        byte[] hash = md5.digest();

        Formatter formatter = new Formatter();

        for (byte b : hash) {
            formatter.format("%02x", b);
        }

        String hashString = formatter.toString();

        digestInputStream.close();
        formatter.close();
        return hashString;
    }

}
