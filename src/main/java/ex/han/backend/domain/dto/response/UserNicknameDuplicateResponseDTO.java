package ex.han.backend.domain.dto.response;


import ex.han.backend.global.ResponseDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class UserNicknameDuplicateResponseDTO extends ResponseDTO {

    private boolean duplicate;

    public UserNicknameDuplicateResponseDTO(boolean duplicate) {
        super();
        this.duplicate = duplicate;
    }

    public static ResponseEntity<ResponseDTO> success(boolean duplicate) {
        ResponseDTO responseBody = new UserNicknameDuplicateResponseDTO(duplicate);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
