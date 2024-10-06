package ex.han.backend.global.exception;

import ex.han.backend.global.ResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class, MultipartException.class, MissingServletRequestPartException.class})
    public ResponseEntity<ResponseDTO> handleValidationException(Exception e) {
        return ResponseDTO.validationFail();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ResponseDTO.databaseError();
    }

    @ExceptionHandler({AuthenticationException.class, ExpiredJwtException.class})
    public ResponseEntity<ResponseDTO> handleAuthenticationException(AuthenticationException e) {
        return ResponseDTO.authenticationFail();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResponseDTO> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return ResponseDTO.notFound();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseDTO.notFound();
    }

    @ExceptionHandler(NickNameDuplicatedException.class)
    public ResponseEntity<ResponseDTO> handleCommonException(RuntimeException e) {
        return ResponseDTO.conflict(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleGenericException(Exception e) {
        e.printStackTrace();
        return ResponseDTO.internalServerError();
    }
}
