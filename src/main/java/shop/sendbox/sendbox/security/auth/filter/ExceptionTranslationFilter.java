package shop.sendbox.sendbox.security.auth.filter;

import java.io.IOException;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import shop.sendbox.sendbox.api.ErrorResponse;
import shop.sendbox.sendbox.security.auth.exception.AuthenticationException;
import shop.sendbox.sendbox.security.auth.exception.AuthorizationException;

@RequiredArgsConstructor
public class ExceptionTranslationFilter implements Filter {

	private final ObjectMapper objectMapper;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException, ServletException {

		try {
			chain.doFilter(request, response);
		} catch (AuthenticationException e) {
			sendErrorResponse((HttpServletResponse)response, ErrorResponse.unauthorized());
		} catch (AuthorizationException e) {
			sendErrorResponse((HttpServletResponse)response, ErrorResponse.accessDenied());
		} catch (Exception e) {
			sendErrorResponse((HttpServletResponse)response, ErrorResponse.serverError());
		}
	}

	private void sendErrorResponse(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
		if (!response.isCommitted()) {
			response.setStatus(errorResponse.statusCode());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
		}
	}
}
