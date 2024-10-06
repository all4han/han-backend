package ex.han.backend.domain.dto.request;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class UserRequiredInfoRequestDTO {

    @Nullable
    private Character gender;
    @Nullable
    private LocalDate birthdate;
    @Nullable
    private String nickname;
    @Nullable
    private String profileS3Key;
}
