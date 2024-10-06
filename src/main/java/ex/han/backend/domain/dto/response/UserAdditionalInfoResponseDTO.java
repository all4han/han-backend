package ex.han.backend.domain.dto.response;


import ex.han.backend.global.ResponseDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class UserAdditionalInfoResponseDTO extends ResponseDTO {

    List<String> requireList;

    public UserAdditionalInfoResponseDTO(List<String> requireList){
        super();
        this.requireList = requireList;
    }

    public static ResponseEntity<ResponseDTO> success(List<String> requireList) {
        ResponseDTO responseBody = new UserAdditionalInfoResponseDTO(requireList);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
