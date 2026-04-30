package com.example.supporportalapplicaton.supporportalapplicaton.resources;

import com.example.supporportalapplicaton.supporportalapplicaton.exception.domain.EmailExistsException;
import com.example.supporportalapplicaton.supporportalapplicaton.exception.domain.ExceptionHandling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserResource extends ExceptionHandling {

    @GetMapping("/home")
    public String show() throws EmailExistsException {
        throw new EmailExistsException("This email is already in taken");
    }
}
