package com.example.supporportalapplicaton.supporportalapplicaton.resources;

import com.example.supporportalapplicaton.supporportalapplicaton.domain.User;
import com.example.supporportalapplicaton.supporportalapplicaton.exception.domain.EmailExistsException;
import com.example.supporportalapplicaton.supporportalapplicaton.exception.domain.UserNameExistsException;
import com.example.supporportalapplicaton.supporportalapplicaton.exception.domain.UserNotFoundException;
import com.example.supporportalapplicaton.supporportalapplicaton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = {"/", "/user"})
public class UserResource {

    UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String show() throws UserNotFoundException {
        throw new UserNotFoundException("This email is already in taken");
    }

    @PostMapping("register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, UserNameExistsException, EmailExistsException {
       User loginUser =  userService.register(user.getFirstName(),user.getLastName(),user.getUsername(),user.getEmail());
       return new ResponseEntity<>(loginUser, HttpStatus.CREATED);
    }

}
