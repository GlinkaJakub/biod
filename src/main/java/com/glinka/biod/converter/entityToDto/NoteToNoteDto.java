package com.glinka.biod.converter.entityToDto;

import com.glinka.biod.converter.ConverterAdapter;
import com.glinka.biod.dto.NoteDto;
import com.glinka.biod.entity.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteToNoteDto extends ConverterAdapter<NoteDto, Note> {

//    private final NoteService noteService;
//
//    public NoteToNoteDto(NoteService noteService) {
//        this.noteService = noteService;
//    }

    @Override
    public NoteDto convert(NoteDto target, Note source) {

        if(target == null || source == null)
            return null;

        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setText(source.getText());
        target.setAuthor(source.getAuthor());
        target.setCommon(source.isCommon());

        return target;
    }
}
