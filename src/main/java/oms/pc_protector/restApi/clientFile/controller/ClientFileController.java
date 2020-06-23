package oms.pc_protector.restApi.clientFile.controller;


import com.system.util.SUtil;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.clientFile.model.ClientFileVO;
import oms.pc_protector.restApi.clientFile.service.ClientFileService;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Log4j2
@RestController
@CrossOrigin
@RequestMapping(value = "/v1/client-file")
public class ClientFileController {

    private final ResponseService responseService;

    private final ClientFileService clientFileService;

    public ClientFileController(ResponseService responseService,
                                ClientFileService clientFileService) {
        this.responseService = responseService;
        this.clientFileService = clientFileService;
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<?> fileEx(Exception e, HttpServletResponse response) throws IOException {
        System.err.println("file Missing");
        response.sendError(400, "파일 없음!");
        return null;
    }

//    @ExceptionHandler(MultipartException.class)
//    public ResponseEntity<?> fileEx(Exception e, HttpServletResponse response) throws IOException {
//        System.err.println("file Missing");
//        response.sendError(400, "파일 없음!");
//        return null;
//    }

    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResult<?> agentFileUpload(
            @RequestPart("version") String version,
            @RequestParam MultipartFile file,
            HttpServletResponse httpServletResponse) throws IOException, NoSuchAlgorithmException, SizeLimitExceededException {

        long fileSize = file.getSize();
        if (fileSize > 104857600) {
            httpServletResponse.sendError(400, "크기 에러!");
            return null;
        }
        if (!file.getOriginalFilename().contains(".exe")) {
            httpServletResponse.sendError(400, "타입 에러!");
            return null;
        }
        if (!file.getOriginalFilename().equals("Protector.exe")) {
            httpServletResponse.sendError(400, "AGENT 파일 아님!");
            return null;
        }
        if (!SUtil.fileTypeCheck(file.getInputStream())) {
            httpServletResponse.sendError(400, "형식 에러!");
            return null;
        }

        String fileName = file.getOriginalFilename();
        String fileMd5 = clientFileService.makeMd5(file.getInputStream());
        boolean isExistFile = clientFileService.findExistFile(version);
        boolean isExistMd5 = clientFileService.findExistMd5(fileMd5);

        ClientFileVO clientFileVO = ClientFileVO.builder()
                .fileName(fileName)
                .fileSize(fileSize)
                .md5(fileMd5)
                .version(version)
                .build();

        log.info("FILE 이름 : " + fileName);
        log.info("FILE 크기 : " + fileSize);
        log.info("FILE 버전 : " + version);
        log.info("MD5 : " + fileMd5);

        if (isExistMd5) {
            httpServletResponse.sendError(400, "Md5 중복!");
            return null;
        } else {
            if (isExistFile) {
                log.info("FILE 업데이트");
                clientFileService.update(clientFileVO);
            } else {
                log.info("FILE 등록");
                clientFileService.registerClientFile(clientFileVO);
            }
        }


        return responseService.getSingleResult(clientFileService.findClientFile());
    }

    @GetMapping(value = "")
    public SingleResult<?> findClientFileAll() {
        List<ClientFileVO> clientFile = clientFileService.findClientFile();
        return responseService.getSingleResult(clientFile);
    }

    @PutMapping(value = "update")
    public SingleResult<?> updateClientFile(@RequestBody ClientFileVO clientFileVO) {
        return responseService.getSingleResult(clientFileService.findClientFile());
    }

    @PutMapping(value = "delete")
    public SingleResult<?> deleteClientFile(@RequestBody List<ClientFileVO> clientFileVO) {
        clientFileService.removeClientFile(clientFileVO);
        return responseService.getSingleResult(clientFileService.findClientFile());
    }

}
