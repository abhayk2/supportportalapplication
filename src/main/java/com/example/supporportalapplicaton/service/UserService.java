package com.example.supporportalapplicaton.service;

import com.example.supporportalapplicaton.domain.User;
import com.example.supporportalapplicaton.exception.domain.EmailExistsException;
import com.example.supporportalapplicaton.exception.domain.UserNameExistsException;
import com.example.supporportalapplicaton.exception.domain.UserNotFoundException;

import java.util.List;

public interface UserService {
    User register(String firstName, String lastName,String userName, String email) throws UserNotFoundException, UserNameExistsException, EmailExistsException;
    List<User> getUsers();
    User findUserByUsername(String username);
    User findUserByEmail(String email);
}
