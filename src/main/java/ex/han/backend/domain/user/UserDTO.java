package ex.han.backend.domain.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long userId;
    private String email;
    private Character gender;
    private LocalDate birthdate;
    private String nickname;
    private String oauthType;
    private String profileS3Key;
    private LocalDateTime signupDate;
}