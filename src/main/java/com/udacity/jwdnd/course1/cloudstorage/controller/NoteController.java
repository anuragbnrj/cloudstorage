package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
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
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping
    public String createOrUpdateNote(Note note, Authentication authentication, Model model) {
        User user = userService.getUserByUsername((String) authentication.getPrincipal());
        Long userId = user.getUserId();

        int rowsUpdated = 0;
        Long noteId = note.getNoteId();
        // check whether add or update request
        if (noteId != null) {
            Note existingNote = noteService.findNoteById(noteId);
            // check whether note exists
            if (existingNote == null) {
                model.addAttribute("result", "error");
                model.addAttribute("errorMessage", "CANNOT UPDATE Note with id " + noteId + ". Note does NOT exist!");
                return "result";
            }
            // check whether note sent in request belongs to logged in user
            if (!existingNote.getUserId().equals(userId)) {
                model.addAttribute("result", "error");
                model.addAttribute("errorMessage", "UNAUTHORIZED: Note with id " + noteId + " does NOT belong to you!");
                return "result";
            }
            note.setUserId(userId);
            rowsUpdated = noteService.updateNote(note);
        } else {
            note.setUserId(userId);
            rowsUpdated = noteService.createNote(note);
        }

        if (rowsUpdated < 1) {
            model.addAttribute("result", "error");
            if (rowsUpdated == -1) { // -1 is returned when note description exceeds 1000 characters
                model.addAttribute("errorMessage", "CANNOT ADD/UPDATE Note where description exceeds 1000 characters");
            }
            if (rowsUpdated == -2) {
                model.addAttribute("errorMessage", "CANNOT ADD/UPDATE Note. Note already available!!");
            }
            return "result";
        }

        model.addAttribute("result", "success");
        return "result";
    }

    @GetMapping("/deleteNote/{noteId}")
    public String deleteNoteById(@PathVariable(name = "noteId") Long noteId, Authentication authentication, Model model) {
        User user = userService.getUserByUsername((String) authentication.getPrincipal());
        Long userId = user.getUserId();

        Note existingNote = noteService.findNoteById(noteId);
        if (existingNote == null) {
            model.addAttribute("result", "error");
            model.addAttribute("errorMessage", "ERROR: CANNOT DELETE Note with id " + noteId + ". Note does NOT exist!");
            return "result";
        }
        if (!existingNote.getUserId().equals(userId)) {
            model.addAttribute("result", "error");
            model.addAttribute("errorMessage", "UNAUTHORIZED: Note with id " + noteId + " does NOT belong to you!");
            return "result";
        }

        int rowsDeleted = noteService.deleteNoteById(noteId);

        if (rowsDeleted < 1) {
            model.addAttribute("result", "error");
            return "result";
        }

        model.addAttribute("result", "success");
        return "result";
    }

}
