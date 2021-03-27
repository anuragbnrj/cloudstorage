package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

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
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        credential.setKey(encodedSalt);
        encryptPasswordInCredential(credential);
        return credentialMapper.createCredential(credential);
    }

    public int updateCredential(Credential credential) {
        encryptPasswordInCredential(credential);
        return credentialMapper.updateCredential(credential);
    }

    public int deleteCredentialById(Long credentialId) {
        return credentialMapper.deleteCredentialById(credentialId);
    }

    private void encryptPasswordInCredential(Credential credential) {
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), credential.getKey()));
    }

}