package com.virtualdrive.services;

import com.virtualdrive.mapper.NoteMapper;
import com.virtualdrive.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int createNote(Note note, Integer userId) {
        return noteMapper.insert(new Note(null, note.getNoteTitle(), note.getNoteDescription(), userId));
    }

    public int updateNote(Note note) {
        return noteMapper.update(note);
    }

    public List<Note> getAllNotes(Integer userId) {
        return noteMapper.findAll(userId);
    }

    public void delete(Integer noteId) {
        noteMapper.deleteById(noteId);
    }

    public Note findById(Integer id) {
        return noteMapper.findById(id);
    }

}
