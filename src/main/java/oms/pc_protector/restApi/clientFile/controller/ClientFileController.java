package oms.pc_protector.restApi.clientFile.controller;


import lombok.extern.log4j.Log4j2;
import oms.pc_protector.apiConfig.model.SingleResult;
import oms.pc_protector.apiConfig.service.ResponseService;
import oms.pc_protector.restApi.clientFile.model.ClientFileVO;
import oms.pc_protector.restApi.clientFile.service.ClientFileService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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


    @GetMapping(value = "")
    public SingleResult<?> findClientFileAll() {
        List<ClientFileVO> clientFile = clientFileService.findClientFileAll();
        return responseService.getSingleResult(clientFile);
    }


    @PostMapping(value = "")
    public SingleResult<?> registerClientFile(@RequestBody @Valid ClientFileVO clientFileVO) {
        clientFileService.registerClientFile(clientFileVO);
        return responseService.getSingleResult(true);
    }
}
