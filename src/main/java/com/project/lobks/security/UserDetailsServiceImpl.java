package com.project.lobks.security;

import com.project.lobks.entity.User;
import com.project.lobks.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findUserByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new UserDetailsImpl(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getStatusUser(),
                    user.getRole()
            );
        }
        throw new UsernameNotFoundException("user doesn't exists");
    }
}
