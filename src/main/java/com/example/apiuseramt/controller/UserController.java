package com.example.apiuseramt.controller;
import com.example.apiuseramt.entity.user.User;
import com.example.apiuseramt.entity.user.UserAuthImpl;
import com.example.apiuseramt.payload.response.JwtResponse;
import com.example.apiuseramt.repository.UserRepository;
import com.example.apiuseramt.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    final UserRepository userRepository;

    final PasswordEncoder encoder;

    final AuthenticationManager authenticationManager;

    final JwtUtils jwtUtils;

    @Autowired
    public UserController(
            UserRepository userRepository,
            PasswordEncoder encoder,
            @Qualifier("admin_manager") AuthenticationManager authenticationManager,
            JwtUtils jwtUtils
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserAuthImpl userImpl = (UserAuthImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userImpl.getUsername());

        return ResponseEntity.ok(new JwtResponse(jwt));
    }



    @PostMapping("/register")
    public ResponseEntity<?> register(String name, String email, String password) {
        //TODO: check si user n'existe pas
        System.out.println(name);
        System.out.println(email);
        System.out.println(password);
        try {
            User newUser = new User(
                    name,
                    email,
                    encoder.encode(password)
            );
            userRepository.save(newUser);
            return new ResponseEntity<>(
                    "C'est ok",
                    HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(
                    "C'est pas ok",
                    HttpStatus.BAD_REQUEST);
        }
    }

}
