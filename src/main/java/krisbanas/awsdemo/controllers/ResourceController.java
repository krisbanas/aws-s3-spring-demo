package krisbanas.awsdemo.controllers;

import krisbanas.awsdemo.services.AmazonS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/bucket/{bucketName}")
public class ResourceController {

    private final AmazonS3Service amazonS3Service;

    @Autowired
    public ResourceController(AmazonS3Service amazonS3Service) {
        this.amazonS3Service = amazonS3Service;
    }

    @PostMapping("/file")
    public ResponseEntity<String> storeResource(@RequestPart("file") MultipartFile file, @PathVariable String bucketName) throws IOException {
        var id = amazonS3Service.storeResourceToBucket(file, bucketName);
        return ResponseEntity.created(URI.create(""))
                .body(id);
    }

    @GetMapping("/file/{resourceId}")
    public ResponseEntity<Resource> getResource(@PathVariable String resourceId, @PathVariable String bucketName) throws IOException {
        var resource = amazonS3Service.getResource(resourceId, bucketName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + resourceId)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }
}

