package com.virtualdrive.controller;

import com.virtualdrive.model.Note;
import com.virtualdrive.services.NoteService;
import com.virtualdrive.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {

    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/notes")
    public String createNote(Authentication authentication, @ModelAttribute("note") Note note, RedirectAttributes redirectAttributes) {
        try {
            Integer userId = userService.getUser(authentication.getName()).getUserId();

            Note noteObject = noteService.findById(note.getNoteId());

            int rowsAffected;
            if (noteObject == null) {
                rowsAffected = noteService.createNote(note, userId);
            } else {
                rowsAffected = noteService.updateNote(note);
            }

            if (rowsAffected > 0) {
                redirectAttributes.addFlashAttribute("noteSuccess", true);
            } else {
                redirectAttributes.addFlashAttribute("noteError", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("noteError", true);
        }
        return "redirect:/result";
    }

    @GetMapping("/notes/delete/{id}")
    public String deleteNote(@PathVariable(value = "id") Integer noteId, RedirectAttributes redirectAttributes) {
        try {
            noteService.delete(noteId);
            redirectAttributes.addFlashAttribute("noteSuccess", true);
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("noteError", true);
        }
        return "redirect:/result";
    }

}
