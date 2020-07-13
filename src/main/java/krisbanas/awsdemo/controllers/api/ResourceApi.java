package krisbanas.awsdemo.controllers.api;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/bucket/{bucketName}")
public interface ResourceApi {

    @PostMapping("/file")
    @ResponseStatus(value = HttpStatus.CREATED)
    ResponseEntity<String> storeResource(@RequestPart("file") MultipartFile file, @PathVariable String bucketName) throws IOException;

    @PostMapping("/file-partial")
    @ResponseStatus(value = HttpStatus.CREATED)
    ResponseEntity<String> storeResourceStreamed(
            @RequestPart("file") MultipartFile file,
            @PathVariable String bucketName
    ) throws IOException, InterruptedException;

    @GetMapping("/file/{resourceId}")
    @ResponseStatus(value = HttpStatus.OK)
    ResponseEntity<Resource> getResource(@PathVariable String bucketName, @PathVariable String resourceId) throws IOException;

    @DeleteMapping("/file/{resourceId}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    ResponseEntity<String> deleteResource(@PathVariable String bucketName, @PathVariable String resourceId);
}
