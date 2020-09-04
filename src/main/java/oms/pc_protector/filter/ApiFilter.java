package oms.pc_protector.filter;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import oms.pc_protector.apiConfig.ReadableRequestWrapper;
import oms.pc_protector.restApi.clientFile.service.ClientFileService;
import oms.pc_protector.restApi.log.model.LogVO;
import oms.pc_protector.restApi.log.service.LogService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@CrossOrigin
@WebFilter(urlPatterns = {"/v1/*"}, description = "API LOG FILTER")
public class ApiFilter implements Filter {


    private LogService logService;
    private ClientFileService clientFileService;

    public ApiFilter(LogService logService, ClientFileService clientFileService) {
        this.logService = logService;
        this.clientFileService = clientFileService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public String GetUserIdByPrincipal(Principal principal) throws NullPointerException {
        try {
            return principal.getName();
        } catch (NullPointerException e) {
            log.error("정보 없음");
            return "정보 없음";
        }
//        return principal.getName();
    }

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Principal User_info = request.getUserPrincipal();
        Map<String, String[]> parameterMap = request.getParameterMap();

        LogVO logVO = new LogVO();
        logVO.setUri(request.getRequestURI());
        logVO.setMethod(request.getMethod());
        logVO.setIpAddress(request.getRemoteAddr());


        boolean hasClientURI = excludeURI(request);

        if (!(logVO.getUri().contains("chunk")) && !(logVO.getUri().contains(".svg"))
                && !(logVO.getUri().contains(".json")) && !(logVO.getUri().contains(".png"))
                && !(logVO.getUri().contains(".woff")) && !(logVO.getUri().contains(".ttf"))
                && !(logVO.getUri().contains("/front")) && !(logVO.getUri().contains("favicon.ico"))) {
            Set<String> ketSet = parameterMap.keySet();
            if (hasClientURI) {
                log.info("=============CLIENT API=============");
            } else {
                log.info("============FRONTEND API============");
                if (request.getRequestURI().contains("/lock") ||
                        request.getRequestURI().contains("/firstlogin") ||
                        request.getRequestURI().contains("/logout")) {
                    logVO.setManagerId(parameterMap.get("userId")[0]);
                } else {
                    logVO.setManagerId(GetUserIdByPrincipal(User_info));
                }
                if (!(logVO.getMethod().equals("GET"))) {
                    logService.register(logVO);
                }
            }

            log.info("Request Uri: {}", request.getRequestURI());


            for (String parameterKey : ketSet) {
                String[] parameterValueArray = parameterMap.get(parameterKey);
                log.info("ParameterKey : " + parameterKey + " , ParameterValue : " + Arrays.toString(parameterValueArray));
            }


            log.info("Request Method: {}", request.getMethod());
            log.info("Request IpAddress : {}", request.getRemoteAddr());
            log.info("Local IpAddress : {}", request.getLocalAddr());
            log.info("Request Protocol : {}", request.getProtocol());
            log.info("====================================");
            log.info("");

        }
        ReadableRequestWrapper rereadableRequestWrapper = new ReadableRequestWrapper((HttpServletRequest) request);
        filterChain.doFilter(rereadableRequestWrapper, response);
    }

    @Override
    public void destroy() {

    }

    // 클라이언트 API 구분하기 위한 메소드
    private boolean excludeURI(HttpServletRequest request) {
        String uri = request.getRequestURI().toString().replaceAll("v1/", "");
        return uri.startsWith("/client/");
    }
}
