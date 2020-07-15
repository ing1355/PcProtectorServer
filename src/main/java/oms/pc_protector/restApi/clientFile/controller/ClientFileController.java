package oms.pc_protector.restApi.clientFile.controller;


import com.system.util.SUtil;
import lombok.extern.log4j.Log4j2;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.clientFile.model.ClientFileVO;
import oms.pc_protector.restApi.clientFile.service.ClientFileService;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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

//    @ExceptionHandler(MultipartException.class)
//    public ResponseEntity<?> fileEx(Exception e, HttpServletResponse response) throws IOException {
//        System.err.println("file Missing");
//        response.sendError(400, "파일 없음!");
//        return null;
//    }

    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResult<?> agentFileUpload(
            @RequestBody @Valid ClientFileVO inputFile,
//            @RequestPart("version") String version,
////            @RequestParam MultipartFile file,
//            @RequestPart("file") String test,
//            @RequestPart("size") Long size,
//            @RequestPart("name") String name,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws IOException, NoSuchAlgorithmException, SizeLimitExceededException {

            byte[] bt = Base64.getDecoder().decode(new String(inputFile.getMd5()).getBytes("UTF-8"));
            InputStream inputStream = new ByteArrayInputStream(bt);
//        long fileSize = file.getSize();
//        final InputStream inputStream = file.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        inputStream.transferTo(baos);
        InputStream firstClone = new ByteArrayInputStream(baos.toByteArray());
        InputStream secondClone = new ByteArrayInputStream(baos.toByteArray());
//        if (fileCheckFunction(file, httpServletResponse, secondClone, fileSize)) return null;
//        String fileName = inputFile.getFileName();


        String fileMd5 = clientFileService.makeMd5(firstClone);

        ClientFileVO clientFileVO = ClientFileVO.builder()
                .fileName(inputFile.getFileName())
                .fileSize(inputFile.getFileSize())
                .md5(fileMd5)
                .version(inputFile.getVersion())
                .build();

//        log.info("FILE 이름 : " + name);
//        log.info("FILE 크기 : " + size);
//        log.info("FILE 버전 : " + version);
        log.info("MD5 : " + fileMd5);

        if (clientFileService.findExistMd5(fileMd5)) {
            httpServletResponse.sendError(400, "Md5 중복!");
            return null;
        } else {
            log.info("FILE 등록");
            ArrayList<String> version_list = clientFileService.selectVersionList();
            String[] req_version = inputFile.getVersion().split("[.]");
            for (String res_version : version_list) {
                String[] temp = res_version.split("[.]");
                int count = 0;
                for (int i = 0; i < temp.length; i++) {
                    if (Integer.parseInt(req_version[i]) <= Integer.parseInt(temp[i])) count++;
                }
                if (count == 4) {
                    httpServletResponse.sendError(400, res_version + "보다 높은 버전을 입력해주세요.");
                    return null;
                }
            }
            clientFileService.registerClientFile(clientFileVO);
        }

        return responseService.getSingleResult(clientFileService.findClientFile());
    }

    @GetMapping(value = "")
    public SingleResult<?> findClientFileAll() {
        List<ClientFileVO> clientFile = clientFileService.findClientFile();
        return responseService.getSingleResult(clientFile);
    }

    @PostMapping(value = "update")
    public SingleResult<?> updateClientFile(@RequestPart("version") String version,
                                            @RequestParam MultipartFile file,
                                            HttpServletResponse httpServletResponse) throws IOException, NoSuchAlgorithmException {
        long fileSize = file.getSize();
        final InputStream inputStream = file.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        inputStream.transferTo(baos);
        InputStream firstClone = new ByteArrayInputStream(baos.toByteArray());
        InputStream secondClone = new ByteArrayInputStream(baos.toByteArray());
        String fileMd5 = clientFileService.makeMd5(firstClone);
        if (fileCheckFunction(file, httpServletResponse, secondClone, fileSize)) return null;
        String fileName = file.getOriginalFilename();



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

        if (clientFileService.findExistMd5(fileMd5)) {
            httpServletResponse.sendError(400, "Md5 중복!");
            return null;
        }
        clientFileService.update(clientFileVO);
        return responseService.getSingleResult(clientFileService.findClientFile());
    }

    public boolean fileCheckFunction(@RequestParam MultipartFile file, HttpServletResponse httpServletResponse, InputStream inputStream, long fileSize) throws IOException {
        if (fileSize > 104857600) {
            httpServletResponse.sendError(400, "크기 에러!");
            return true;
        }
        if (!file.getOriginalFilename().contains(".exe")) {
            httpServletResponse.sendError(400, "타입 에러!");
            return true;
        }
        if (!file.getOriginalFilename().equals("Protector.exe")) {
            httpServletResponse.sendError(400, "AGENT 파일 아님!");
            return true;
        }

        if (!SUtil.fileTypeCheck(inputStream)) {
            httpServletResponse.sendError(400, "형식 에러!");
            return true;
        }
        return false;
    }

    @PutMapping(value = "delete")
    public SingleResult<?> deleteClientFile(@RequestBody List<ClientFileVO> clientFileVO) {
        int row_num = clientFileService.removeClientFile(clientFileVO);

        return responseService.getSingleResult(clientFileService.findClientFile());
    }

}
