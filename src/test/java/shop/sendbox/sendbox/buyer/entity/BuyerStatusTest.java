package shop.sendbox.sendbox.buyer.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BuyerStatusTest {

	//create test code
	@Test
	void testBuyerStatus() {
		// given
		BuyerStatus buyerStatus = BuyerStatus.ACTIVE;

		// when
		String status = buyerStatus.name();

		// then
		assertEquals("ACTIVE", status);
	}
}
