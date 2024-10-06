package ex.han.backend.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * User
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userRole;
    private String email;
    private String nickname;
    private Character gender;
    private LocalDate birthdate;
    private String oauthType;
    @Column(name = "profile_s3_key")
    private String profileS3Key;
    private LocalDateTime signupDate;
    private LocalDateTime deactivationDate;

    public static UserDTO mapToDTO(User user){
        return UserDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .gender(user.getGender())
                .birthdate(user.getBirthdate())
                .nickname(user.getNickname())
                .oauthType(user.getOauthType())
                .signupDate(user.getSignupDate())
                .build();
    }
}
