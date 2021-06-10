package oms.pc_protector.restApi.manager.service;

import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.restApi.manager.model.ManagerVO;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.PrematureCloseException;

import java.net.ConnectException;

@Service
@Slf4j
public class OmpassService {
    String ompassBaseUrl = "https://www.ompass.kr:8383/fido2/auth";

    public String checkAccessKey(String username)  {
        JSONObject jsonObject = new JSONObject()
                .put("accessKey", "51161e19f3058056a3217dd63784cf169718194937db85832923a9c3c5feedad")
                .put("did",30)
                .put("username",username);


        WebClient webClient = WebClient.builder()
                .baseUrl(ompassBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                .build();

        String response =  webClient.post()
                .uri("/verify-accesskey")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(jsonObject.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("generate AccessToken : {}",response);
        String accessToken = new JSONObject(response).getString("accessToken");
        log.info("accessToken : {}",accessToken);
        return accessToken;

    }

    public String generateOmpassRegisterUrl(ManagerVO managerVO){
        String accessToken = checkAccessKey(managerVO.getId());
        return "https://www.ompass.kr:8383/register/did/30?domain=192.168.182.32:3000&redirect_uri=192.168.182.32:3000/register-fido" +
              "&username="+managerVO.getId()+"&display_name="+managerVO.getName()+"&access_key="+accessToken;

    }

    public boolean verifyAccessToken(JSONObject verifyTokenData) {
        verifyTokenData.remove("userId");

        WebClient webClient = WebClient.builder()
                .baseUrl(ompassBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                .build();

        String response =  webClient.post()
                .uri("/verify-token")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(verifyTokenData.toString())
                .retrieve()
//                .onStatus(HttpStatus::is5xxServerError,
//                        clientResponse -> clientResponse.bodyToMono(String.class).map(OmpServerError::new))
                .bodyToMono(String.class)
                .block();
        log.info("check accessToken result : {}",response);

        return response.equals("true");

    }

    public void deleteOmpass(String userId)  {

        WebClient webClient = WebClient.builder()
                .baseUrl("https://www.ompass.kr:8383/fido2/did/30/username/"+userId)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                .build();

        String response =  webClient.delete()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
//                .onStatus(HttpStatus::is5xxServerError,
//                        clientResponse -> clientResponse.bodyToMono(String.class).map(OmpServerError::new))
                .bodyToMono(String.class)
                .block();
        log.info("response : {}",response);


    }
}
