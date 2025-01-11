package com.sonu.creditCard.service;

import com.sonu.creditCard.Entity.EncryptionResponse;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Service
public class EncryptionService {
    private KeyPair keyPair;

    // Generate and store RSA key pair securely
    public KeyPair getOrCreateKeyPair() throws Exception {
        if (keyPair == null) {
            keyPair = generateRSAKeyPair();
        }
        return keyPair;
    }

    // Generate RSA key pair
    public KeyPair generateRSAKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // RSA key size
        return keyPairGenerator.generateKeyPair();
    }

    // Encrypt credit card details using AES and RSA
    public EncryptionResponse encryptCreditCardDetails(String cardInfo, PublicKey publicKey) throws Exception {
        // Step 1: Generate AES key
        SecretKey aesKey = generateAESKey();

        // Step 2: Encrypt the credit card details using AES
        String encryptedCardDetails = encryptAES(cardInfo, aesKey);

        // Step 3: Encrypt the AES key using RSA
        String encryptedAESKey = encryptRSA(aesKey, publicKey);

        // Step 4: Return the encrypted details and AES key in a response
        return new EncryptionResponse(encryptedCardDetails, encryptedAESKey);
    }

    // Generate AES key for encryption
    private SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); // AES 128-bit encryption
        return keyGenerator.generateKey();
    }

    // Encrypt data using AES
    private String encryptAES(String data, SecretKey aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData); // Return Base64 encoded encrypted data
    }

    // Encrypt AES key using RSA
    private String encryptRSA(SecretKey aesKey, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedAESKey = cipher.doFinal(aesKey.getEncoded()); // Encrypt the AES key
        return Base64.getEncoder().encodeToString(encryptedAESKey); // Return Base64 encoded encrypted AES key
    }

    // Decrypt credit card details using AES and RSA
    public String decryptCreditCardDetails(String encryptedCardDetails, String encryptedAESKey, PrivateKey privateKey) throws Exception {
        // Step 1: Decrypt the AES key using RSA
        SecretKey aesKey = decryptRSA(encryptedAESKey, privateKey);

        // Step 2: Decrypt the credit card details using AES
        return decryptAES(encryptedCardDetails, aesKey);
    }

    // Decrypt AES key using RSA
    private SecretKey decryptRSA(String encryptedAESKey, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedAESKey = cipher.doFinal(Base64.getDecoder().decode(encryptedAESKey));
        return new SecretKeySpec(decryptedAESKey, "AES"); // Return the decrypted AES key
    }

    // Decrypt data using AES
    private String decryptAES(String encryptedData, SecretKey aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedData); // Return decrypted data as a string
    }
}
