package oms.pc_protector.restApi.clientFile;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@RestControllerAdvice
public class MultipartExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
    @ResponseBody
    protected String handleMaxUploadSizeExceededException(final HttpServletRequest request,
                                                               final HttpServletResponse response, final Throwable e)
            throws IOException
    {
        log.error(e);
        return "test";
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    protected String handleGenericMultipartException(final HttpServletRequest request,
                                                          final HttpServletResponse response, final Throwable e)
            throws IOException
    {
        log.error(e);
        return "testtest";
    }
}
