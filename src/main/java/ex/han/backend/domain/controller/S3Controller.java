package ex.han.backend.domain.controller;

import com.amazonaws.HttpMethod;

import ex.han.backend.domain.dto.response.S3PresignedUrlResponseDTO;
import ex.han.backend.domain.user.S3Service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RequestMapping("/api/s3")
@RestController
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/generate-presigned-url/put")
    public ResponseEntity<? super S3PresignedUrlResponseDTO> generatePresignedUrlForPutMethod(
            @RequestParam("fileName") String fileName) {
        String presignedUrl = s3Service.generatePresignedUrl(fileName, HttpMethod.PUT);
        log.info(presignedUrl);
        return S3PresignedUrlResponseDTO.success(presignedUrl);
    }

    @GetMapping("/generate-presigned-url/get")
    public ResponseEntity<? super S3PresignedUrlResponseDTO> generatePresignedUrlForGetMethod(
            @RequestParam("fileName") String fileName) {
        String presignedUrl = s3Service.generatePresignedUrl(fileName, HttpMethod.GET);
        log.info(presignedUrl);
        return S3PresignedUrlResponseDTO.success(presignedUrl);
    }
}
