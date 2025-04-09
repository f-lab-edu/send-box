package shop.sendbox.sendbox.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuditorBaseAwareImpl implements AuditorAware<Long> {

	@Override
	public Optional<Long> getCurrentAuditor() {
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

		if (attributes != null) {
			HttpServletRequest request = attributes.getRequest();
			String userId = request.getHeader("X-User-Id");
			try {
				return Optional.of(Long.parseLong(userId));
			} catch (NumberFormatException e) {
				// 로그 추가 (필요 시)
				log.warn("X-User-Id 가 올바르지 않습니다.", e);
				return Optional.empty();
			}
		}
		return Optional.empty();
	}
}
