package com.glinka.biod.service;

import com.glinka.biod.dto.NoteDto;
import com.glinka.biod.entity.Note;
import com.glinka.biod.entity.Users;

import java.util.List;

public interface NoteService {

    List<Note> findAll();

    List<NoteDto> findAllByUser(String users);

    List<NoteDto> findAllByAccess(List<Users> access);

    List<NoteDto> findAllByPublic(boolean isPublic);

    Note findById(Long noteId);

    Note addUsersToNote(Long noteId, Users users);

    Note saveNoteDto(NoteDto note, String username);

    Note updateNote(Note note);

    boolean deleteNote(Note note);

}
