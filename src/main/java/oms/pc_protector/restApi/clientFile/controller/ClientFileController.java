package oms.pc_protector.restApi.clientFile.controller;


import lombok.extern.log4j.Log4j2;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.clientFile.model.ClientFileVO;
import oms.pc_protector.restApi.clientFile.service.ClientFileService;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
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
import java.util.Map;

import com.system.util.SUtil;

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> nullex(Exception e) {
        System.err.println("testtest");
        return null;
    }

    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResult<?> agentFileUpload(
            @RequestParam MultipartFile file,
            HttpServletResponse httpServletResponse) throws IOException, NoSuchAlgorithmException, SizeLimitExceededException {

        long fileSize = file.getSize();
        if (fileSize > 104857600) {
            httpServletResponse.sendError(400, "크기 에러!");
            return null;
        }
        if(!file.getOriginalFilename().contains(".exe")) {
            httpServletResponse.sendError(400,"타입 에러!");
            return null;
        }
        if (!SUtil.fileTypeCheck(file.getInputStream())) {
            httpServletResponse.sendError(400,"형식 에러!");
            return null;
        }

        String fileName = file.getOriginalFilename();
        String fileMd5 = clientFileService.makeMd5(file.getInputStream());
        boolean isExistFile = clientFileService.findExistFile();

        ClientFileVO clientFileVO = ClientFileVO.builder()
                .fileName(fileName)
                .fileSize(fileSize)
                .md5(fileMd5)
                .build();

        log.info("FILE 이름 : " + fileName);
        log.info("FILE 크기 : " + fileSize);
        log.info("MD5 : " + fileMd5);

        if (isExistFile) {
            log.info("FILE 업데이트");
            clientFileService.update(clientFileVO);
        } else {
            log.info("FILE 등록");
            clientFileService.registerClientFile(clientFileVO);
        }

        clientFileVO = clientFileService.findClientFile();
        return responseService.getSingleResult(clientFileVO);
    }

    @GetMapping(value = "")
    public SingleResult<?> findClientFileAll() {
        ClientFileVO clientFile = clientFileService.findClientFile();
        return responseService.getSingleResult(clientFile);
    }

}
