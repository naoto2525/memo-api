package com.example.memoapi.service;

import com.example.memoapi.dto.Note;
import com.example.memoapi.exception.NoteArgumentNotValidException;
import com.example.memoapi.exception.NoteNotFoundException;
import com.example.memoapi.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    public Note saveNote(Note note){
        //title が空の場合のバリデーション
        if(note.getTitle() == null || note.getTitle().isEmpty()){
            throw new NoteArgumentNotValidException("title is empty");
        }
        // content が空の場合のバリデーション
        if(note.getContent() == null || note.getContent().isEmpty()){
            throw new NoteArgumentNotValidException("content is empty");
        }

        noteRepository.insertNote(note);
        return note;
    }

    public Note findById(long id){
        Note note = noteRepository.findById(id);

        if(note == null){
            throw new NoteNotFoundException(id);
        }

        return note;
    }
    public boolean delete(long id){
        if(!noteRepository.delete(id)){
            throw new NoteNotFoundException(id);
        }
        return true;
    }
    @Transactional
    public Note update(Note note, long id){

        //title が空の場合のバリデーション
        if(note.getTitle() == null || note.getTitle().isEmpty()){
            throw new NoteArgumentNotValidException("title is empty");
        }
        // content が空の場合のバリデーション
        if(note.getContent() == null || note.getContent().isEmpty()){
            throw new NoteArgumentNotValidException("content is empty");
        }

        //指定されたidが存在しない場合のバリデーション
        Note targetNote = noteRepository.findById(id);
        if(targetNote == null){
            throw new NoteNotFoundException(id);
        }
        note.setId(id);
        note.setCreatedAt(targetNote.getCreatedAt());
        return noteRepository.update(note);

    }
}
