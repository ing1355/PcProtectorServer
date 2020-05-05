package response;

import org.springframework.http.HttpStatus;

public class sendError extends Throwable {
    public sendError(HttpStatus internalServerError) {

    }
}
