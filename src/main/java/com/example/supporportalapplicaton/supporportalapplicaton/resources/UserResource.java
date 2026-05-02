package com.example.supporportalapplicaton.supporportalapplicaton.resources;

import com.example.supporportalapplicaton.supporportalapplicaton.domain.HttpResponse;
import com.example.supporportalapplicaton.supporportalapplicaton.exception.domain.EmailExistsException;
import com.example.supporportalapplicaton.supporportalapplicaton.exception.domain.ExceptionHandling;
import com.example.supporportalapplicaton.supporportalapplicaton.exception.domain.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping(path = {"/","/user"})
public class UserResource {

    @GetMapping("/home")
    public String show() throws UserNotFoundException {
        throw new UserNotFoundException("This email is already in taken");
    }

}
