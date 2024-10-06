package ex.han.backend.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import ex.han.backend.domain.user.User;
import ex.han.backend.domain.user.UserDTO;
import ex.han.backend.global.ResponseCode;
import ex.han.backend.global.ResponseDTO;
import ex.han.backend.global.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserStatusResponseDTO extends ResponseDTO {

    private UserDTO user;

    public UserStatusResponseDTO(User user) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.user = User.mapToDTO(user);
    }

    public static ResponseEntity<ResponseDTO> success(User user) {
        ResponseDTO responseBody = new UserStatusResponseDTO(user);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> userDeactivated() {
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.USER_DEACTIVATED, ResponseMessage.USER_DEACTIVATED);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

}
