package com.glinka.biod.converter.dtoToEntity;

import com.glinka.biod.converter.ConverterAdapter;
import com.glinka.biod.dto.NoteDto;
import com.glinka.biod.entity.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteDtoToNote extends ConverterAdapter<Note, NoteDto> {

    @Override
    public Note convert(Note target, NoteDto source) {

        if(target == null || source == null)
            return null;

        target.setText(source.getText());
        target.setTitle(source.getTitle());
        target.setAuthor(source.getAuthor());
        target.setCommon(source.isCommon());

        return target;
    }
}
