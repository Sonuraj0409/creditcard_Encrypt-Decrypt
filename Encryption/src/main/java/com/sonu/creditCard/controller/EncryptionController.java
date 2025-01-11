package com.sonu.creditCard.controller;

import com.sonu.creditCard.Entity.EncryptionResponse;
import com.sonu.creditCard.Entity.CreditCardDetails;
import com.sonu.creditCard.service.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Map;

@RestController
@RequestMapping("/api/creditcard")
public class EncryptionController {

    @Autowired
    private EncryptionService encryptionService;

    // Endpoint for encryption
    @PostMapping("/encrypt")
    public EncryptionResponse encryptCreditCardDetails(@RequestBody CreditCardDetails cardDetails) throws Exception {
        String cardInfo = cardDetails.getCardDetails();

        // Retrieve the RSA key pair
        KeyPair keyPair = encryptionService.getOrCreateKeyPair();
        PublicKey publicKey = keyPair.getPublic();

        // Encrypt the credit card details using the service
        return encryptionService.encryptCreditCardDetails(cardInfo, publicKey);
    }

    // Endpoint for decryption
    @PostMapping("/decrypt")
    public String decryptCreditCardDetails(@RequestBody Map<String, String> payload) throws Exception {
        String encryptedCardDetails = payload.get("encryptedCardDetails");
        String encryptedAESKey = payload.get("encryptedAESKey");

        if (encryptedCardDetails == null || encryptedAESKey == null) {
            throw new IllegalArgumentException("Missing required parameters: encryptedCardDetails or encryptedAESKey");
        }

        // Retrieve the RSA key pair
        KeyPair keyPair = encryptionService.getOrCreateKeyPair();

        // Decrypt the credit card details using the service
        return encryptionService.decryptCreditCardDetails(encryptedCardDetails, encryptedAESKey, keyPair.getPrivate());
    }
    @GetMapping("/")
    public String redirectToSwagger() {
        return "redirect:/swagger-ui.html";
    }
}
