package oms.pc_protector.restApi.clientFile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class MultipartExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
    @ResponseBody
    protected String handleMaxUploadSizeExceededException(final HttpServletRequest request,
                                                          final HttpServletResponse response, final Throwable e)
            throws IOException {
        e.printStackTrace();
        return "test";
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    protected String handleGenericMultipartException(final HttpServletRequest request,
                                                     final HttpServletResponse response, final Throwable e)
            throws IOException {
        return "멀티파트 에러!";
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    @ResponseBody
    protected String httpMessageConversionException(final HttpServletRequest request,
                                      final HttpServletResponse response, final Throwable e)
            throws IOException {
        String test = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        log.info("error file body : " + test);
        log.info(String.valueOf(e));
        return "멀티파트 에러!";
    }


    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseBody
    public String missingServletRequestPartException(final HttpServletRequest request,
                                                                final HttpServletResponse response, final Throwable e)
            throws IOException {
        return "멀티파트 에러!";
    }

    @ExceptionHandler(InputMismatchException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    protected String TimeException(final HttpServletRequest request,
                                   final HttpServletResponse response, final Throwable e)
            throws IOException {
        e.printStackTrace();
        return "날짜가 잘못 설정되어있습니다.";
    }
}
