package shop.sendbox.sendbox.api;

public record ErrorResponse(
	int statusCode,
	String message,
	String errorCode,
	String errorDetail
) {
	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(
			errorCode.getStatusCode(),
			errorCode.getMessage(),
			errorCode.getErrorCode(),
			errorCode.getDefaultDetail()
		);
	}

	public static ErrorResponse of(ErrorCode errorCode, String detail) {
		return new ErrorResponse(
			errorCode.getStatusCode(),
			errorCode.getMessage(),
			errorCode.getErrorCode(),
			detail
		);
	}

	public static ErrorResponse of(int status, String msg, String code, String detail) {
		return new ErrorResponse(status, msg, code, detail);
	}

	public static ErrorResponse unauthorized() {
		return of(ErrorCode.UNAUTHORIZED);
	}

	public static ErrorResponse missingPrincipal() {
		return of(ErrorCode.MISSING_PRINCIPAL);
	}

	public static ErrorResponse accessDenied() {
		return of(ErrorCode.ACCESS_DENIED);
	}

	public static ErrorResponse serverError() {
		return of(ErrorCode.SERVER_ERROR);
	}

	public static ErrorResponse badRequest() {
		return of(ErrorCode.BAD_REQUEST);
	}

	public static ErrorResponse badRequest(String detail) {
		return of(ErrorCode.BAD_REQUEST, detail);
	}
}
