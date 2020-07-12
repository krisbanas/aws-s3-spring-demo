package krisbanas.awsdemo.controllers;

import com.amazonaws.services.s3.model.Bucket;
import krisbanas.awsdemo.services.AmazonS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;

@RestController
@RequestMapping("buckets")
public class BucketController {

    private final AmazonS3Service amazonS3Service;

    @Autowired
    public BucketController(AmazonS3Service amazonS3Service) {
        this.amazonS3Service = amazonS3Service;
    }

    @GetMapping
    public String listBuckets() {
        return "List of buckets on S3: " + Arrays.toString(amazonS3Service.getAllBuckets().toArray());
    }

    @PostMapping(path = "{bucketName}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Bucket> createBucket(@PathVariable String bucketName) {
        var bucket = amazonS3Service.createBucket(bucketName);
        return ResponseEntity.created(URI.create("")).body(bucket);
    }

    @DeleteMapping(path = "{bucketName}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public ResponseEntity<String> deleteBucket(@PathVariable String bucketName) {
        var deleteSucceeded = amazonS3Service.deleteBucket(bucketName);
        if (deleteSucceeded) return ResponseEntity.accepted().build();
        return ResponseEntity.noContent().build();
    }
}
