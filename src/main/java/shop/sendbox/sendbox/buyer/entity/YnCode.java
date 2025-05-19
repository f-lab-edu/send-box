package shop.sendbox.sendbox.buyer.entity;

public enum YnCode {
	Y, N;
	public boolean isDeleted() {
		return this == Y;
	}
}
