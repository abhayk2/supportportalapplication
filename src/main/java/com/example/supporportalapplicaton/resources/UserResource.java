package com.example.supporportalapplicaton.resources;

import com.example.supporportalapplicaton.domain.User;
import com.example.supporportalapplicaton.domain.UserPrincipal;
import com.example.supporportalapplicaton.exception.domain.EmailExistsException;
import com.example.supporportalapplicaton.exception.domain.UserNameExistsException;
import com.example.supporportalapplicaton.exception.domain.UserNotFoundException;
import com.example.supporportalapplicaton.service.UserService;
import com.example.supporportalapplicaton.utility.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import static com.example.supporportalapplicaton.constant.SecurityConstant.JWT_TOKEN_HEADER;

@RestController
@RequestMapping(path = {"/user"})
public class UserResource {

    UserService userService;
    private AuthenticationManager authenticationManager;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public UserResource(UserService userService, AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @GetMapping("/home")
    public String show() throws UserNotFoundException {
        throw new UserNotFoundException("This email is already in taken");
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, UserNameExistsException, EmailExistsException {
       User loginUser =  userService.register(user.getFirstName(),user.getLastName(),user.getUsername(),user.getEmail());
       return new ResponseEntity<>(loginUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user){
        System.out.println("Login hit");
        authenticate(user.getUsername(),user.getPassword());
        User loginUser =  userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser,jwtHeader, HttpStatus.OK);
    }

    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
        HttpHeaders jwtHeader = new HttpHeaders();
        jwtHeader.add(JWT_TOKEN_HEADER,jwtTokenProvider.generateToken(userPrincipal));
        return jwtHeader;
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException e) {
            System.out.println("BAD CREDENTIALS: " + e.getMessage());
            throw e;
        } catch (DisabledException e) {
            System.out.println("ACCOUNT DISABLED: " + e.getMessage());
            throw e;
        } catch (LockedException e) {
            System.out.println("ACCOUNT LOCKED: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("OTHER ERROR: " + e.getClass().getName() + " - " + e.getMessage());
            throw e;
        }
    }

}
