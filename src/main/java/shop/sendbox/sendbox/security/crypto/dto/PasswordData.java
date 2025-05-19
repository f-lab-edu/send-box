package shop.sendbox.sendbox.security.crypto.dto;

public record PasswordData(String hashedPassword, String salt) {
}
