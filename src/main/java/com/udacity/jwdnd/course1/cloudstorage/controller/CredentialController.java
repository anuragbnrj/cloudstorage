package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/credentials")
public class CredentialController {

    private final CredentialService credentialService;
    private final UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping
    public String createOrUpdateCredential(Credential credential, Authentication authentication, Model model) {
        User user = userService.getUserByUsername((String) authentication.getPrincipal());
        Long userId = user.getUserId();

        int rowsUpdated = 0;
        Long credentialId = credential.getCredentialId();
        // check whether add or update request
        if (credentialId != null) {
            Credential existingCredential = credentialService.findCredentialById(credentialId);
            // check whether credential exists
            if (existingCredential == null) {
                model.addAttribute("result", "error");
                model.addAttribute("errorMessage", "ERROR: CANNOT UPDATE Credential with id " + credentialId + ". Credential does NOT exist!");
                return "result";
            }
            // check whether credential sent in request belongs to logged in user
            if (!existingCredential.getUserId().equals(userId)) {
                model.addAttribute("result", "error");
                model.addAttribute("errorMessage", "UNAUTHORIZED: Credential with id " + credentialId + " does NOT belong to you!");
                return "result";
            }
            log.info("Credential: {}", credential);
            credential.setKey(existingCredential.getKey());
            credential.setUserId(userId);
            rowsUpdated = credentialService.updateCredential(credential);
        } else {
            log.info("Credential: {}", credential);
            credential.setUserId(userId);
            rowsUpdated = credentialService.createCredential(credential);
        }

        if (rowsUpdated < 1) {
            model.addAttribute("result", "error");
            return "result";
        }

        model.addAttribute("result", "success");
        return "result";
    }

    @GetMapping("/deleteCredential/{credentialId}")
    public String deleteCredentialById(@PathVariable(name = "credentialId") Long credentialId, Authentication authentication, Model model) {
        User user = userService.getUserByUsername((String) authentication.getPrincipal());
        Long userId = user.getUserId();

        Credential existingCredential = credentialService.findCredentialById(credentialId);
        if (existingCredential == null) {
            model.addAttribute("result", "error");
            model.addAttribute("errorMessage", "ERROR: CANNOT DELETE Credential with id " + credentialId + ". Credential does NOT exist!");
            return "result";
        }
        if (!existingCredential.getUserId().equals(userId)) {
            model.addAttribute("result", "error");
            model.addAttribute("errorMessage", "UNAUTHORIZED: Credential with id " + credentialId + " does NOT belong to you!");
            return "result";
        }

        int rowsDeleted = credentialService.deleteCredentialById(credentialId);

        if (rowsDeleted < 1) {
            model.addAttribute("result", "error");
            return "result";
        }

        model.addAttribute("result", "success");
        return "result";
    }

}
