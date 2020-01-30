package com.glinka.biod.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Authorities {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @OneToOne
    @JoinColumn(name="users")
    private Users users;

    @NotNull
    private String authority;

}
