package krisbanas.awsdemo.controllers;

import krisbanas.awsdemo.controllers.api.ResourceApi;
import krisbanas.awsdemo.services.BasicResourceService;
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
public class ResourceController implements ResourceApi {

    private final BasicResourceService basicResourceService;

    @Autowired
    public ResourceController(BasicResourceService basicResourceService) {
        this.basicResourceService = basicResourceService;
    }

    public ResponseEntity<String> storeResource(MultipartFile file, String bucketName) throws IOException {
        var id = basicResourceService.storeResourceToBucket(file, bucketName);

        return ResponseEntity.created(URI.create(""))
                .body(id);
    }

    public ResponseEntity<String> storeResourceStreamed(MultipartFile file, String bucketName) throws IOException, InterruptedException {
        var id = basicResourceService.storeResourceStreamed(file, bucketName);

        return ResponseEntity.created(URI.create(""))
                .body(id);
    }

    public ResponseEntity<Resource> getResource(String bucketName, String resourceId) throws IOException {
        var resource = basicResourceService.getResource(bucketName, resourceId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + resourceId)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    public ResponseEntity<String> deleteResource(String bucketName, String resourceId) {
        var deleteSucceeded = basicResourceService.deleteResource(bucketName, resourceId);
        if (deleteSucceeded) return ResponseEntity.accepted().body("Successfully deleted resource: " + resourceId);
        return ResponseEntity.noContent().build();
    }
}
