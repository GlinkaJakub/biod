package com.glinka.biod.dto;

import lombok.Data;

@Data
public class NoteDto {

    private Long id;

    private String title;

    private String text;

    private String author;

    private boolean common;

}
