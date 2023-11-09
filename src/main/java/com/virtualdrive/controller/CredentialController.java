package com.virtualdrive.controller;

import com.virtualdrive.model.Credential;
import com.virtualdrive.services.CredentialService;
import com.virtualdrive.services.EncryptionService;
import com.virtualdrive.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
public class CredentialController {
    private CredentialService credentialService;
    private UserService userService;
    private EncryptionService encryptionService;

    public CredentialController(CredentialService credentialService, UserService userService, EncryptionService encryptionService) {
        this.credentialService = credentialService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    @PostMapping("/credentials")
    public String createCredential(Authentication authentication, @ModelAttribute("credential") Credential credential, RedirectAttributes redirectAttributes) {
        try {
            Integer userId = userService.getUser(authentication.getName()).getUserId();

            setCredential(credential, userId);

            // If record doesn't exist, create new one otherwise update
            Credential credentialObject = credentialService.findById(credential.getCredentialId());
            if (credentialObject == null) {
                int count;
                count = credentialService.createCredential(credential);
                if (count > 0) {
                    redirectAttributes.addFlashAttribute("credentialSuccess", true);
                } else {
                    redirectAttributes.addFlashAttribute("credentialError", true);
                }
            } else {
                credentialService.update(credential);
                redirectAttributes.addFlashAttribute("credentialSuccess", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("credentialError", true);
        }
        return "redirect:/result";
    }

    private void setCredential(Credential credential, Integer userId) {
        // Add user id
        credential.setUserid(userId);

        // Generate random secured key
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        // Set encrypted key and encrypted password
        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);
    }

    @GetMapping("/credentials/delete/{id}")
    public String deleteCredential(@PathVariable(value = "id") Integer credentialId, RedirectAttributes redirectAttributes) {
        try {
            credentialService.delete(credentialId);
            redirectAttributes.addFlashAttribute("credentialSuccess", true);
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("credentialError", true);
        }
        return "redirect:/result";
    }

}
