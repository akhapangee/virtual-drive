package com.virtualdrive.controller;

import com.virtualdrive.model.Credential;
import com.virtualdrive.model.Note;
import com.virtualdrive.services.CredentialService;
import com.virtualdrive.services.FileUploadService;
import com.virtualdrive.services.NoteService;
import com.virtualdrive.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private FileUploadService fileUploadService;
    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;


    public HomeController(NoteService noteService, UserService userService, CredentialService credentialService, FileUploadService fileUploadService) {
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping("/home")
    public String getHomePage(Authentication authentication, Note note, Credential credential, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserId();
        model.addAttribute("files", fileUploadService.getAllFiles(userId));
        model.addAttribute("notes", noteService.getAllNotes(userId));
        model.addAttribute("credentials", this.credentialService.getAllCredentials(userId));
        return "home";
    }

    @GetMapping("/result")
    public String result(){
        return "result";
    }
}
