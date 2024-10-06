package ex.han.backend.domain.controller;

import ex.han.backend.domain.dto.request.UserNicknameDuplicateRequestDTO;
import ex.han.backend.domain.dto.request.UserRequiredInfoRequestDTO;
import ex.han.backend.domain.dto.request.UserStatusRequestDTO;
import ex.han.backend.domain.dto.response.UserAdditionalInfoResponseDTO;
import ex.han.backend.domain.dto.response.UserNicknameDuplicateResponseDTO;
import ex.han.backend.domain.dto.response.UserStatusResponseDTO;
import ex.han.backend.domain.user.User;
import ex.han.backend.domain.user.UserService;
import ex.han.backend.global.ResponseDTO;
import ex.han.backend.global.exception.AuthenticationException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RequestMapping("/api/user")
@RestController
@Tag(name = "User", description = "user APIs")
public class UserController {

    private final UserService userService;

    @PostMapping("/nickname-check")
    public ResponseEntity<? super UserNicknameDuplicateResponseDTO> userNicknameDuplicateCheck(@RequestBody @Valid UserNicknameDuplicateRequestDTO userNicknameDuplicateDTO){

        boolean rst = userService.userNicknameDuplicateCheck(userNicknameDuplicateDTO);

        return UserNicknameDuplicateResponseDTO.success(rst);

    }
    @PostMapping("/info")
    public ResponseEntity<? super UserStatusResponseDTO> loadUserStatus(@RequestBody UserStatusRequestDTO userStatusRequest, @AuthenticationPrincipal UserDetails userDetails){

        Long userId = userStatusRequest.getUserId();
        User user = userService.loadUserStatus(userId, userDetails);
        if(user.getDeactivationDate() != null){
            return UserStatusResponseDTO.userDeactivated();
        }

        return UserStatusResponseDTO.success(user);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/withdraw")
    public ResponseEntity<? super ResponseDTO> userWithdraw(@AuthenticationPrincipal UserDetails userDetails){
        if(userDetails == null){
            throw new AuthenticationException("not authenticated");
        }

        userService.userWithdraw(userDetails);


        return ResponseDTO.success();
    }

    /**
     * 필수로 입력해야 하는데 누락된 유저 정보를 리스트로 반환
     */
    @GetMapping("additional-info-check")
    public ResponseEntity<? super UserAdditionalInfoResponseDTO> userAdditionalInfoCheck(){

        List<String> rst = userService.userAdditionalInfoCheck();

        return UserAdditionalInfoResponseDTO.success(rst);
    }

    /**
     * 회원 가입 미기입 정보(성별, 생년월일, 닉네임, 프로필 이미지 s3 key) 입력
     * 기존 정보 변경시에도 이 api 사용
     */
    @PatchMapping("/required-info")
    public ResponseEntity<ResponseDTO> patchUserRequiredInfo(
            @RequestBody UserRequiredInfoRequestDTO userInfo) {
        boolean isChanged = userService.patchUserRequiredInfo(userInfo);
        if (!isChanged) {
            return ResponseDTO.notModified();
        }
        return ResponseDTO.success();
    }

//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/review")
//    public ResponseEntity<? super MyReviewGetResponseDTO> getMyReviews(@AuthenticationPrincipal UserDetails userDetails){
//        List<ReviewResponseDTO> reviewResponseDTOList = userService.getMyReviews(userDetails.getUsername());
//
//        return MyReviewGetResponseDTO.success(reviewResponseDTOList);
//    }

//    @PreAuthorize("isAuthenticated()")
//    @PatchMapping("/review/{reviewId}")
//    public ResponseEntity<? super ResponseDTO> updateReview(@PathVariable(value = "reviewId")Long reviewId, @RequestBody ReviewUpdateRequestDTO reviewUpdateRequestDTO, @AuthenticationPrincipal UserDetails userDetails){
//        userService.updateMyReview(reviewId, reviewUpdateRequestDTO, userDetails.getUsername());
//        return ResponseDTO.success();
//    }

//    @PreAuthorize("isAuthenticated()")
//    @DeleteMapping("/review/{reviewId}")
//    public ResponseEntity<? super ResponseDTO> deleteReview(@PathVariable(value = "reviewId")Long reviewId, @AuthenticationPrincipal UserDetails userDetails){
//        userService.deleteMyReview(reviewId, userDetails.getUsername());
//        return ResponseDTO.success();
//    }
}
