package shop.sendbox.sendbox.buyer.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeleteStatusTest {

	@Test
	@DisplayName("삭제 상태가 Y이면 삭제된 상태이다.")
	void isDeleted() {
		// given
		DeleteStatus deleteStatus = DeleteStatus.Y;

		// when
		boolean isDeleted = deleteStatus.isDeleted();

		// then
		Assertions.assertThat(isDeleted).isTrue();
	}

	@Test
	@DisplayName("삭제 상태가 N이면 삭제되지 않은 상태이다.")
	void isNotDeleted() {
		// given
		DeleteStatus deleteStatus = DeleteStatus.N;

		// when
		boolean isDeleted = deleteStatus.isDeleted();

		// then
		Assertions.assertThat(isDeleted).isFalse();
	}
}