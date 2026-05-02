package com.example.supporportalapplicaton.supporportalapplicaton.service;

import com.example.supporportalapplicaton.supporportalapplicaton.domain.User;
import com.example.supporportalapplicaton.supporportalapplicaton.exception.domain.EmailExistsException;
import com.example.supporportalapplicaton.supporportalapplicaton.exception.domain.UserNameExistsException;
import com.example.supporportalapplicaton.supporportalapplicaton.exception.domain.UserNotFoundException;

import java.util.List;

public interface UserService {
    User register(String firstName, String lastName,String userName, String email) throws UserNotFoundException, UserNameExistsException, EmailExistsException;
    List<User> getUsers();
    User findUserByUsername(String username);
    User findUserByEmail(String email);
}
