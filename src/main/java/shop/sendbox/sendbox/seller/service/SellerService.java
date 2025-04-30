package shop.sendbox.sendbox.seller.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.sendbox.sendbox.security.dto.PasswordData;
import shop.sendbox.sendbox.security.service.SecurityService;
import shop.sendbox.sendbox.seller.entity.Seller;
import shop.sendbox.sendbox.seller.repository.SellerRepository;

@Service
@RequiredArgsConstructor
public class SellerService {

	private final SellerRepository sellerRepository;
	private final SecurityService securityService;

	@Transactional
	public SellerCreateResult createSeller(SellerCreateCommand command) {
		validateBusinessNumberNotDuplicate(command);

		String encryptedPhoneNumber = securityService.encryptText(command.phoneNumber());
		String encryptedTaxEmail = securityService.encryptText(command.taxEmail());
		String encryptedBusinessNumber = securityService.encryptText(command.businessNumber());

		PasswordData passwordData = securityService.encryptPassword(command.password());

		Seller seller = Seller.create(passwordData.hashedPassword(), passwordData.salt(), command.name(),
			encryptedBusinessNumber, encryptedPhoneNumber, encryptedTaxEmail);

		Seller savedSeller = sellerRepository.save(seller);

		String decryptedPhoneNumber = securityService.decryptText(savedSeller.getPhoneNumber());
		String decryptedTaxEmail = securityService.decryptText(savedSeller.getTaxEmail());

		return SellerCreateResult.of(
			savedSeller, decryptedPhoneNumber, decryptedTaxEmail
		);
	}

	private void validateBusinessNumberNotDuplicate(SellerCreateCommand command) {
		String encryptedBusinessNumber = securityService.encryptText(command.businessNumber());
		if (sellerRepository.existsByBusinessNumber(encryptedBusinessNumber)) {
			throw new IllegalArgumentException("이미 등록된 사업자번호입니다.");
		}
	}
}
