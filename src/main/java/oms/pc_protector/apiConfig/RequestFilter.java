//package oms.pc_protector.apiConfig;
//
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//@Log4j2
//@Component
//@WebFilter(urlPatterns = {"/v1/*"}, description = "API LOG FILTER")
//public class RequestFilter implements javax.servlet.Filter {
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        RereadableRequestWrapper rereadableRequestWrapper = new RereadableRequestWrapper((HttpServletRequest)request);
//        chain.doFilter(rereadableRequestWrapper, response);
//    }
//}