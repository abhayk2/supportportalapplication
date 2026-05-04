package com.example.supporportalapplicaton.service.implementation;

import com.example.supporportalapplicaton.domain.User;
import com.example.supporportalapplicaton.domain.UserPrincipal;
import com.example.supporportalapplicaton.exception.domain.EmailExistsException;
import com.example.supporportalapplicaton.exception.domain.UserNameExistsException;
import com.example.supporportalapplicaton.exception.domain.UserNotFoundException;
import com.example.supporportalapplicaton.repository.UserRepository;
import com.example.supporportalapplicaton.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.example.supporportalapplicaton.enumeration.Role.ROLE_USER;

@Service
@Transactional
@Qualifier("userDetailService")
public class UserServiceImpl implements UserService, UserDetailsService {
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists!";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists!";
    public static final String USER_IMAGE_PROFILE_TEMP = "/user/image/profile/temp";
    private final UserRepository userRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userDetails = userRepository.findUserByUsername(username);
        if (null == userDetails) {
            LOGGER.error("User not found by username: {}", username);
            throw new UsernameNotFoundException("User not found");
        } else {
            userDetails.setLastLoginDateDisplay(userDetails.getLastLoginDate());
            userDetails.setLastLoginDate(new Date());
            userRepository.save(userDetails);
            UserPrincipal userPrincipal = new UserPrincipal(userDetails);
            LOGGER.info("User found by username: {}", username);
            return userPrincipal;
        }
    }

    @Override
    public User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, UserNameExistsException, EmailExistsException {
        validateNewUsernameAndEmail("", username, email);
        User user = new User();
        user.setUserId(generateUserId());
        String password = generatePassword();
        String encodedPassword = encodePassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setJoinDate(new Date());
        user.setUsername(username);
        user.setIsActive(true);
        user.setNotLocked(true);
        user.setRoles(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporarilyImageUrl());
        userRepository.save(user);
        LOGGER.info("User registered successfully with password: {}", password);
        return user;
    }

    private String getTemporarilyImageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PROFILE_TEMP).toUriString();
    }

    private String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    private String generatePassword() {
        return new Random().ints(6, 'a', 'z' + 1).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    private String generateUserId() {
        return "user" + System.currentTimeMillis();
    }

    private User validateNewUsernameAndEmail(String currentUserName, String newUserName, String newEmail) throws UserNotFoundException, UserNameExistsException, EmailExistsException {

        User userByUsername = findUserByUsername(newUserName);
        User findByEmail = findUserByEmail(newEmail);
        if (StringUtils.hasText(currentUserName)) {
            User currentUser = findUserByUsername(currentUserName);
            if (null == currentUser) {
                throw new UserNotFoundException("User not found with username: " + currentUserName);
            }
            if (null != userByUsername && !currentUser.getId().equals(userByUsername.getId())) {
                throw new UserNameExistsException(USERNAME_ALREADY_EXISTS);
            }

            if (null != findByEmail && !currentUser.getId().equals(findByEmail.getId())) {
                throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
            }

            return currentUser;
        } else {
            if (null != userByUsername) {
                throw new UserNameExistsException(USERNAME_ALREADY_EXISTS);
            }
            if (findByEmail != null) {
                throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
