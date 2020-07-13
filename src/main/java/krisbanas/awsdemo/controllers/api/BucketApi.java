package krisbanas.awsdemo.controllers.api;

import com.amazonaws.services.s3.model.Bucket;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("buckets")
public interface BucketApi {

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    String listBuckets();

    @PostMapping(path = "{bucketName}")
    @ResponseStatus(value = HttpStatus.CREATED)
    ResponseEntity<Bucket> createBucket(@PathVariable String bucketName);

    @DeleteMapping(path = "{bucketName}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    ResponseEntity<String> deleteBucket(@PathVariable String bucketName);
}
