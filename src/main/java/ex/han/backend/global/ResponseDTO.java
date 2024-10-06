package ex.han.backend.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class ResponseDTO {
    private String code;
    private String message;

    public ResponseDTO() {
        this.code = ResponseMessage.SUCCESS;
        this.message = ResponseMessage.SUCCESS;
    }

    public static ResponseEntity<ResponseDTO> success() {
        ResponseDTO responseBody = new ResponseDTO();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> notModified() {
        ResponseDTO responseBody = new ResponseDTO(ResponseMessage.NOT_MODIFIED, ResponseMessage.NOT_MODIFIED);
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> validationFail() {
        ResponseDTO responseBody = new ResponseDTO(ResponseMessage.VALIDATION_FAIL, ResponseMessage.VALIDATION_FAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> authenticationFail() {
        ResponseDTO responseBody = new ResponseDTO(ResponseMessage.AUTHENTICATION_FAILED, ResponseMessage.AUTHENTICATION_FAILED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> databaseError() {
        ResponseDTO responseBody = new ResponseDTO(ResponseMessage.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> notFound() {
        ResponseDTO responseBody = new ResponseDTO(ResponseMessage.RESOURCE_NOT_FOUND, ResponseMessage.RESOURCE_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> conflict(String message) {
        ResponseDTO responseBody = new ResponseDTO(ResponseMessage.ETC, ResponseMessage.ETC + ":" + message);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> internalServerError() {
        ResponseDTO responseBody = new ResponseDTO(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

}
