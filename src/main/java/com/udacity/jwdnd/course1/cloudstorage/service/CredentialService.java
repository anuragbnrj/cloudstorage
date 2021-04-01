package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public Credential findCredentialById(Long credentialId) {
        return credentialMapper.findCredentialById(credentialId);
    }

    public List<Credential> findCredentialsByUserId(Long userId) {
        return credentialMapper.findCredentialsByUserId(userId);
    }

    public int createCredential(Credential credential) {
        int validateCredentialResult = validateCredential(credential);
        if (validateCredentialResult < 1) {
            return validateCredentialResult;
        }
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        credential.setKey(encodedSalt);
        encryptPasswordInCredential(credential);
        return credentialMapper.createCredential(credential);
    }

    public int updateCredential(Credential credential) {
        int validateCredentialResult = validateCredential(credential);
        if (validateCredentialResult < 1) {
            return validateCredentialResult;
        }
        encryptPasswordInCredential(credential);
        return credentialMapper.updateCredential(credential);
    }

    public int deleteCredentialById(Long credentialId) {
        return credentialMapper.deleteCredentialById(credentialId);
    }

    private void encryptPasswordInCredential(Credential credential) {
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), credential.getKey()));
    }

    private int validateCredential(Credential credential) {
        List<Credential> existingCredentialList = credentialMapper.findCredentialsByUserId(credential.getUserId());
        for (Credential existingCredential : existingCredentialList) {
            if (credential.getUrl().equals(existingCredential.getUrl())
                    && credential.getUsername().equals(existingCredential.getUsername())
            ) {
                return -1; // -1 is the code when url and username combo already exist for user
            }
        }
        return 1;
    }

}