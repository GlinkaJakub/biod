package com.glinka.biod.service;

import com.glinka.biod.converter.ConverterAdapter;
import com.glinka.biod.dto.NoteDto;
import com.glinka.biod.entity.Note;
import com.glinka.biod.entity.Users;
import com.glinka.biod.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserService userService;

    private final ConverterAdapter<Note, NoteDto> noteDtoToNoteConverter;
    private final ConverterAdapter<NoteDto, Note> noteToNoteDtoConverter;

    public NoteServiceImpl(NoteRepository noteRepository, UserService userService, ConverterAdapter<Note, NoteDto> noteDtoToNoteConverter, ConverterAdapter<NoteDto, Note> noteToNoteDtoConverter) {
        this.noteRepository = noteRepository;
        this.userService = userService;
        this.noteDtoToNoteConverter = noteDtoToNoteConverter;
        this.noteToNoteDtoConverter = noteToNoteDtoConverter;
    }

    @Override
    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    @Override
    public List<NoteDto> findAllByUser(String user) {
        return noteToNoteDtoConverter.convertToList(noteRepository.findAllByAuthor(user));
    }

    @Override
    public List<NoteDto> findAllByAccess(List<Users> access) {
        return noteToNoteDtoConverter.convertToList(noteRepository.findAllByAccessInAndCommon(access, false));
    }

    @Override
    public List<NoteDto> findAllByPublic(boolean isPublic) {
        return noteToNoteDtoConverter.convertToList(noteRepository.findAllByCommon(isPublic));
    }

    @Override
    public Note findById(Long noteId) {
        return noteRepository.findById(noteId).orElse(null);
    }

    @Override
    public Note addUsersToNote(Long noteId, Users users) {
        Note note = findById(noteId);
        List<Users> userList = note.getAccess();
        userList.add(users);
        note.setAccess(userList);
        return updateNote(note);
    }

//    @Override
//    public Note saveNote(Note note, String username) {
//        note.setAuthor(username);
//        return noteRepository.saveAndFlush(note);
//    }

    @Override
    public Note updateNote(Note note) {
        return noteRepository.saveAndFlush(note);
    }

    @Override
    public Note saveNoteDto(NoteDto noteDto, String username) {
        //TODO
        noteDto.setAuthor(username);
        Note note = noteDtoToNoteConverter.convert(noteDto);
        Users user = userService.findByUsername(username);
//        addUsersToNote(note.getId(), user);
        List<Users> accessUser = new ArrayList<>();
        accessUser.add(user);
        note.setAccess(accessUser);
        return noteRepository.saveAndFlush(note);
    }

    @Override
    public boolean deleteNote(Note note) {
        noteRepository.delete(note);
        return false;
    }
}
