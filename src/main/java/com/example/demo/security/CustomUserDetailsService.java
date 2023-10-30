package com.example.demo.security;

import com.example.demo.model.exception.ResourceNotFoundException;
import com.example.demo.service.UserService;
import com.example.demo.shared.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userService.findByEmail(email);
    }


    public UserDetails loadUserById(Long id) {
        return userService.findById(id);
    }


//    private User getUser(Supplier<Optional<User>> supplier) {
//        return supplier.get().orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
//    }
}

