package krisbanas.awsdemo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Incorrect bucket name")
public class CreateBucketException extends RuntimeException {
    public CreateBucketException(String message) {
        super(message);
    }
}
