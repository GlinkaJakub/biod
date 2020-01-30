package com.glinka.biod.entity;

import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
public class Note {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(columnDefinition = "VARCHAR(256)")
    private String title;

    @NotNull
    @Column(columnDefinition = "VARCHAR(1024)")
    private String text;

    @NotNull
    @Column(columnDefinition = "VARCHAR(128)")
    private String author;

    @NotNull
    @Column(columnDefinition = "BOOLEAN")
    private boolean common;

    @Cascade({CascadeType.SAVE_UPDATE})
    @ManyToMany
    @JoinTable(
            name = "users_access",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Users> access;

}
