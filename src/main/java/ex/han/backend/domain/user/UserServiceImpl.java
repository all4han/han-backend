package ex.han.backend.domain.user;


import ex.han.backend.domain.dto.request.UserNicknameDuplicateRequestDTO;
import ex.han.backend.domain.dto.request.UserRequiredInfoRequestDTO;
import ex.han.backend.global.exception.NickNameDuplicatedException;
import ex.han.backend.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Override
    public boolean userNicknameDuplicateCheck(UserNicknameDuplicateRequestDTO userNicknameDuplicateRequestDTO) {
        return isUserNicknameDuplicated(userNicknameDuplicateRequestDTO.getNickname());

    }
    /**
     * userID가 null인 경우 자기 정보 조회, 타인 정보인 경우 userId, profile_s3_key, nickname만 노출
     */
    @Override
    public User loadUserStatus(Long userId, UserDetails userDetails) {
        User user;
        log.info("userId:{}", userId);
        if(userId == null){
            user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                    () -> new ResourceNotFoundException("no active user :" + userDetails.getUsername())
            );
        }
        else{
            user = userRepository.findUserByUserIdAndDeactivationDateIsNull(userId).orElseThrow(
                    () -> new ResourceNotFoundException("no active user :" + userDetails.getUsername())
            );
            user.setEmail(null);
            user.setGender(null);
            user.setBirthdate(null);
            user.setOauthType(null);
            user.setSignupDate(null);
        }
        return user;
    }
    @Transactional
    @Override
    public void userWithdraw(UserDetails userDetails) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findUserByEmailAndDeactivationDateIsNull(userDetails.getUsername());

        User user = foundUser.orElseThrow(() -> new ResourceNotFoundException("no active user :" + userDetails.getUsername()));

        user.setDeactivationDate(LocalDateTime.now());

        userRepository.save(user);
    }
    @Override
    public List<String> userAdditionalInfoCheck() {
        String userEmail = findUserEmailFromJwt();
        User user = userRepository.findUserByEmailAndDeactivationDateIsNull(userEmail).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + userEmail)
        );
        List<String> requireList = new ArrayList<>();
        if(user.getBirthdate() == null){
            requireList.add("birthdate");
        }
        if(user.getGender() == null){
            requireList.add("gender");
        }

        return requireList;
    }

    @Transactional
    @Override
    public boolean patchUserRequiredInfo(UserRequiredInfoRequestDTO userInfo) {

        log.info("Received User Info: {}", userInfo);
        String userEmail = findUserEmailFromJwt();
        User user = userRepository.findUserByEmailAndDeactivationDateIsNull(userEmail).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + userEmail)
        );

        boolean result = false;

        if (userInfo.getGender() != null) {
            user.setGender(userInfo.getGender());
            result = true;
        }
        if (userInfo.getBirthdate() != null) {
            user.setBirthdate(userInfo.getBirthdate());
            result = true;
        }
        if (userInfo.getNickname() != null) {
            if(isUserNicknameDuplicated(userInfo.getNickname())){
                throw new NickNameDuplicatedException("Nickname Duplicated.");
            }
            user.setNickname(userInfo.getNickname());
            result = true;
        }
        if (userInfo.getProfileS3Key() != null) {
            String old = user.getProfileS3Key();
            log.info("old one:{}", old);
            user.setProfileS3Key(userInfo.getProfileS3Key()); // 프로필 이미지 URL 저장
            if(old != null) s3Service.deleteImageFromS3(old);
            result = true;
        }

        if (result) {
            userRepository.save(user);
        }
        return result;
    }
    /**
     * SecurityContextHolder에 저장된 유저 정보 추출
     * @return
     */
    private static String findUserEmailFromJwt() {
        String userEmail = "default@default.com";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            userEmail = authentication.getName();
        }
        return userEmail;
    }
    @Override
    public boolean isUserNicknameDuplicated(String nickname){
        log.info("nickname:{}", nickname);
        log.info("duplicated?:{}", userRepository.existsByNicknameNative(nickname));
        return userRepository.existsByNicknameNative(nickname);
    }

    @Override
    public void recoverData(User user) {
        user.setDeactivationDate(null);
        user.setSignupDate(LocalDateTime.now());
        // TODO 다른 테아블도 복구
    }
}
