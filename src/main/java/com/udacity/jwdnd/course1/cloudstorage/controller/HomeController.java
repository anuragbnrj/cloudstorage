package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = {"/", "/home"})
public class HomeController {

    private final UserService userService;
    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public HomeController(UserService userService,
                          FileService fileService,
                          NoteService noteService,
                          CredentialService credentialService,
                          EncryptionService encryptionService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public String homeView(Model model, Authentication authentication) {
        User user = userService.getUserByUsername((String) authentication.getPrincipal());
        Long userId = user.getUserId();

        List<File> fileList = fileService.findFilesByUserId(userId);
        model.addAttribute("fileList", fileList);

        List<Note> noteList = noteService.findNotesByUserId(userId);
        model.addAttribute("noteList", noteList);

        List<Credential> credentialList = credentialService.findCredentialsByUserId(userId);
        model.addAttribute("credentialList", credentialList);
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }

}
