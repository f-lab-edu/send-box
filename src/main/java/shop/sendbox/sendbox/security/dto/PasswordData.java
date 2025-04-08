package shop.sendbox.sendbox.security.dto;

public record PasswordData(String hashedPassword, String salt) {
}