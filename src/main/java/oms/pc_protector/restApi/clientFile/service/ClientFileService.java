package oms.pc_protector.restApi.clientFile.service;

import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.restApi.clientFile.mapper.ClientFileMapper;
import oms.pc_protector.restApi.clientFile.model.ClientFileVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ClientFileService {

    private final ClientFileMapper clientFileMapper;

    public ClientFileService(ClientFileMapper clientFileMapper) {
        this.clientFileMapper = clientFileMapper;
    }

    @Transactional
    public List<ClientFileVO> findClientFile(String User_Idx) {
        return Optional.ofNullable(clientFileMapper.selectClientFile(User_Idx))
                .orElseThrow(() -> new RuntimeException("등록된 파일이 없습니다."));
    }

    @Transactional
    public ClientFileVO findClientFileRecent(String idx) {
        return Optional.ofNullable(clientFileMapper.selectClientFileRecent(idx))
                .orElse(null);
    }

    @Transactional
    public ArrayList<String> selectVersionList(String User_Idx) {
        return Optional.ofNullable(clientFileMapper.selectVersionList(User_Idx))
                .orElse(null);
    }

    @Transactional
    public String findRecentMd5(String idx) {
        return Optional.ofNullable(findClientFileRecent(idx))
                .map(ClientFileVO::getMd5).orElse("");
    }

    @Transactional
    public String findRecentVersion(String idx) {
        return Optional.ofNullable(findClientFileRecent(idx))
                .map(ClientFileVO::getVersion).orElse("");
    }


    @Transactional
    public void registerClientFile(ClientFileVO clientFileVO) {
        clientFileMapper.insertClientFile(clientFileVO);
    }


    @Transactional
    public int removeClientFile(List<ClientFileVO> clientFileVO, String idx) {
        int temp = 0;
        for(ClientFileVO clientFileVO1 : clientFileVO) {
            clientFileVO1.setIdx(idx);
            temp = clientFileMapper.deleteClientFile(clientFileVO1);
        }
        return temp;
    }

    @Transactional
    public boolean findExistMd5(String md5, String User_Idx) {
        int result = clientFileMapper.selectExistMd5(md5, User_Idx);
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
