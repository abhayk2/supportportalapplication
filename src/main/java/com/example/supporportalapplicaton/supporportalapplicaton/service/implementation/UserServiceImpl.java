package com.example.supporportalapplicaton.supporportalapplicaton.service.implementation;

import com.example.supporportalapplicaton.supporportalapplicaton.domain.User;
import com.example.supporportalapplicaton.supporportalapplicaton.domain.UserPrincipal;
import com.example.supporportalapplicaton.supporportalapplicaton.exception.domain.EmailExistsException;
import com.example.supporportalapplicaton.supporportalapplicaton.exception.domain.UserNameExistsException;
import com.example.supporportalapplicaton.supporportalapplicaton.exception.domain.UserNotFoundException;
import com.example.supporportalapplicaton.supporportalapplicaton.repository.UserRepository;
import com.example.supporportalapplicaton.supporportalapplicaton.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Qualifier("userDetailService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final Logger LOGGER  = LoggerFactory.getLogger(getClass());

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userDetails = userRepository.findUserByUsername(username);
        if(null==userDetails){
            LOGGER.error("User not found by username: {}", username);
            throw new UsernameNotFoundException("User not found");
        }else{
            userDetails.setLastLoginDateDisplay(userDetails.getLastLoginDate());
            userDetails.setLastLoginDate(new Date());
            userRepository.save(userDetails);
            UserPrincipal userPrincipal = new UserPrincipal(userDetails);
            LOGGER.info("User found by username: {}", username);
            return userPrincipal;
        }
    }

    @Override
    public User register(String firstName, String lastName, String username ,String email) throws UserNotFoundException, UserNameExistsException, EmailExistsException {
        validateNewUsernameAndEmail("",username,email);
        return null;
    }

    private User validateNewUsernameAndEmail(String currentUserName,String newUserName, String newEmail) throws UserNotFoundException, UserNameExistsException, EmailExistsException {
        if(StringUtils.hasText(currentUserName)){
            User currentUser = findUserByUsername(currentUserName);
            if(null == currentUser){
                throw new UserNotFoundException("User not found with username: "+currentUserName);
            }
            User userByUsername = findUserByUsername(newUserName);
            if(null != userByUsername && !currentUser.getId().equals(userByUsername.getId())){
                throw new UserNameExistsException("Username "+newUserName+" already exist ");
            }

            User userByEmail = findUserByEmail(newEmail);
            if(null != userByEmail && !currentUser.getId().equals(userByEmail.getId())){
                throw new EmailExistsException("Email "+userByEmail+" already exist ");
            }
            return currentUser;
        }else{
            User userByUsername = findUserByUsername(newUserName);
            if(null!=userByUsername ){
                throw new UserNameExistsException("Username "+ userByUsername.getUsername()+ " already Exists! ");
            }

            User findByEmail = findUserByEmail(newEmail);
            if(findByEmail != null){
                throw new EmailExistsException("Email "+ newEmail+ " already exists!");
            }
            return null;
        }
    }

    @Override
    public List<User> getUsers() {
        return List.of();
    }

    @Override
    public User findUserByUsername(String username) {
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return null;
    }
}
