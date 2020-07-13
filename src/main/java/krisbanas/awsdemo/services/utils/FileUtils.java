package krisbanas.awsdemo.services.utils;

import krisbanas.awsdemo.exceptions.ResourceExceptionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class FileUtils {

    private FileUtils() {
    }

    public static File convertMultipartFileToFile(MultipartFile file) throws IOException {
        var newFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        file.getInputStream().transferTo(new FileOutputStream(newFile));
        return newFile;
    }

    public static void validateFile(MultipartFile multipartFile, String bucketName) {
        if (multipartFile == null) {
            log.warn("No file to store in bucket {}", bucketName);
            throw new ResourceExceptionException("No file to store sent");
        }
        if (multipartFile.isEmpty()) {
            log.warn("The file to store is empty. Ignoring...");
            throw new ResourceExceptionException("Empty file sent");
        }
    }
}
