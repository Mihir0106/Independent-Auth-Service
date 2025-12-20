package com.Independent.AuthService.Service;

import com.Independent.AuthService.Model.User;
import com.Independent.AuthService.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.security.autoconfigure.SecurityProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = repository.findByEmail(email);

        if(user != null){
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles("USER")
                    //.disabled(!user.isVerified())
                    .build();
        }else{
            throw new UsernameNotFoundException("User name not found with email : " + email);
        }
    }

    public boolean verifyUser(String token){
        Optional<User> userOptional = repository.findByVerificationToken(token);

        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setVerified(true);
            user.setVerificationToken(null); // clear the token
            repository.save(user);
            return true;
        }

        return  false;
    }


}
