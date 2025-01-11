package com.sonu.creditCard.Entity;

public class EncryptionResponse {
    private String encryptedDetails;
    private String encryptedAESKey;

    // Constructor
    public EncryptionResponse (String encryptedDetails, String encryptedAESKey) {
        this.encryptedDetails = encryptedDetails;
        this.encryptedAESKey = encryptedAESKey;
    }

    // Getters and Setters
    public String getEncryptedDetails() {
        return encryptedDetails;
    }

    public void setEncryptedDetails(String encryptedDetails) {
        this.encryptedDetails = encryptedDetails;
    }

    public String getEncryptedAESKey() {
        return encryptedAESKey;
    }

    public void setEncryptedAESKey(String encryptedAESKey) {
        this.encryptedAESKey = encryptedAESKey;
    }
}
