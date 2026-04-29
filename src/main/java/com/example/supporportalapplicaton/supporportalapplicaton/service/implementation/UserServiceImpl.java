package com.example.supporportalapplicaton.supporportalapplicaton.service.implementation;

import com.example.supporportalapplicaton.supporportalapplicaton.domain.User;
import com.example.supporportalapplicaton.supporportalapplicaton.domain.UserPrincipal;
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

import javax.transaction.Transactional;
import java.util.Date;

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
}
