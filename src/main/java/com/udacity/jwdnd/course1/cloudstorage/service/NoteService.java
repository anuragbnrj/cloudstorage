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
        return noteMapper.createNote(note);
    }

    public int updateNote(Note note) {
        return noteMapper.updateNote(note);
    }

    public int deleteNoteById(Long noteId) {
        return noteMapper.deleteNoteById(noteId);
    }

}
