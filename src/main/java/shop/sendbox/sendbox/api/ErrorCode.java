package shop.sendbox.sendbox.api;

public enum ErrorCode {

	UNAUTHORIZED(401, "AUTH_001", "UNAUTHORIZED", "이 리소스에 접근하기 위해서는 인증이 필요합니다."),
	MISSING_PRINCIPAL(401, "AUTH_002", "UNAUTHORIZED", "사용자 정보가 없습니다."),
	ACCESS_DENIED(403, "AUTH_003", "ACCESS_DENIED", "이 리소스에 접근할 권한이 없습니다."),

	SERVER_ERROR(500, "SYS_001", "SERVER_ERROR", "예기치 않은 오류가 발생했습니다. 나중에 다시 시도해 주세요."),
	BAD_REQUEST(400, "SYS_002", "BAD_REQUEST", "잘못된 요청입니다."),
	INVALID_INPUT_VALUE(400, "SYS_003", "BAD_REQUEST", "입력값이 올바르지 않습니다.");

	private final int statusCode;
	private final String errorCode;
	private final String message;
	private final String defaultDetail;

	ErrorCode(int statusCode, String errorCode, String message, String defaultDetail) {
		this.statusCode = statusCode;
		this.errorCode = errorCode;
		this.message = message;
		this.defaultDetail = defaultDetail;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}

	public String getDefaultDetail() {
		return defaultDetail;
	}
}
