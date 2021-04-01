package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NoteService {

    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public Note findNoteById(Long noteId) {
        return noteMapper.findNoteById(noteId);
    }

    public List<Note> findNotesByUserId(Long userId) {
        return noteMapper.findNotesByUserId(userId);
    }

    public int createNote(Note note) {
        int validateNoteResult = validateNote(note);
        if (validateNoteResult < 1) {
            return validateNoteResult;
        }
        return noteMapper.createNote(note);
    }

    public int updateNote(Note note) {
        int validateNoteResult = validateNote(note);
        if (validateNoteResult < 1) {
            return validateNoteResult;
        }
        return noteMapper.updateNote(note);
    }

    public int deleteNoteById(Long noteId) {
        return noteMapper.deleteNoteById(noteId);
    }

    private int validateNote(Note note) {
        if (note.getNoteDescription().length() > 1000) {
            return -1; // -1 is the code when description for note exceeds 1000 characters
        }
        List<Note> existingUserNoteList = noteMapper.findNotesByUserId(note.getUserId());
        for (Note existingUserNote : existingUserNoteList) {
            if (note.getNoteTitle().equals(existingUserNote.getNoteTitle()) &&
                    note.getNoteDescription().equals(existingUserNote.getNoteDescription())
            ) {
                return -2;
            }
        }
        return 1;
    }

}
