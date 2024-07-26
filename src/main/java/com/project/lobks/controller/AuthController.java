package com.project.lobks.controller;

import com.project.lobks.dto.jwt.request.LoginRequest;
import com.project.lobks.dto.jwt.request.SighUpRequest;
import com.project.lobks.dto.jwt.response.JwtResponse;
import com.project.lobks.dto.jwt.response.MessageResponse;
import com.project.lobks.entity.Role;
import com.project.lobks.entity.StatusUser;
import com.project.lobks.entity.User;
import com.project.lobks.repository.UserRepository;
import com.project.lobks.security.UserDetailsImpl;
import com.project.lobks.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return new ResponseEntity<>(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles),
                HttpStatus.OK);
    }
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@RequestBody SighUpRequest sighUpRequest) throws RoleNotFoundException {
        if (userRepository.existsByUsername(sighUpRequest.getUsername())) {
            return new ResponseEntity<>(new MessageResponse("Error: Username is already taken"), HttpStatus.BAD_REQUEST);

        }
        if (userRepository.existsByEmail(sighUpRequest.getEmail())) {
            return new ResponseEntity<>(new MessageResponse("Error: Email is already taken"), HttpStatus.BAD_REQUEST);
        }

        String role = sighUpRequest.getRole();
        Role currentRole = null;
        if (role.equals("user")) {
            currentRole = Role.USER;
        } else if (role.equals("admin")) {
            currentRole = Role.ADMIN;
        } else {
            throw new RoleNotFoundException("Error: Role doesn't exists");
        }
        User user = User.builder()
                .username(sighUpRequest.getUsername())
                .email(sighUpRequest.getEmail())
                .password(passwordEncoder.encode(sighUpRequest.getPassword()))
                .statusUser(StatusUser.ACTIVE)
                .role(currentRole)
                .build();
        userRepository.save(user);
        return new ResponseEntity<>(new MessageResponse("User registered successfully"), HttpStatus.OK);
    }
}
