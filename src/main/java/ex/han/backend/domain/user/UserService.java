package ex.han.backend.domain.user;



import ex.han.backend.domain.dto.request.UserNicknameDuplicateRequestDTO;
import ex.han.backend.domain.dto.request.UserRequiredInfoRequestDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;


public interface UserService {
    boolean userNicknameDuplicateCheck(UserNicknameDuplicateRequestDTO userNicknameDuplicateRequestDTO);
    User loadUserStatus(Long userId, UserDetails userDetails);
    List<String> userAdditionalInfoCheck();

    void userWithdraw(UserDetails userDetails);

    boolean patchUserRequiredInfo(UserRequiredInfoRequestDTO userInfo);

//    List<ReviewResponseDTO> getMyReviews(String username);

//    void updateMyReview(Long reviewId, ReviewUpdateRequestDTO reviewUpdateRequestDTO, String username);

//    void deleteMyReview(Long reviewId, String username);

    boolean isUserNicknameDuplicated(String nickname);

    void recoverData(User user);
}
