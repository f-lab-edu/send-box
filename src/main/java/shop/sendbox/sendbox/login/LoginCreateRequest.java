package shop.sendbox.sendbox.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginCreateRequest(
    @NotBlank(message = "이메일은 필수 입력값입니다")
    @Email(message = "이메일 형식이 올바르지 않습니다")
    String email,
    
    @NotBlank(message = "비밀번호는 필수 입력값입니다")
    String password,
    
    @NotNull(message = "사용자 유형은 필수 입력값입니다") 
    UserType userType) {
    
    public LoginRequest toServiceRequest() {
        return new LoginRequest(email, password, userType);
    }
}
