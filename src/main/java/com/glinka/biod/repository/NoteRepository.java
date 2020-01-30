package com.glinka.biod.repository;

import com.glinka.biod.entity.Note;
import com.glinka.biod.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findAllByAuthor(String user);

    List<Note> findAllByAccessInAndCommon(List<Users> user, boolean isPublic);

    List<Note> findAllByCommon(boolean isPublic);

}
