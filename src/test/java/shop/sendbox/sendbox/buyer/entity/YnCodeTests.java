package shop.sendbox.sendbox.buyer.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class YnCodeTests {

	@Test
	@DisplayName("Y는 삭제된 상태를 의미한다")
	void codeYIsDeleted() {
		// given
		YnCode ynCode = YnCode.Y;

		// when
		boolean result = ynCode.isDeleted();

		// then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("N은 삭제되지 않은 상태를 의미한다")
	void codeNIsNotDeleted() {
		// given
		YnCode ynCode = YnCode.N;

		// when
		boolean result = ynCode.isDeleted();

		// then
		assertThat(result).isFalse();
	}

}
