package krisbanas.awsdemo.controllers;

import krisbanas.awsdemo.services.AmazonS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<String> storeResource(@RequestPart("file") MultipartFile file, @PathVariable String bucketName) throws IOException {
        var id = amazonS3Service.storeResourceToBucket(file, bucketName);
        return ResponseEntity.created(URI.create(""))
                .body(id);
    }

    @PostMapping("/file-partial")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<String> storeResourceStreamed(@RequestPart("file") MultipartFile file, @PathVariable String bucketName) throws IOException, InterruptedException {
        var id = amazonS3Service.storeResourceStreamed(file, bucketName);
        return ResponseEntity.created(URI.create(""))
                .body(id);
    }

    @GetMapping("/file/{resourceId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Resource> getResource(@PathVariable String bucketName, @PathVariable String resourceId) throws IOException {
        var resource = amazonS3Service.getResource(bucketName, resourceId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + resourceId)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @DeleteMapping("/file/{resourceId}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public ResponseEntity<String> deleteResource(@PathVariable String bucketName, @PathVariable String resourceId) {
        var deleteSucceeded = amazonS3Service.deleteResource(bucketName, resourceId);
        if (deleteSucceeded) return ResponseEntity.accepted().body("Successfully deleted resource: " + resourceId);
        return ResponseEntity.noContent().build();
    }
}
