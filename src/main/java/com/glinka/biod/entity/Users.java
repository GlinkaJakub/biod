package com.glinka.biod.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Users {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true, columnDefinition = "VARCHAR(128)")
    private String username;

    @NotNull
    @Column(columnDefinition = "VARCHAR(128)")
    private String password;

    @NotNull
    @Column(columnDefinition = "BOOLEAN")
    private boolean enabled;

//    @NotNull
//    @ManyToMany(mappedBy = "access")
//    private Set<Note> notes;

}
