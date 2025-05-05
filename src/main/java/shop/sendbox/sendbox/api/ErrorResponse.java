package shop.sendbox.sendbox.api;

/**
 * 예외 발생 시 클라이언트에게 반환할 표준 에러 응답 형식
 */
public record ErrorResponse(
	int statusCode,
	String message,
	String errorCode,
	String errorDetail
) {
	public static ErrorResponse unauthorized() {
		return new ErrorResponse(
			401,
			"UNAUTHORIZED",
			"AUTH_001",
			"이 리소스에 접근하기 위해서는 인증이 필요합니다."
		);
	}

	public static ErrorResponse accessDenied() {
		return new ErrorResponse(
			403,
			"ACCESS_DENIED",
			"AUTH_003",
			"이 리소스에 접근할 권한이 없습니다."
		);
	}

	public static ErrorResponse serverError() {
		return new ErrorResponse(
			500,
			"SERVER_ERROR",
			"SYS_001",
			"예기치 않은 오류가 발생했습니다. 나중에 다시 시도해 주세요."
		);
	}

	public static ErrorResponse badRequest(String message) {
		return new ErrorResponse(
			400,
			"BAD_REQUEST",
			"SYS_002",
			message
		);
	}

	public static ErrorResponse of(int statusCode, String message, String errorCode, String errorDetail) {
		return new ErrorResponse(statusCode, message, errorCode, errorDetail);
	}
}
