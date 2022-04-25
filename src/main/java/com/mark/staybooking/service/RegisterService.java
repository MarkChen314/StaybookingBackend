package com.mark.staybooking.service;

import com.mark.staybooking.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mark.staybooking.repository.AuthorityRepository;
import com.mark.staybooking.repository.UserRepository;

import com.mark.staybooking.model.Authority;
import com.mark.staybooking.model.User;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.mark.staybooking.exception.UserAlreadyExistException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.mark.staybooking.repository.ReservationRepository;
import com.mark.staybooking.repository.StayReservationDateRepository;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class RegisterService {
    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private PasswordEncoder passwordEncoder;



    @Autowired
    public RegisterService(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }




    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void add(User user, UserRole role) throws UserAlreadyExistException {
        if (userRepository.existsById(user.getUsername())) {
            throw new UserAlreadyExistException("User already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        userRepository.save(user);
        authorityRepository.save(new Authority(user.getUsername(), role.name()));
    }

}