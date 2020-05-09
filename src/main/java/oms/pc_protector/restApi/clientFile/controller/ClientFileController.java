package oms.pc_protector.restApi.clientFile.controller;


import lombok.extern.log4j.Log4j2;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.clientFile.model.ClientFileVO;
import oms.pc_protector.restApi.clientFile.service.ClientFileService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;

@Log4j2
@RestController
@CrossOrigin
@RequestMapping(value = "**/client-file")
public class ClientFileController {

    private final ResponseService responseService;

    private final ClientFileService clientFileService;

    public ClientFileController(ResponseService responseService,
                                ClientFileService clientFileService) {
        this.responseService = responseService;
        this.clientFileService = clientFileService;
    }

    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResult<?> agentFileUpload(
            @RequestParam MultipartFile file)
            throws IOException, NoSuchAlgorithmException {

        String fileName = file.getOriginalFilename();
        long fileSize = file.getSize();
        String fileMd5 = clientFileService.makeMd5(file.getInputStream());
        boolean isExistFile = clientFileService.findExistFile();

        ClientFileVO clientFileVO = ClientFileVO.builder()
                .fileName(fileName)
                .fileSize(fileSize)
                .md5(fileMd5)
                .build();

        log.info("--------------File Upload---------------");
        log.info("FILE 이름 : " + fileName);
        log.info("FILE 크기 : " + fileSize);
        log.info("MD5 : " + fileMd5);

        if(isExistFile) {
            log.info("FILE 업데이트");
            clientFileService.update(clientFileVO);
        }
        else {
            log.info("FILE 등록");
            clientFileService.registerClientFile(clientFileVO);
        }
        return responseService.getSingleResult(clientFileVO);
    }


    @GetMapping(value = "")
    public SingleResult<?> findClientFileAll() {
        List<ClientFileVO> clientFile = clientFileService.findClientFileAll();
        return responseService.getSingleResult(clientFile);
    }

}
