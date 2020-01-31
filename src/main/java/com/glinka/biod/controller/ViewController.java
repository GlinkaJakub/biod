package com.glinka.biod.controller;

import com.glinka.biod.dto.NoteDto;
import com.glinka.biod.dto.UsersDto;
import com.glinka.biod.dto.UsersNewPasswordDto;
import com.glinka.biod.dto.UsersRegisterDto;
import com.glinka.biod.entity.Note;
import com.glinka.biod.entity.Users;
import com.glinka.biod.service.NoteService;
import com.glinka.biod.service.UserService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
class ViewController {

    private final UserService userService;
    private final NoteService noteService;

    public ViewController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
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
    public String register(@Valid @ModelAttribute("user") UsersRegisterDto users, BindingResult bindingResult) {
        String PATTERN = "^((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,})";

        if(userService.findByUsername(users.getUsername()) != null){
            FieldError existUser = new FieldError("user", "username", "User exist");
            bindingResult.addError(existUser);
        }

        if (!users.getPassword().equals(users.getConfirmPassword())){
            FieldError samePass = new FieldError("user", "confirmPassword", "Passwords must be the same");
            bindingResult.addError(samePass);
        }

        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(users.getPassword());
        if(!matcher.matches()){
            FieldError newPass = new FieldError("user", "password", "Password must be contain at least 1 upper-case character, 1 lower-case character and 1 digit");
            bindingResult.addError(newPass);
        }

        if (bindingResult.hasErrors()) {
            return "register";
        } else {
            userService.register(users);
            return "redirect:/login.html";
        }
    }

    @GetMapping("/changepass.html")
    public String changepass(Model model){
        UsersNewPasswordDto usersNewPasswordDto = new UsersNewPasswordDto();
        model.addAttribute("updateUser", usersNewPasswordDto);

        return "changepass";
    }

    @Transactional
    @PostMapping("/changepass")
    public String changepass(@Valid @ModelAttribute("updateUser") UsersNewPasswordDto users, BindingResult bindingResult, Authentication authentication){
        String username = authentication.getName();
        String pass = userService.findByUsername(username).getPassword();
        String PATTERN = "^((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,})";

        if(!BCrypt.checkpw(users.getOldPassword(), pass)){
            FieldError existUser = new FieldError("updateUser", "oldPassword", "Wrong old password");
            bindingResult.addError(existUser);
        }

        if (!users.getNewPassword().equals(users.getConfirmPassword())){
            FieldError samePass = new FieldError("updateUser", "confirmPassword", "Passwords must be the same");
            bindingResult.addError(samePass);
        }

        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(users.getNewPassword());
        if(!matcher.matches()){
            FieldError samePass = new FieldError("updateUser", "newPassword", "Password must be contain at least 1 upper-case character, 1 lower-case character and 1 digit");
            bindingResult.addError(samePass);
        }

        if (bindingResult.hasErrors()) {
            return "changepass";
        } else {
            users.setUsername(username);
            userService.changePassword(users);
            return "redirect:/main.html";
        }
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

        if (note.getText() == null || note.getTitle() == null){
            return "redirect:/main.html";
        }

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
