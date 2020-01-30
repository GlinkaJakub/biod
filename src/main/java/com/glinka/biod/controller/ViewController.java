package com.glinka.biod.controller;

import com.glinka.biod.dto.NoteDto;
import com.glinka.biod.dto.UsersDto;
import com.glinka.biod.dto.UsersRegisterDto;
import com.glinka.biod.entity.Note;
import com.glinka.biod.entity.Users;
import com.glinka.biod.service.NoteService;
import com.glinka.biod.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Controller
class ViewController {

    private final UserService userService;
    private final NoteService noteService;

    public ViewController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @GetMapping("/login.html")
    public String login(){
        return "login";
    }

    @GetMapping("/register.html")
    public String register(Model model){

        UsersRegisterDto usersRegisterDto = new UsersRegisterDto();
        model.addAttribute("user", usersRegisterDto);

        return "register";
    }

    @Transactional
    @PostMapping("/register")
    public String register(@ModelAttribute("user") UsersRegisterDto users){
        userService.register(users);
        return "redirect:/login.html";
    }

    @GetMapping("/main.html")
    public String main(Model model, Authentication authentication){
        String username = authentication.getName();
        Users activUser = userService.findByUsername(username);
//        Long userId = userService.findByUsername(username).getId();

//      new Note
        NoteDto noteDto = new NoteDto();
        model.addAttribute("note", noteDto);
//      author's notes
        List<NoteDto> authorNotes = noteService.findAllByUser(username);
        model.addAttribute("authorNote", authorNotes);
//      private notes
        List<Users> users = new ArrayList<>();
        users.add(activUser);
        List<NoteDto> privateNotes = noteService.findAllByAccess(users);
        model.addAttribute("privateNote", privateNotes);
//      public notes
        List<NoteDto> publicNotes = noteService.findAllByPublic(true);
        model.addAttribute("publicNote", publicNotes);
//      add users to note
        List<UsersDto> usersList = userService.findAllUsers();
        model.addAttribute("users", usersList);

        UsersDto user = new UsersDto();
        model.addAttribute("addUser", user);

        return "main";
    }

    @Transactional
    @PostMapping("/addNote")
    public String addNote(@ModelAttribute("note") NoteDto note, Authentication authentication){
        String username = authentication.getName();
        noteService.saveNoteDto(note, username);
        return "redirect:/main.html";
    }

    @GetMapping("/deleteNote")
    public String deleteNote(@ModelAttribute("note") Long noteId){
        Note note = noteService.findById(noteId);
        noteService.deleteNote(note);
        return "redirect:/main.html";
    }

    @Transactional
    @PostMapping("/addUserToNote")
    public String addUserToNote(@ModelAttribute("addUser") UsersDto usersDto, @RequestParam("note") Long noteId){
        Users users = userService.findByUsername(usersDto.getUsername());
        noteService.addUsersToNote(noteId, users);
        return "redirect:/main.html";
    }

}
