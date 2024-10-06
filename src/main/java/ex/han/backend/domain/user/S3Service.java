package ex.han.backend.domain.user;


import com.amazonaws.HttpMethod;

public interface S3Service {

    String generatePresignedUrl(String fileName, HttpMethod method);
    void deleteImageFromS3(String fileName);
}
