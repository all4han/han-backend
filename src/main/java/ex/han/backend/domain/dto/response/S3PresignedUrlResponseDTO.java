package ex.han.backend.domain.dto.response;


import ex.han.backend.global.ResponseDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Getter
public class S3PresignedUrlResponseDTO extends ResponseDTO {

    private String presignedUrl;

    public S3PresignedUrlResponseDTO(String presignedUrl){
        super();
        this.presignedUrl = presignedUrl;
    }

    public static ResponseEntity<ResponseDTO> success(String presignedUrl) {
        ResponseDTO responseBody = new S3PresignedUrlResponseDTO(presignedUrl);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
