package shop.sendbox.sendbox.buyer.entity;

public enum DeleteStatus {
	Y, N;

	public boolean isDeleted() {
		return this == Y;
	}
}
