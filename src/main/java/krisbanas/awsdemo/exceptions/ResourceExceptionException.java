package krisbanas.awsdemo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Resource error")
public class ResourceExceptionException extends RuntimeException {
    public ResourceExceptionException(String message) {
        super(message);
    }
}
