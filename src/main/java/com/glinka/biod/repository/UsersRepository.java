package com.glinka.biod.repository;

import com.glinka.biod.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String username);

}