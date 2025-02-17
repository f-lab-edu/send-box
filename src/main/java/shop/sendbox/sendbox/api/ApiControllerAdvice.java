package shop.sendbox.sendbox.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
기본적으로 서버에서 발생한 예외는 모두 500 에러로 처리됩니다.
하지만 클라이언트가 잘못된 요청을 보낸 경우 400 에러로 처리를 해야합니다.
스프링 부트는 @RestControllerAdvice 애노테이션을 사용하여 JSON 응답 형태로 반환할 수 있도록 합니다.
 */
@RestControllerAdvice
public class ApiControllerAdvice {
	/*
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	예외 HTTP 상태 코드를 지정할 수 있습니다.

	@ExceptionHandler 애노테이션은 try-catch 와 유사하게 catch 할 예외를 지정합니다.
	그 예외에 대한 처리를 메소드 바드에서 처리할 수 있습니다.
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ApiResponse<Object> handleIllegalAccessException(IllegalArgumentException error) {
		return ApiResponse.of(HttpStatus.BAD_REQUEST, error.getMessage(), null);
	}
	/*
	예외가 발생한 원인에 대한 메세지를 클라이언트에게 전달하기 위해 ApiResponse 객체를 생성하여 반환합니다.
	 */
}
