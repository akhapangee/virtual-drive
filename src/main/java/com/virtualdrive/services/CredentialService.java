package com.virtualdrive.services;

import com.virtualdrive.mapper.CredentialMapper;
import com.virtualdrive.model.Credential;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public int createCredential(Credential credential) {
        return credentialMapper.insert(credential);
    }

    public List<Credential> getAllCredentials(Integer userId) {
        return credentialMapper.getAllCredentials(userId)
                .stream()
                .map(credential -> {
                    credential.setPasswordDecrypted(encryptionService.decryptValue(credential.getPassword(), credential.getKey()));
                    return credential;
                }).collect(Collectors.toList());
    }

    public Credential findById(Integer id) {
        return credentialMapper.findById(id);
    }

    public void update(Credential credential) {
        credentialMapper.update(credential);
    }

    public void delete(Integer credentialId) {
        credentialMapper.delete(credentialId);
    }
}
