package org.raniu.user.service;

public interface RSAService {
    String getPublicKey();

    String decrypt(String data) throws Exception;
}
