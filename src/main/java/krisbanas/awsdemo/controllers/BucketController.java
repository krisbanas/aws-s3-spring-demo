package krisbanas.awsdemo.controllers;

import com.amazonaws.services.s3.model.Bucket;
import krisbanas.awsdemo.controllers.api.BucketApi;
import krisbanas.awsdemo.services.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Arrays;

@RestController
public class BucketController implements BucketApi {

    private final BucketService bucketService;

    @Autowired
    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    public String listBuckets() {
        return "List of buckets on S3: " + Arrays.toString(bucketService.getAllBuckets().toArray());
    }

    public ResponseEntity<Bucket> createBucket(String bucketName) {
        var bucket = bucketService.createBucket(bucketName);
        return ResponseEntity.created(URI.create("")).body(bucket);
    }

    public ResponseEntity<String> deleteBucket(String bucketName) {
        var deleteSucceeded = bucketService.deleteBucket(bucketName);
        if (deleteSucceeded) return ResponseEntity.accepted().body("Successfully deleted bucket: " + bucketName);
        return ResponseEntity.noContent().build();
    }
}
