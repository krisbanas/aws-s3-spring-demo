package krisbanas.awsdemo.controllers;

import krisbanas.awsdemo.services.AmazonS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController("/")
public class AmazonS3Controller {

    private final AmazonS3Service amazonS3Service;

    @Autowired
    public AmazonS3Controller(AmazonS3Service amazonS3Service) {
        this.amazonS3Service = amazonS3Service;
    }

    @GetMapping(path = "list-buckets")
    public String listBuckets() {
        return "List of buckets on S3: " + Arrays.toString(amazonS3Service.getAllBuckets().toArray());
    }
}
