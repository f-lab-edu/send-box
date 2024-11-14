package shop.sendbox.sendbox.api;

import org.springframework.http.HttpStatus;

import lombok.Getter;

// Getter 으로 Jackson 라이브러리가 JSON 형태로 변환할 수 있도록 한다
@Getter
public class ApiResponse<T> {

	private final int statusCode;
	private final HttpStatus status;
	private final String message;
	private final T data;

	private ApiResponse(final HttpStatus status, final String message, final T data) {
		this.statusCode = status.value();
		this.data = data;
		this.status = status;
		this.message = message;
	}

	public static <T> ApiResponse<T> of(final HttpStatus status, final String message, final T data) {
		return new ApiResponse<>(status, message, data);
	}

	public static <T> ApiResponse<T> ok(final T data) {
		return new ApiResponse<>(HttpStatus.OK, HttpStatus.OK.name(), data);
	}

}
